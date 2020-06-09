package com.example.essaouiraapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    private Button btnAuth;
    private Button btnfab;
    private Button btnTrst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        btnAuth=(Button) findViewById(R.id.btnLog);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAuth();
            }
        });

        btnfab=findViewById(R.id.btnFab);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationFabricant();
            }
        });

        btnTrst=findViewById(R.id.btnTourist);
        btnTrst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationTouriste();
            }
        });
    }

    private void openAuth(){
        Intent intent = new Intent(this,Authentification.class);
        startActivity(intent);
    }
    private void openRegistrationFabricant(){
        Intent intent = new Intent(this,RegistrationFabricant.class);
        startActivity(intent);
    }
    private void openRegistrationTouriste(){
        Intent intent = new Intent(this,RegistrationTourist.class);
        startActivity(intent);
    }


    }

