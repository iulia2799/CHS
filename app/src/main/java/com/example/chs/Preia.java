
package com.example.chs;

import static android.content.Intent.ACTION_SEND;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.TestLooperManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.login.DAONotification;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.model.Act;
import com.example.chs.data.model.Alert;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preia extends AppCompatActivity {
    private String trackingnumber;
    private String categorie;
    private EditText description;
    private MaterialButton adauga;
    private MaterialButton confirma;
    private TextView numefisier;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://proiect-chs.appspot.com");
    private String path;
    private String username = "";
    private PrimarieLocalStorage primarieLocalStorage;
    private Primarie primarie;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preia);
        description = findViewById(R.id.preia_desc);
        adauga = findViewById(R.id.adauga);
        confirma = findViewById(R.id.conf);
        numefisier = findViewById(R.id.numef);
        numefisier.setText("Nimic adaugat");
        Intent i;
        i=getIntent();
        i.getExtras();
        username = getIntent().getStringExtra("username");
        trackingnumber = getIntent().getStringExtra("trackingnumber");
        categorie = getIntent().getStringExtra("categorie");


    }

    @Override
    protected void onStart() {
        super.onStart();
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(auth()){
            primarie = primarieLocalStorage.getLoggedInUser();
        }

    }

    public boolean auth(){
        return primarieLocalStorage.getUserLoggedIn();
    }

    public void findPost(){
        DatabaseReference ref = database.getReference(categorie);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(trackingnumber).exists()){
                    Primarie primarie = primarieLocalStorage.getLoggedInUser();
                    primarie.setPassword("********");
                    snapshot.child(trackingnumber).child("assignee").getRef().setValue(primarie);

                    snapshot.child(trackingnumber).child("datet").getRef().setValue(System.currentTimeMillis());
                    snapshot.child(trackingnumber).child("status").getRef().setValue("In curs:"+description.getText().toString());
                    Toast.makeText(getApplicationContext(),"Preluat cu succes",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void GivePoints(){
        DatabaseReference ref = database.getReference("Primarie");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie x = dataSnapshot.getValue(Primarie.class);
                    if(x.getEmail().equals(primarie.getEmail())){
                        dataSnapshot.child("points").getRef().setValue(x.getPoints()+20);
                        Toast.makeText(getApplicationContext(),"Preluat cu succes",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClick(View view){
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
                    //Uri FilePath = data.getData();
                    //path = FilePath.getEncodedPath();
                    numefisier.setText(path);
                }
                break;

        }
    }

    public void findUser(String filer){
        if(username.length()>0 && !username.contains("anonim")){
            DatabaseReference ref = database.getReference("User");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot usersnap : snapshot.getChildren()){
                        User user = usersnap.getValue(User.class);
                        assert user != null;
                        List<Alert> list = new ArrayList<>();
                        Alert mAlert;
                        System.out.println(user.getUsername()+username);
                        if(user.getUsername().equals(username)){
                            if(!filer.equals("")){
                                GivePoints();
                                //String link = sendToFirebase();
                                //Toast.makeText(getApplicationContext(),filer,Toast.LENGTH_SHORT).show();
                                mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Cazul #"+trackingnumber+" a fost preluat de "+primarie.getPrimarie() + ". " + description.getText().toString(),filer);
                            }else
                                mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Cazul #"+trackingnumber+" a fost preluat de "+primarie.getPrimarie()+". " + description.getText().toString());
                                DataSnapshot ref = usersnap.child("alertList");
                                for(DataSnapshot reference : ref.getChildren()){
                                    list.add(reference.getValue(Alert.class));
                                }
                                list.add(mAlert);

                                DatabaseReference rootref = usersnap.getRef();
                                DatabaseReference arr = rootref.child("alertList");
                                Map<String,Object> map = new HashMap<>();
                                System.out.println(arr.getKey());
                                arr.setValue(list);

                            //}
                            if(!numefisier.getText().toString().equals("Nimic adaugat")) {
                                sendEmail(user.getEmail(),path);
                            }
                            else {
                                findPost();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            findPost();
        }

    }

    public void sendEmail(String email,String path){
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),path);
            Uri uri = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    file);
           Intent send = new Intent(Intent.ACTION_SEND);
            send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.setDataAndType(uri, getContentResolver().getType(uri));
            send.putExtra(Intent.EXTRA_STREAM, uri);

            send.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            send.putExtra(Intent.EXTRA_SUBJECT, "Cazul #" + trackingnumber);
            //if (uri != null) {
            ////    send.putExtra(Intent.EXTRA_STREAM, uri);
            //}
            send.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.putExtra(Intent.EXTRA_TEXT, "Cazul #" + trackingnumber + " a fost preluat de " + primarieLocalStorage.getLoggedInUser().getEmail() + " . "+description.getText().toString());
            System.out.println("fdsfdsfsdsdf");
            findPost();
        }catch (Throwable t){
            System.out.println(t.toString());
            Toast.makeText(this,"Sending email has failed, email may be invalid "+t.toString(),Toast.LENGTH_LONG).show();
        }
        //findPost();

    }

    public String sendToFirebase(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),path);
        InputStream stream = null;
        String[] files = {""};
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(stream !=null){
            StorageReference ref = storageReference.child("preiafiles/"+String.valueOf(System.currentTimeMillis()));
            UploadTask uploadTask = ref.putStream(stream);
            uploadTask.addOnFailureListener(fail ->{
                System.out.println(fail.getMessage());
                Toast.makeText(this,"Upload failed",Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener( uri1 -> {
                        files[0] = uri1.toString();
                        findUser(files[0]);
                    }).addOnFailureListener(fail -> {
                        System.out.println(fail.getMessage());
                    });
                }
            });
        }
        if(files.length >0) {
            return files[0];
        }

        return null;
    }


    public void clickConfirma(View view){
        if(!numefisier.getText().toString().equals("Nimic adaugat")) {
            sendToFirebase();
        }else
        findUser("");
        //findPost();
    }
}