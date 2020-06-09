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

public class Liste_Destinations extends AppCompatActivity {
    private RecyclerView list;
    ImageView back;
    private DatabaseReference dataRef;
    private ArrayList<Destination> ListDest;
    private FirebaseRecyclerOptions<Destination> options;
    private FirebaseRecyclerAdapter<Destination, Liste_Destinations.DestinationAdapter> adapter;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__destinations);


        back=findViewById(R.id.backD);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dataRef= FirebaseDatabase.getInstance().getReference("services").child("Detinations");
        dataRef.keepSynced(true);
        list=findViewById(R.id.ListDest);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        ListDest=new ArrayList<>();

        options=new FirebaseRecyclerOptions.Builder<Destination>().setQuery(dataRef, Destination.class).build();
        adapter=new FirebaseRecyclerAdapter<Destination, Liste_Destinations.DestinationAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Liste_Destinations.DestinationAdapter listViewHolder, int i, @NonNull final Destination destination) {
                listViewHolder.setNom(destination.getNom());
                listViewHolder.getimages(destination.getId());
                listViewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Liste_Destinations.this,Detail_Destination.class);
                        intent.putExtra("Destination",destination);
                        intent.putExtra("photo",listViewHolder.list);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public Liste_Destinations.DestinationAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Liste_Destinations.DestinationAdapter(LayoutInflater.from(Liste_Destinations.this).inflate(R.layout.row_list_destination,parent,false));
            }
        };
        list.setAdapter(adapter);

    }


    public class DestinationAdapter extends RecyclerView.ViewHolder {
        View item;
        ArrayList<String>list;
        public DestinationAdapter( View itemView) {
            super(itemView);
            item=itemView;
            this.list=new ArrayList<String>();

        }
        public void setNom(String nom){
            TextView Nom=(TextView)item.findViewById(R.id.nomD);
            Nom.setText(nom);
        }


        public void getimages(String id){
            Query qr= FirebaseDatabase.getInstance().getReference("services").child("Detinations").child(id).orderByChild("photo");
            qr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageView image=item.findViewById(R.id.img_D);
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
