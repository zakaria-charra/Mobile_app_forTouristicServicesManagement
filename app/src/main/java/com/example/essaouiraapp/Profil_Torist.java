package com.example.essaouiraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profil_Torist extends AppCompatActivity {
    TextView fullname,id;
    EditText nom,paye,email,prenom;
    Button update;
    ImageView Back;
    ProgressDialog progress;

    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil__torist);

        progress=new ProgressDialog(this);
        progress.setMessage("Modification en cours...");
        final Touriste touriste=(Touriste) getIntent().getSerializableExtra("Touriste");
        Back=findViewById(R.id.backP);
        fullname =findViewById(R.id.Trname);
        id =findViewById(R.id.Tr_id);
        nom =findViewById(R.id.nomT);
        prenom =findViewById(R.id.PrenT);
        email =findViewById(R.id.mailT);
        paye =findViewById(R.id.payT);
        //values
        fullname.setText(touriste.getNom()+" "+touriste.getPrenom());
        id.setText(touriste.getId());
        nom.setText(touriste.getNom());
        prenom.setText(touriste.getPrenom());
        paye.setText(touriste.getPays());
        email.setText(touriste.getEmail());
        update=findViewById(R.id.updProfilT);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                UpdateProfil(touriste.getId(),touriste.getPassword());

            }
        });


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void UpdateProfil(String id,String pass){
        final String nm=nom.getText().toString();
        final String pnm=prenom.getText().toString();
        String adress=paye.getText().toString();
        String mail=email.getText().toString();

        Touriste touriste=new Touriste(id,nm,pnm,mail,adress,pass);
        FirebaseDatabase.getInstance().getReference("Tourists")
                .child(id).setValue(touriste).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fullname.setText(nm+" "+pnm);
                progress.dismiss();
            }
        });
    }

}
