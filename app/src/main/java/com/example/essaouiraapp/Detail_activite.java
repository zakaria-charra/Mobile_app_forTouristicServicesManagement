package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detail_activite extends AppCompatActivity {
    private static final int REQ_CALL=1;
    TextView nomA,desc,date,adresse;
    ImageView call,email,loc;
    ViewFlipper A_flip;
    Activite activite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activite=(Activite)getIntent().getSerializableExtra("Activite") ;
        setContentView(R.layout.activity_detail_activite);
        nomA=findViewById(R.id.ANom);
        desc=findViewById(R.id.descA);
        date=findViewById(R.id.date);
        call=findViewById(R.id.tel_A);
        loc=findViewById(R.id.locA);
        email=findViewById(R.id.btnemail);

        nomA.setText(activite.getNom());
        desc.setText(activite.getDescription());

        if(activite.getDebut().equals("-")){
            date.setText("non specifié");
        }else {
            date.setText(activite.getDebut()+" à "+activite.getFin());
        }
        //appelle
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });
        //email
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:"+activite.getEmail()));
                startActivity(intent);
            }
        });

        //map
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps=new Intent(Intent.ACTION_VIEW);
                maps.setData(Uri.parse("http://maps.google.co.in/maps?q="+activite.getAdresse()));
                startActivity(maps);
            }
        });


        A_flip=findViewById(R.id.A_flipper);

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
        A_flip.addView(img);
        A_flip.setFlipInterval(4000);
        A_flip.setAutoStart(true);
        A_flip.startFlipping();
        A_flip.setInAnimation(this, android.R.anim.slide_in_left);
        A_flip.setOutAnimation(this, android.R.anim.slide_out_right);
    }
    private void Call(){
        String tel="tel:" +activite.getTel();
        if (activite.getTel().isEmpty()){
            Toast.makeText(Detail_activite.this,"aucun numero",Toast.LENGTH_LONG).show();
        }else{
            if(ContextCompat.checkSelfPermission(Detail_activite.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Detail_activite.this,new String[]{Manifest.permission.CALL_PHONE},REQ_CALL);
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
                Toast.makeText(Detail_activite.this,"donner la permission !!",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showEmail(){
        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip=ClipData.newPlainText("text",activite.getEmail());
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(Detail_activite.this,"email copié",Toast.LENGTH_LONG).show();
    };
}
