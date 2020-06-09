package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detail_produit extends AppCompatActivity {

    TextView nom,prix,adress,quantite,desc;
    private static final int REQ_CALL=1;
    ImageView call,email,loc;
    String Tel;
    Produit produit;
    ViewFlipper P_flip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produit);
        produit=(Produit)getIntent().getSerializableExtra("Produit");
        P_flip=findViewById(R.id.P_flipper);
        nom=findViewById(R.id.nom_prd);
        prix=findViewById(R.id.prix_prd);
        desc=findViewById(R.id.desc_prd);
        quantite=findViewById(R.id.quantite_prd);
        adress=findViewById(R.id.adress_prd);
        call=findViewById(R.id.telP);
        email=findViewById(R.id.messageP);
        loc=findViewById(R.id.locPro);
        nom.setText(produit.getNom());
        prix.setText("A partir de "+produit.getPrix()+" DH");
        desc.setText(produit.getDescription());
        quantite.setText("Quantité: "+produit.getQuantite());
        adress.setText(produit.getAdresse());
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SendMessage(produit.getVendeur());
            }
        });

        //call
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call(produit.getVendeur());
            }
        });

        //map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+produit.getAdresse()));
                startActivity(maps);
            }
        });

        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+produit.getAdresse()));
                startActivity(maps);
            }
        });


        ArrayList<String> img=getIntent().getStringArrayListExtra("photo");

        for (int i=0;i< img.size();i++){
            String url=img.get(i);
            flipper(url);
        }
    }

    private void flipper(String image) {
        ImageView img=new ImageView(this);
        Picasso.with(this)
                .load(image)
                .fit()
                .centerCrop()
                .into(img);

        P_flip.addView(img);
        P_flip.setFlipInterval(4000);
        P_flip.setAutoStart(true);
        P_flip.startFlipping();
        P_flip.setInAnimation(this, android.R.anim.slide_in_left);
        P_flip.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void Call(String id){
        FirebaseDatabase.getInstance().getReference().child("Fabricant_Traditionnel").child(id).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String tel="tel:" +dataSnapshot.getValue().toString();
                if (dataSnapshot.getValue().toString().isEmpty()){
                    Toast.makeText(Detail_produit.this,"aucune numero",Toast.LENGTH_LONG).show();
                }else{
                    if(ContextCompat.checkSelfPermission(Detail_produit.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(Detail_produit.this,new String[]{Manifest.permission.CALL_PHONE},REQ_CALL);
                    }else{
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        startActivity(intent);}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void SendMessage(String id){
        FirebaseDatabase.getInstance().getReference().child("Fabricant_Traditionnel").child(id).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email=dataSnapshot.getValue().toString();
                if (dataSnapshot.getValue().toString().isEmpty()){
                    Toast.makeText(Detail_produit.this,"aucune email",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("mailto:"+email));

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Call(produit.getVendeur());
            }else {
                Toast.makeText(Detail_produit.this,"donner la permission !!",Toast.LENGTH_LONG).show();
            }

        }
    }







}
