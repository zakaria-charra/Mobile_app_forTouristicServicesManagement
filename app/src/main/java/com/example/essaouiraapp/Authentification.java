package com.example.essaouiraapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Authentification extends AppCompatActivity {

    private Spinner spinner;
    EditText Temail,Tpass,Tid;
    Button btn;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        progress=new ProgressDialog(this);
        spinner=findViewById(R.id.spinner);
        Temail=findViewById(R.id.emailLog);
        Tpass=findViewById(R.id.passwordLog);
        Tid=findViewById(R.id.userIDLog);
        btn=findViewById(R.id.Loginbtn);
        List<String> ls=new ArrayList<>();
        ls.add(0,"Qui êtes vous?");
        ls.add("Touriste");
        ls.add("Fabricant Traditionel");
        ArrayAdapter<String> dataAadapt;
        dataAadapt=new ArrayAdapter(this,android.R.layout.simple_spinner_item,ls) ;
        dataAadapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAadapt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String Qui=spinner.getSelectedItem().toString();
                 String id=Tid.getText().toString().trim();
                 String email=Temail.getText().toString().trim();
                 String pass=Tpass.getText().toString().trim();
                if(!validEmail()|!validEmpty(Tid,id)|!validEmpty(Temail,email)| !validEmpty(Tpass,pass)){
                    return;
                }else if(Qui.equals("Qui êtes vous?")){
                    Toast.makeText(Authentification.this,"Qui êtes vous?",Toast.LENGTH_LONG).show();
                }else if(Qui.equals("Fabricant Traditionel")){
                    LoginFab();
                }else{
                    LoginTourist();
                }
            }
        });


    }


    public Boolean validEmpty(EditText edit, String val){
        if(val.isEmpty()) { edit.setError("Remplir ce champ"); return false;
        }else{
            return true;}
    };
    public Boolean validEmail(){
        String email = Temail.getText().toString();
        String regex = "^(.+)@(.+)$";
        if(!email.matches(regex)){ Temail.setError("format incorrect"); return false;}
        else{return true;}
    };



    private void LoginFab(){
        final String id=Tid.getText().toString().trim();
        final String email=Temail.getText().toString().trim();
        final String pass=Tpass.getText().toString().trim();
        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("Fabricant_Traditionnel");
        Query isUser=dataRef.orderByChild("id").equalTo(id);
        isUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String isemail=dataSnapshot.child(id).child("email").getValue().toString();
                    String ispass=dataSnapshot.child(id).child("password").getValue().toString();
                    if(email.equals(isemail) && pass.equals(ispass)){
                        progress.setMessage("Se connecter..");
                        progress.show();
                        String nom=dataSnapshot.child(id).child("nom").getValue().toString();
                        String prenom=dataSnapshot.child(id).child("prenom").getValue().toString();
                        String adresse=dataSnapshot.child(id).child("adresse").getValue().toString();
                        String Tele=dataSnapshot.child(id).child("phone").getValue().toString();
                        Fabricant fabricant =new Fabricant(id,nom,prenom,email,pass,adresse,Tele);

                        Intent intent=new Intent(Authentification.this,HomeFabricant.class);
                        intent.putExtra("Fabricant",fabricant);
                        startActivity(intent);
                        finish();
                    }else {
                        Temail.setError("Verifier vootre email");
                        Tpass.setError("Verifier votre password");
                    }

                }else{
                    Tid.setError("Ce ID n'exist pas !!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Authentification.this,"Erreur de Connexion",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void LoginTourist(){
        final String id=Tid.getText().toString().trim();
        final String email=Temail.getText().toString().trim();
        final String pass=Tpass.getText().toString().trim();
        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("Tourists");
        Query isUser=dataRef.orderByChild("id").equalTo(id);
        isUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String isemail=dataSnapshot.child(id).child("email").getValue().toString();
                    String ispass=dataSnapshot.child(id).child("password").getValue().toString();
                    if(email.equals(isemail) && pass.equals(ispass)){
                        progress.setMessage("Se connecter..");
                        progress.show();
                        String nom=dataSnapshot.child(id).child("nom").getValue().toString();
                        String prenom=dataSnapshot.child(id).child("prenom").getValue().toString();
                        String pays=dataSnapshot.child(id).child("pays").getValue().toString();
                        Touriste touriste =new Touriste(id,nom,prenom,email,pays,pass);

                        Intent intent=new Intent(Authentification.this,HomeTouriste.class);
                        intent.putExtra("Touriste",touriste);
                        startActivity(intent);
                        finish();
                    }else {
                        Temail.setError("Verifier vootre email");
                        Tpass.setError("Verifier votre password");
                    }

                }else{
                    Tid.setError("Ce ID n'exist pas !!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Authentification.this,"Erreur de Connexion",Toast.LENGTH_LONG).show();
            }
        });

    }


}
