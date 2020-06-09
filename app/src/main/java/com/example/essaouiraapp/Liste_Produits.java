package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Liste_Produits extends AppCompatActivity {

    private RecyclerView list;
    private Handler slideHand=new Handler();
    ImageView back;
    public ViewPager2 viewPager2;
    private DatabaseReference dataRef;
    private FirebaseRecyclerOptions<Produit> options;
    private FirebaseRecyclerAdapter<Produit, Liste_Produits.ProduitAdapter> adapter;
    ArrayList<String> images;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__produits);
        images=new ArrayList<String>();

        viewPager2=findViewById(R.id.viewPage);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                    float r=1-Math.abs(position);
                    page.setScaleY(0.85f+r*0.25f);
            }
        });

            viewPager2.setPageTransformer(compositePageTransformer);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    slideHand.removeCallbacks(slideRun);
                    slideHand.postDelayed(slideRun,3000);

                }
            });

        back=findViewById(R.id.backP);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        dataRef= FirebaseDatabase.getInstance().getReference("produits");
        dataRef.keepSynced(true);
        list=findViewById(R.id.ListViw);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        options=new FirebaseRecyclerOptions.Builder<Produit>().setQuery(dataRef,Produit.class).build();
        adapter=new FirebaseRecyclerAdapter<Produit, Liste_Produits.ProduitAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Liste_Produits.ProduitAdapter listViewHolder, int i, @NonNull final Produit produit) {
                listViewHolder.setNom(produit.getNom());
                listViewHolder.setUser(produit.getVendeur());
                listViewHolder.setDate(produit.getDate());
                listViewHolder.setPrix(produit.getPrix()+" DH");
                listViewHolder.getimages(produit.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Liste_Produits.this,Detail_produit.class);
                        intent.putExtra("Produit",produit);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public Liste_Produits.ProduitAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Liste_Produits.ProduitAdapter(LayoutInflater.from(Liste_Produits.this).inflate(R.layout.row_list_produit,parent,false));
            }
        };
        list.setAdapter(adapter);



    }

    private Runnable slideRun=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };
    public class ProduitAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public ProduitAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();

        }
        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.descP);
            Nom.setText(nom);
        }
        public void setUser(String user){
            TextView User=(TextView)item.findViewById(R.id.UserN);
            User.setText(user);
        }
        public void setPrix(String prix){
            TextView Prix=(TextView)item.findViewById(R.id.prx_P);
            Prix.setText(prix);
        }
        public void setDate(String date){
            TextView Date=(TextView)item.findViewById(R.id.date_P);
            Date.setText(date);
        }



        public void getimages(String id){
            Query qr=FirebaseDatabase.getInstance().getReference("produits").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.img_P);
                    for (DataSnapshot data:dataSnapshot.child("photo").getChildren()){
                        list.add(data.getValue().toString());
                    }

                    Picasso.with(getApplicationContext()).load(list.get(0)).fit()
                            .centerCrop().into(image);

                    images.add(list.get(0));
                    viewPager2.setAdapter(new SliderAdapter(viewPager2,images,getApplicationContext()));


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        };

    }
}
