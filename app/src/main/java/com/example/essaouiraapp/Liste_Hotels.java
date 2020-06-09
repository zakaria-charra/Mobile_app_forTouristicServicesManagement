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

public class Liste_Hotels extends AppCompatActivity {

    private RecyclerView list;
    ImageView back;
    private DatabaseReference dataRef;
    private FirebaseRecyclerOptions<Hotel> options;
    private FirebaseRecyclerAdapter<Hotel, Liste_Hotels.HotelAdapter> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__hotels);
        back=findViewById(R.id.backH);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dataRef= FirebaseDatabase.getInstance().getReference("services").child("Hotels");
        dataRef.keepSynced(true);
        list=findViewById(R.id.ListHotel);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        options=new FirebaseRecyclerOptions.Builder<Hotel>().setQuery(dataRef,Hotel.class).build();
        adapter=new FirebaseRecyclerAdapter<Hotel, Liste_Hotels.HotelAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Liste_Hotels.HotelAdapter listViewHolder, int i, @NonNull final Hotel hotel) {
                listViewHolder.setNom(hotel.getNom());
                listViewHolder.setDesc(hotel.getDescription());
               listViewHolder.setPrix(hotel.getPrix());
                listViewHolder.getimages(hotel.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Liste_Hotels.this,Detail_Hotel.class);
                        intent.putExtra("Hotel",hotel);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public Liste_Hotels.HotelAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Liste_Hotels.HotelAdapter(LayoutInflater.from(Liste_Hotels.this).inflate(R.layout.row_list_hotel,parent,false));
            }
        };
        list.setAdapter(adapter);
    }

    public class HotelAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public HotelAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();

        }
        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.nomH);
            Nom.setText(nom);
        }
        public void setPrix(String pr){
            TextView Prix=(TextView)item.findViewById(R.id.prixH);
            Prix.setText(pr+" DH");
        }
        public void setDesc(String desc){
            TextView Desc=(TextView)item.findViewById(R.id.descH);
            Desc.setText(desc);
        }

        public void getimages(String id){
            Query qr=FirebaseDatabase.getInstance().getReference("services").child("Hotels").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.img_H);
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
