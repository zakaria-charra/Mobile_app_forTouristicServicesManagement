package com.example.essaouiraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFabricant extends AppCompatActivity {

    TextView text;
    BottomNavigationView navigation;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage("Etes-vous sûr de vous déconnecter?")
                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeFabricant.this,Authentification.class));
                        finish();
                    }
                }).setNegativeButton("Non",null);
        AlertDialog alert=builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fabricant);
            Fabricant fabricant=(Fabricant)getIntent().getSerializableExtra("Fabricant");

            navigation=findViewById(R.id.bottommenu);
            navigation.setOnNavigationItemSelectedListener(navListner);


        getSupportFragmentManager().beginTransaction().replace(R.id.myfragment,new FirstPageFabricant()).commit();

            }


            BottomNavigationView.OnNavigationItemSelectedListener navListner=
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            Fragment selectedFragment=null;
                            switch (menuItem.getItemId()){
                                case R.id.nav_desc: selectedFragment=new FirstPageFabricant();break;
                                case R.id.nav_add: selectedFragment=new Add_produit();break;
                                case R.id.nav_home: selectedFragment=new My_products();break;
                                case R.id.nav_profil: selectedFragment=new profile();break;
                                case R.id.nav_contact: selectedFragment=new messages();break;
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.myfragment,selectedFragment).commit();
                           return true;
                        }
                    };
}
