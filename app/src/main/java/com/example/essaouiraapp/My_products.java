package com.example.essaouiraapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class My_products extends Fragment {
    private RecyclerView list;
    private DatabaseReference dataRef;
    private FirebaseRecyclerOptions<Produit> options;
    private Button delete;
    private FirebaseRecyclerAdapter<Produit, My_products.ProdAdapter> adapter;
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_my_products,container,false);
        final Fabricant fabricant=(Fabricant)getActivity().getIntent().getSerializableExtra("Fabricant");
        dataRef= FirebaseDatabase.getInstance().getReference("produits");
        dataRef.keepSynced(true);
        Query qr=dataRef.orderByChild("/vendeur").equalTo(fabricant.getId());
        list=v.findViewById(R.id.ListDest);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        options=new FirebaseRecyclerOptions.Builder<Produit>().setQuery(qr, Produit.class).build();
        adapter=new FirebaseRecyclerAdapter<Produit, My_products.ProdAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final My_products.ProdAdapter listViewHolder, int i, @NonNull final Produit produit) {
                listViewHolder.setNom(produit.getNom());
                listViewHolder.setPrix(produit.getPrix()+" DH");
                listViewHolder.setDate(produit.getDate());
                listViewHolder.getimages(produit.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(My_products.this.getActivity(),Detail_Destination.class);
                        intent.putExtra("ProduitA",produit);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
                delete=listViewHolder.item.findViewById(R.id.supp);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listViewHolder.Delete(produit.getId());
                    }
                });

            }

            @NonNull
            @Override
            public My_products.ProdAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new My_products.ProdAdapter(LayoutInflater.from(My_products.this.getActivity()).inflate(R.layout.row_list_prod_ad,parent,false));
            }
        };
        list.setAdapter(adapter);
        return v;

    }

    public class ProdAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public ProdAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();


        }


        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.descPA);
            Nom.setText(nom);
        }
        public void setPrix(String prix){
            TextView Prix=(TextView)item.findViewById(R.id.prx_PA);
            Prix.setText(prix);
        }
        public void setDate(String date){
            TextView Date=(TextView)item.findViewById(R.id.date_PA);
            Date.setText(date);
        }



        public void getimages(String id){
            Query qr= FirebaseDatabase.getInstance().getReference("produits").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.img_PA);
                    for (DataSnapshot data:dataSnapshot.child("photo").getChildren()){
                        list.add(data.getValue().toString());
                    }

                    Picasso.with(getActivity()).load(list.get(0))
                            .fit()
                            .centerCrop()
                            .into(image);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        };

        public void Delete(final String id){
                    AlertDialog.Builder builder =new AlertDialog.Builder(itemView.getRootView().getContext());
                    builder.setMessage("vous voulez vraiment supprimer ce produit ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("produits").child(id);
                                    ref.removeValue();
                                }
                            }).setNegativeButton("Non",null);
                    AlertDialog alert=builder.create();
                    alert.show();

                }
        }

    }



