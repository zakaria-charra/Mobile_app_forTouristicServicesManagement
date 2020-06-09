package com.example.essaouiraapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegistrationTourist extends AppCompatActivity  {
    Spinner spinner;
    private EditText Tid,Tnom,Tprenom,Temail,Tpass,Tcpass;
    Button btnsave;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_tourist);

        List<String> ls = new ArrayList<>();
        ls.add(0, "choisissez votre pays");
        String[] coun = Locale.getISOCountries();
        for (String nameCountry : coun) {
            Locale locale = new Locale("en", nameCountry);
            ls.add(locale.getDisplayCountry());
        }
        progress=new ProgressDialog(this);
        spinner =findViewById(R.id.spinnerT);
        Tid=findViewById(R.id.UserIdT);
        Tnom =findViewById(R.id.nomT);
        Tprenom =findViewById(R.id.prenomT);
        Temail =findViewById(R.id.emailT);
        Tpass =findViewById(R.id.passT);
        Tcpass =findViewById(R.id.CpassT);
        btnsave =findViewById(R.id.saveT);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Tourists");

        ArrayAdapter<String> dataAadapt;
        dataAadapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ls);
        dataAadapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAadapt);
        //ajouter user
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     VerifiedReg();
            }
        });

    }

//methodes
    private void register(){
        String pays = spinner.getSelectedItem().toString();
        String id=Tid.getText().toString().trim();
        String nom = Tnom.getText().toString();
        String prenom = Tprenom.getText().toString();
        String email = Temail.getText().toString();
        String pass = Tpass.getText().toString();
        String cpass=Tcpass.getText().toString();


        if(!validEmpty(Tid,id)| !validEmpty(Tnom,nom)| !validEmpty(Tprenom,prenom)|!validEmpty(Temail,email)| !validEmpty(Tpass,pass)| !validEmpty(Tcpass,cpass)
                |!validpays()|!validpass()|!validEmail() |!validLength(Tid,4)|!validLength(Tpass,5)){
                return;
        }
        else {
            progress.setMessage("Registration en cours ");
            progress.show();
            Touriste touriste = new Touriste(id, nom, prenom, email,pays,pass);
            myRef.child(id).setValue(touriste).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationTourist.this,"Registration avec success ",Toast.LENGTH_LONG).show();
                        progress.cancel();
                        Auth();
                    }
                    else{
                        Toast.makeText(RegistrationTourist.this,"Registration échoué !!",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }

    private void Auth(){
        Intent intent = new Intent(this,Authentification.class);
        startActivity(intent);
        finish();
    }

    public Boolean validEmpty(EditText edit,String val){
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

    public Boolean validpass(){
        String pass = Tpass.getText().toString();
        String cpass=Tcpass.getText().toString();
        if(!cpass.equals(pass)) {Tcpass.setError("Entrer le meme mot de passe"); return false;}
        else{return true;}
    };

    public Boolean validpays() {
        String pays = spinner.getSelectedItem().toString();
        if (pays.equals("choisissez votre pays")) {
            Toast.makeText(RegistrationTourist.this, "s'il vous plaît choisissez votre pays!!", Toast.LENGTH_LONG).show();return false;
        } else {
            return true;
        }
    };

    private void VerifiedReg(){
        String id=Tid.getText().toString().trim();
        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("Tourists");
        Query exist=dataRef.orderByChild("id").equalTo(id);
        exist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Tid.setError("ID déjà utilisé !!");
                }else{
                    register();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    };

    private Boolean validLength(EditText text,int nb){
        String txt=text.getText().toString().trim();
        if(txt.length()<nb){
            text.setError("au moins "+nb+" caractères");
            return false;
        }else{
            return true;
        }
    };

}
