package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.List;

public class Liste_Restaurants extends AppCompatActivity   {

    private RecyclerView list;
    ImageView back;
    private DatabaseReference dataRef;
    private ArrayList<Restaurant> ListRest;
    private FirebaseRecyclerOptions<Restaurant> options;
    private FirebaseRecyclerAdapter<Restaurant,RestoAdapter> adapter;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__restaurants);

        back=findViewById(R.id.backR);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dataRef= FirebaseDatabase.getInstance().getReference("services").child("Restaurants");
        dataRef.keepSynced(true);
        list=findViewById(R.id.ListResto);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        ListRest=new ArrayList<>();

        options=new FirebaseRecyclerOptions.Builder<Restaurant>().setQuery(dataRef, Restaurant.class).build();
        adapter=new FirebaseRecyclerAdapter<Restaurant, RestoAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RestoAdapter listViewHolder, int i, @NonNull final Restaurant restaurant) {
                listViewHolder.setNom(restaurant.getNom());
                listViewHolder.setDesc(restaurant.getDescription());
                listViewHolder.getimages(restaurant.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Liste_Restaurants.this,Detail_Restaurant.class);
                        intent.putExtra("Resto",restaurant);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public RestoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RestoAdapter(LayoutInflater.from(Liste_Restaurants.this).inflate(R.layout.row_list,parent,false));
            }
        };
            list.setAdapter(adapter);
    }


    public class RestoAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public RestoAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();

        }
        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.row_titre);
            Nom.setText(nom);
        }
        public void setDesc(String desc){
            TextView Desc=(TextView)item.findViewById(R.id.row_desc);
            Desc.setText(desc);
        }
        public void setImg(String desc){
            ImageView image=item.findViewById(R.id.row_img);
            Picasso.with(getApplicationContext()).load(desc).into(image);

        }
        public void getimages(String id){
            Query qr=FirebaseDatabase.getInstance().getReference("services").child("Restaurants").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.row_img);
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
