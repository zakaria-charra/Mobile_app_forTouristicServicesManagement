package com.example.essaouiraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_produit extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;

    ProgressDialog progress;
    private EditText nom,desc,adress,quantite,prix;
    private DatabaseReference database;
    ProgressBar progressBar;
    TextView alert;
    Button imgs,save;
    ArrayList<Uri> listImage=new ArrayList<Uri>();
    Map<Integer,String> photo=new HashMap();
    Uri image;
    ArrayList<String> urls=new ArrayList<String>();
    public int i=0;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_add_produit,container,false);
        database= FirebaseDatabase.getInstance().getReference("produits");
       // photo.put(0,"eee");
        progress=new ProgressDialog(Add_produit.this.getActivity());
        progressBar=v.findViewById(R.id.progress_horizontal);
        alert=v.findViewById(R.id.alert);
        nom=v.findViewById(R.id.nomP);
        desc=v.findViewById(R.id.DescP);
        quantite=v.findViewById(R.id.Quan);
        prix=v.findViewById(R.id.prixP);
        adress=v.findViewById(R.id.AdressP);
        save=v.findViewById(R.id.saveP);
        imgs=v.findViewById(R.id.images);
        imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomp,descp,quanp,prixp,adressp;
                nomp=nom.getText().toString();
                descp=desc.getText().toString();
                quanp=quantite.getText().toString();
                prixp=prix.getText().toString();
                adressp=adress.getText().toString();

                if(!validEmpty(nom,nomp) |!validEmpty(desc,descp) |!validEmpty(quantite,quanp) |!validEmpty(prix,prixp) |!validEmpty(adress,adressp) ){
                    progress.dismiss();
                    return;

                }else if (listImage.isEmpty()){
                    Toast.makeText(getContext(),"Selectionner au moins une image !!",Toast.LENGTH_LONG).show() ;
                    progress.dismiss();
                }else {
                progress.setMessage("sauvegarde en cours...");
                progress.show();
                alert.setText("Uploading... ");
               uploadImages();}

            }
        });



        return v;

}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK){
            if(data.getClipData()!=null){
                int size=data.getClipData().getItemCount();
                int img=0;
                while (img<size){
                    image=data.getClipData().getItemAt(img).getUri();
                    listImage.add(image);
                    img=img+1;

                }

                Toast.makeText(Add_produit.this.getActivity(),"vous avez selectionné plusieur images"+listImage.size(),Toast.LENGTH_SHORT).show();
                alert.setText("vous avez selectionné "+size+" images");
            }else if(data.getData()!=null){
                image=data.getData();
                listImage.add(image);
                Toast.makeText(Add_produit.this.getActivity(),"vous avez une image",Toast.LENGTH_SHORT).show();
                alert.setText("vous avez selectionné 1 images");

            }
        }
    }

    public  void uploadImages(){

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(listImage.size());
       final StorageReference reference=  FirebaseStorage.getInstance().getReference().child("ProduitImages");
       int cop=0;
       while (cop<listImage.size()){
            final Uri singlImg=listImage.get(cop);
            reference.child(getFileExtension(singlImg)+"."+System.currentTimeMillis()).putFile(singlImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                    urlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            urls.add(url);
                            progressBar.incrementProgressBy(1);
                            if(progressBar.getProgress()==progressBar.getMax()){
                                alert.setText("Uploaded successful");
                                alert.setTextColor(Color.GREEN);
                                SaveProduit();
                            }else{
                            alert.setText("Uploading... ");

                            }



                        }
                    });

                    Toast.makeText(Add_produit.this.getActivity(),"vous avez "+urls.size(),Toast.LENGTH_SHORT).show();

                }
            });
            cop=cop+1;

        }


    }



    public void SaveProduit(){


        String nomp,descp,quanp,prixp,adressp;
        nomp=nom.getText().toString();
        descp=desc.getText().toString();
        quanp=quantite.getText().toString();
        prixp=prix.getText().toString();
        adressp=adress.getText().toString();
        for (i=0;i<urls.size();i++){
            photo.put(i,urls.get(i));
        }



                Fabricant fab=(Fabricant)getActivity().getIntent().getSerializableExtra("Fabricant");
                String id=database.push().getKey();
                String date= Calendar.getInstance().getTime().toString();
                Produit produit=new Produit(id,fab.getId(),nomp,adressp,quanp,prixp,descp,date,urls);
                database.child(id).setValue(produit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.dismiss();
                        refresh();
                        success();
                        makeEmpty();

                    }
                });


        }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public Boolean validEmpty(EditText edit,String val){
        if(val.isEmpty()) { edit.setError("Remplir ce champ"); return false;
        }else{
            return true;}
    };
    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
        urls.clear();
        listImage.clear();
        progressBar.setProgress(0);

    }
    public void makeEmpty(){
        nom.getText().clear();
        desc.getText().clear();
        adress.getText().clear();
        prix.getText().clear();
        quantite.getText().clear();

    }
    private void success(){
      AlertDialog.Builder builder =new AlertDialog.Builder(this.getActivity());
      builder.setTitle("Success");
        builder.setMessage("Produit ajouté avec success !!");
        builder.setIcon(getResources().getDrawable(R.drawable.ic_done_all_black_24dp));
        AlertDialog alert=builder.create();
        alert.show();
    }

}
