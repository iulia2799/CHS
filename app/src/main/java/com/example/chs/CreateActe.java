package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.chs.data.ActAdapter;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.model.Act;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateActe extends AppCompatActivity {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://proiect-chs.appspot.com");
    private PrimarieLocalStorage primarieLocalStorage;
    private Primarie log;
    private MaterialButton adauga;
    private RecyclerView lista;
    private String path;
    private Act adaugat = null;
    private String deletedAttachment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acte);
        adauga = findViewById(R.id.addacte);
        lista = findViewById(R.id.dynamic);
    }

    @Override
    protected void onStart() {
        super.onStart();
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(primarieLocalStorage.getUserLoggedIn()){
            log= primarieLocalStorage.getLoggedInUser();
            find();
        }else
            log= null;
    }

    public void find(){
        List<Act> acte  = new ArrayList<>();
        log = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getEmail().equals(log.getEmail())){
                        if(dataSnapshot.child("links").exists()){
                            for(DataSnapshot link : dataSnapshot.child("links").getChildren()){
                                  Act act = link.getValue(Act.class);
                                  acte.add(act);
                            }
                        }
                    }else{
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                    }

                }
                ActAdapter adapter = new ActAdapter(acte, position -> {
                    String l=  acte.get(position).getLink();
                    String name = acte.get(position).getNume();
                    System.out.println(l);
                    DeleteAttachment(acte.get(position));
                });
                lista.setHasFixedSize(true);
                lista.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                lista.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }

    public void DeleteAttachment(Act act){
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(act.getLink());
        storageRef.delete().addOnFailureListener(fail ->{
            System.out.println(fail.getMessage());
        }).addOnSuccessListener(unused -> {
            deletedAttachment = act.getLink();
            AddAttachment();
        });
    }

    public void Adaugare(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        startActivityForResult(Intent.createChooser(intent,"Add attachment"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());//create path from uri
                    final String[] split = file.getPath().split(":");//split the path.
                    path = split[1];//assign it to a string(your choice).
                    sendToFirebase(path);
                    //String firebasepath = sendToFirebase();
                    //adaugat = new Act(uri.getLastPathSegment(),sendToFirebase());
                    //Uri FilePath = data.getData();
                    //path = FilePath.getEncodedPath();
                }
                break;

        }
    }


    public String sendToFirebase(String path){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),path);
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(stream !=null){
            StorageReference ref = storageReference.child("links/"+String.valueOf(System.currentTimeMillis()));
            final String[] files = {""};
            UploadTask uploadTask = ref.putStream(stream);
            uploadTask.addOnFailureListener(fail ->{
                System.out.println(fail.getMessage());
                Toast.makeText(this,"Upload failed",Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener( uri1 -> {
                        files[0] = uri1.toString();
                        adaugat = new Act(file.getName(),files[0]);
                        AddAttachment();
                    }).addOnFailureListener(fail -> {
                        System.out.println(fail.getMessage());
                    });
                }
            });
        }

        return null;
    }

    public void AddAttachment(){
        List<Act> acte  = new ArrayList<>();
        log = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getEmail().equals(log.getEmail())){
                        if(dataSnapshot.child("links").exists()){
                            for(DataSnapshot link : dataSnapshot.child("links").getChildren()){
                                Act act = link.getValue(Act.class);
                                if(act.getLink().equals(deletedAttachment)) {
                                    continue;
                                }else {
                                    acte.add(act);
                                }
                            }
                            if(deletedAttachment==null){
                                acte.add(adaugat);
                            }
                            DatabaseReference rootref = dataSnapshot.getRef();
                            DatabaseReference arr = rootref.child("links");
                            System.out.println(arr.getKey());
                            arr.setValue(acte);
                        }
                    }else{
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }
}