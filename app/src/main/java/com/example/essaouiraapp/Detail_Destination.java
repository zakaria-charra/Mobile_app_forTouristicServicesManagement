package com.example.essaouiraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detail_Destination extends AppCompatActivity {
    TextView nom,desc,adrs;
    ViewFlipper D_flip;
    ImageView image,loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Destination destination=(Destination)getIntent().getSerializableExtra("Destination");
        setContentView(R.layout.activity_detail__destination);
        nom=findViewById(R.id.NomD);
        desc=findViewById(R.id.descD);
        adrs=findViewById(R.id.locD);
        loc=findViewById(R.id.locDe);
        image=findViewById(R.id.imgD);
        nom.setText(destination.getNom());
        desc.setText(destination.getDescription());
        adrs.setText(destination.getAdresse());

        ArrayList<String> img=getIntent().getStringArrayListExtra("photo");
        Picasso.with(Detail_Destination.this).load(img.get(0)).into(image);


        //map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+destination.getAdresse()));
                startActivity(maps);
            }
        });


        D_flip=findViewById(R.id.D_flipper);


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
        D_flip.addView(img);
        D_flip.setFlipInterval(4000);
        D_flip.setAutoStart(true);
        D_flip.startFlipping();
        D_flip.setInAnimation(this, android.R.anim.slide_in_left);
        D_flip.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}
