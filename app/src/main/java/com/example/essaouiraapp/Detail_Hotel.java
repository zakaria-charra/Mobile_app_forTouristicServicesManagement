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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detail_Hotel extends AppCompatActivity {
    private static final int REQ_CALL=1;
    TextView nomH,desc,chambre,prix;
    ImageView call,loc;
    ViewFlipper H_flip;
    Hotel hotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__hotel);

        hotel=(Hotel)getIntent().getSerializableExtra("Hotel");
        nomH=findViewById(R.id.HNom);
        desc=findViewById(R.id.descH);
        chambre=findViewById(R.id.nbChamb);
        prix=findViewById(R.id.prix);
        loc=findViewById(R.id.locH);



        nomH.setText(hotel.getNom());
        desc.setText(hotel.getDescription());
        prix.setText("A partir de "+hotel.getPrix()+" DH");
        chambre.setText(hotel.getChambres()+" Chambres");
//call
        call=findViewById(R.id.telH);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });
        H_flip=findViewById(R.id.H_flipper);
        ArrayList<String> img=getIntent().getStringArrayListExtra("photo");
        //map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+hotel.getAdresse()));
                startActivity(maps);
            }
        });


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
        H_flip.addView(img);
        H_flip.setFlipInterval(4000);
        H_flip.setAutoStart(true);
        H_flip.startFlipping();
        H_flip.setInAnimation(this, android.R.anim.slide_in_left);
        H_flip.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void Call(){
        String tel="tel:" +hotel.getTel();
        if (hotel.getTel().isEmpty()){
            Toast.makeText(Detail_Hotel.this,"aucun numero",Toast.LENGTH_LONG).show();
        }else{
            if(ContextCompat.checkSelfPermission(Detail_Hotel.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Detail_Hotel.this,new String[]{Manifest.permission.CALL_PHONE},REQ_CALL);
            }else{
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(tel));
                startActivity(intent);}
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Call();
            }else {
                Toast.makeText(Detail_Hotel.this,"donner la permission !!",Toast.LENGTH_LONG).show();
            }

        }
    }
}
