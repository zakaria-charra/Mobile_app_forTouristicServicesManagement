package com.example.essaouiraapp;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class HomeTouriste extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu;
    CardView CRest,CHot,CProd,CDest,CAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_touriste);
        //hooks
        menu=findViewById(R.id.icon_menu);
        drawerLayout=findViewById(R.id.drawL);
        navigationView=findViewById(R.id.navV);
        CRest=findViewById(R.id.cardRes);
        CHot=findViewById(R.id.cardHotel);
        CProd=findViewById(R.id.cardTra);
        CDest=findViewById(R.id.cardDest);
        CAct=findViewById(R.id.cardAct);


     NavMove();
     homeChooseCard();
    }

    private void homeChooseCard() {
        CRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeTouriste.this,Liste_Restaurants.class);
                startActivity(intent);
            }
        });
        CHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeTouriste.this,Liste_Hotels.class);
                startActivity(intent);
            }
        }); CDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeTouriste.this,Liste_Destinations.class);
                startActivity(intent);
            }
        }); CProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeTouriste.this,Liste_Produits.class);
                startActivity(intent);
            }
        }); CAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeTouriste.this,Liste_Activites.class);
                startActivity(intent);
            }
        });
    }


    private void NavMove() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.n_home);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }else{
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
            }
        });
    }

    private void logout(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage("Etes-vous sûr de vous déconnecter?")
                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeTouriste.this,Authentification.class));
                        finish();
                    }
                }).setNegativeButton("Non",null);
        AlertDialog alert=builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        logout();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Touriste touriste=(Touriste)getIntent().getSerializableExtra("Touriste");
        switch (menuItem.getItemId()){

            case R.id.n_home:break;
            case R.id.n_resto:startActivity(new Intent(HomeTouriste.this,Liste_Restaurants.class));break;
            case R.id.n_hotel:startActivity(new Intent(HomeTouriste.this,Liste_Hotels.class));break;
            case R.id.n_activit:startActivity(new Intent(HomeTouriste.this,Liste_Activites.class));break;
            case R.id.n_destn:startActivity(new Intent(HomeTouriste.this,Liste_Destinations.class));break;
            case R.id.n_produit:startActivity(new Intent(HomeTouriste.this,Liste_Produits.class));break;
            case R.id.n_prof:startActivity(new Intent(HomeTouriste.this,Profil_Torist.class)
            .putExtra("Touriste",touriste)
            );break;
            case R.id.n_log:logout();break;

        }
        return true;
    }

}
