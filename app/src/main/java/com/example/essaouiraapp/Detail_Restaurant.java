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

public class Detail_Restaurant extends AppCompatActivity {

    private static final int REQ_CALL=1;
    TextView text;
    TextView desc;
    ImageView call,loc;
    ViewFlipper R_flip;
    Restaurant resto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__restaurant);

       resto=(Restaurant)getIntent().getSerializableExtra("Resto");
        text=findViewById(R.id.DetNom);
        desc=findViewById(R.id.descR);
        loc=findViewById(R.id.locR);
        call=findViewById(R.id.telR);
        R_flip=findViewById(R.id.R_flipper);
        text.setText(resto.getNom());
        desc.setText(resto.getDescription());

        //call
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });


        //map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+resto.getAdresse()));
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
        R_flip.addView(img);
        R_flip.setFlipInterval(4000);
        R_flip.setAutoStart(true);
        R_flip.startFlipping();
        R_flip.setInAnimation(this, android.R.anim.slide_in_left);
        R_flip.setOutAnimation(this, android.R.anim.slide_out_right);
    }
    private void Call(){
        String tel="tel:" +resto.getTel();
        if (resto.getTel().isEmpty()){
            Toast.makeText(Detail_Restaurant.this,"aucun numero",Toast.LENGTH_LONG).show();
        }else{
            if(ContextCompat.checkSelfPermission(Detail_Restaurant.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Detail_Restaurant.this,new String[]{Manifest.permission.CALL_PHONE},REQ_CALL);
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
                 Toast.makeText(Detail_Restaurant.this,"donner la permission !!",Toast.LENGTH_LONG).show();
             }

         }
    }
}
