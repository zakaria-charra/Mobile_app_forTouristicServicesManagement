package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class Liste_Activites extends AppCompatActivity {
    private RecyclerView list;
    ImageView back;
    private DatabaseReference dataRef;
    private ArrayList<Activite> ListAct;
    private FirebaseRecyclerOptions<Activite> options;
    private FirebaseRecyclerAdapter<Activite, Liste_Activites.ActivitetoAdapter> adapter;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__activites);
        back=findViewById(R.id.backA);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dataRef= FirebaseDatabase.getInstance().getReference("services").child("Activities");
        dataRef.keepSynced(true);
        list=findViewById(R.id.ListActivite);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        ListAct=new ArrayList<>();

        options=new FirebaseRecyclerOptions.Builder<Activite>().setQuery(dataRef, Activite.class).build();
        adapter=new FirebaseRecyclerAdapter<Activite, Liste_Activites.ActivitetoAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Liste_Activites.ActivitetoAdapter listViewHolder, int i, @NonNull final Activite activite) {
                listViewHolder.setNom(activite.getNom());
                listViewHolder.getimages(activite.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Liste_Activites.this,Detail_activite.class);
                        intent.putExtra("Activite",activite);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public Liste_Activites.ActivitetoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Liste_Activites.ActivitetoAdapter(LayoutInflater.from(Liste_Activites.this).inflate(R.layout.row_list_activite,parent,false));
            }
        };
        list.setAdapter(adapter);
    }


    public class ActivitetoAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public ActivitetoAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();

        }
        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.nomA);
            Nom.setText(nom);
        }


        public void getimages(String id){
            Query qr= FirebaseDatabase.getInstance().getReference("services").child("Activities").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.img_A);
                    for (DataSnapshot data:dataSnapshot.child("photo").getChildren()){
                        list.add(data.getValue().toString());
                    }

                    Picasso.with(getApplicationContext()).load(list.get(0)).into(image);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        };

    }
}
