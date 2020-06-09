package com.example.essaouiraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class messages extends Fragment {

    EditText email,message;
    Button send;
    ProgressDialog progressDialog;

    private DatabaseReference mDatabaseRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_messages,container,false);

        progressDialog =new ProgressDialog(this.getContext());

            message=v.findViewById(R.id.MessageA);
            email=v.findViewById(R.id.mailA);
            send=v.findViewById(R.id.sendM);

            final Fabricant fabricant=(Fabricant)getActivity().getIntent().getSerializableExtra("Fabricant");

            email.setText(fabricant.getEmail());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("message");
        progressDialog.setMessage("Envoi en cours...");
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validEmpty(email,email.getText().toString()) |!validEmpty(message,message.getText().toString())){
                        return;
                    }else {

                        progressDialog.show();
                        SendMessage(fabricant.getEmail(),message.getText().toString(),fabricant.getId());
                        message.getText().clear();
                    }

                }
            });


        return v;

    }

    public void SendMessage(String email,String message,String id){

        Calendar calendar=Calendar.getInstance();

        String date= DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        Message messag=new Message(email,message,date,id);
        String uploadId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(uploadId).setValue(messag).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(messages.this.getActivity(),"Message Envoyée avec success",Toast.LENGTH_LONG).show();

                    progressDialog.cancel();
                }else {
                    Toast.makeText(messages.this.getActivity(),"N'est pas envoyée",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            }
        });

    }

    public Boolean validEmpty(EditText edit,String val){
        if(val.isEmpty()) { edit.setError("Remplir ce champ"); return false;
        }else{
            return true;}
    };
}
