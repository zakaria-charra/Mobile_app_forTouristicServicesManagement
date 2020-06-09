package com.example.essaouiraapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegistrationFabricant extends AppCompatActivity {

    EditText Tid,Tnom,Tprenom,Temail,Tpass,TCpass,Tadress,Ttele;
    Button btnSave;
    ProgressDialog progress;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_fabricant);
        progress =new ProgressDialog(RegistrationFabricant.this);
        Tid=findViewById(R.id.UserIdF);
        Tnom=findViewById(R.id.nomF);
        Tprenom=findViewById(R.id.prenomF);
        Temail=findViewById(R.id.emailF);
        Tpass=findViewById(R.id.passF);
        TCpass=findViewById(R.id.CpassF);
        Tadress=findViewById(R.id.AdressF);
        Ttele=findViewById(R.id.TeleF);
        btnSave=findViewById(R.id.SignBtnF);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Fabricant_Traditionnel");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifiedReg();
            }
        });

    }





    private void RegistreFabricant(){
        String id=Tid.getText().toString().trim();
        String nom =Tnom.getText().toString();
        String prenom=Tprenom.getText().toString();
        String email=Temail.getText().toString();
        String pass=Tpass.getText().toString();
        String Cpass=TCpass.getText().toString();
        String adresse=Tadress.getText().toString();
        String tel=Ttele.getText().toString();
//validation
        if(!validEmpty(Tid,id)|!validEmpty(Tnom,nom)| !validEmpty(Tprenom,prenom)|!validEmpty(Temail,email)| !validEmpty(Tpass,pass)| !validEmpty(TCpass,Cpass)
                | !validEmpty(Tadress,adresse)| !validEmpty(Ttele,tel)|!validEmail()|!validpass()){
            return;
        }
        else {
            progress.setMessage("Registration en cours ");
            progress.show();
            Fabricant fab = new Fabricant(id, nom, prenom,email,pass,adresse,tel);
            myRef.child(id).setValue(fab).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationFabricant.this,"Registration avec success ",Toast.LENGTH_LONG).show();
                        progress.cancel();
                        Auth();
                    }
                    else{
                        Toast.makeText(RegistrationFabricant.this,"Registration échoué !!",Toast.LENGTH_LONG).show();
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
        String cpass=TCpass.getText().toString();
        if(!cpass.equals(pass)) {TCpass.setError("Entrer le meme mot de passe"); return false;}
        else{return true;}
    };

    private void VerifiedReg(){
        String id=Tid.getText().toString().trim();
        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("Fabricant_Traditionnel");
        Query exist=dataRef.orderByChild("id").equalTo(id);
        exist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Tid.setError("ID déjà utilisé !!");
                }else{
                    RegistreFabricant();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    };

}
