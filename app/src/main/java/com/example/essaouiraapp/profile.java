package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile extends Fragment {
    TextView fullname,id,countProd;
    EditText nom,prenom,adresse,email,phone;
    Button update;
    ProgressDialog progress;

    private DatabaseReference dataRef;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_profile,container,false);
            progress=new ProgressDialog(this.getContext());
            progress.setMessage("Modification en cours...");
         final Fabricant fabricant=(Fabricant)getActivity().getIntent().getSerializableExtra("Fabricant");
        countProd=v.findViewById(R.id.nbProd);
        fullname =v.findViewById(R.id.fabname);
        id =v.findViewById(R.id.fabid);
        nom =v.findViewById(R.id.nomU);
        prenom =v.findViewById(R.id.PrenomU);
        adresse =v.findViewById(R.id.adresseU);
        email =v.findViewById(R.id.emailU);
        phone =v.findViewById(R.id.phoneU);

        fullname.setText(fabricant.getNom()+" "+fabricant.getPrenom());
        id.setText(fabricant.getId());
        nom.setText(fabricant.getNom());
        prenom.setText(fabricant.getPrenom());
        adresse.setText(fabricant.getAdresse());
        email.setText(fabricant.getEmail());
        phone.setText(fabricant.getPhone());
        update=v.findViewById(R.id.updProfil);

        dataRef= FirebaseDatabase.getInstance().getReference("produits");

        Query qr=dataRef.orderByChild("/vendeur").equalTo(fabricant.getId());
        qr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                countProd.setText("vous avez publié "+count+" produits");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });//count

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                UpdateProfil(fabricant.getId(),fabricant.getPassword());
                refresh();
            }
        });


        return v;

    }


    public void UpdateProfil(String id,String pass){
        String nm=nom.getText().toString();
        String pnom=prenom.getText().toString();
        String adress=adresse.getText().toString();
        String tele=phone.getText().toString();
        String mail=email.getText().toString();

        Fabricant fab=new Fabricant(id,nm,pnom,mail,pass,adress,tele);
        FirebaseDatabase.getInstance().getReference("Fabricant_Traditionnel")
                .child(id).setValue(fab).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progress.dismiss();
            }
        });
    }

    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
        progress.dismiss();


    }
}
