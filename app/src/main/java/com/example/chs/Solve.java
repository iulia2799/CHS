package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chs.data.Categorie;
import com.example.chs.data.DAOPost;
import com.example.chs.data.Post;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.model.Alert;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solve extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private TextView desc;
    private Switch aSwitch;
    private Post post;
    private PrimarieLocalStorage primarie;
    private String p;
    private Post mPost;
    private Intent i;
    private ImageView swin;
    private Categorie[] categories = {
            new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("curatenie"),
            new Categorie("altele")
    };
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private String trackingnumber = "mo";
    private String finalUsername = "anonim";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        button = findViewById(R.id.resp);
        textView = findViewById(R.id.solve);
        desc = findViewById(R.id.response);
        aSwitch = findViewById(R.id.couldnotsolve);
        swin = findViewById(R.id.swin);
        trackingnumber = getIntent().getStringExtra("tr");
        System.out.println(trackingnumber);
    }

    private boolean authenticate(){
        primarie = new PrimarieLocalStorage(this);
        if(primarie.getLoggedInUser() == null) return false;
        return primarie.getUserLoggedIn();
    }

    public void OnClickSolve(View view){
        i = getIntent();
        i.getExtras();
        if(authenticate()){
            p = primarie.getLoggedInUser().getEmail();
        }
        String postname= i.getStringExtra("names");
        FirebaseDatabase database =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        for(Categorie cat : categories){
            DatabaseReference ref = database.getReference(cat.getNume());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("HERE");
                    if(snapshot.child(trackingnumber).exists()) {
                        DataSnapshot dataSnapshot = snapshot.child(trackingnumber);
                        mPost = dataSnapshot.getValue(Post.class);
                        assert mPost != null;
                        System.out.println("HERE 1");
                        if (postname.equals(mPost.getName())) {
                            if (!aSwitch.isChecked()) {
                                dataSnapshot.child("status").getRef().setValue("Rezolvat de  : " + p + "-> " + desc.getText().toString());
                                putNotification(mPost);
                                Toast.makeText(getApplicationContext(), "Actualizat cu succes!", Toast.LENGTH_SHORT).show();
                            } else {
                                dataSnapshot.child("status").getRef().setValue("Nerezolvat de : " + p + "-> " + desc.getText().toString());
                                putNotification(mPost);
                                Toast.makeText(getApplicationContext(), "Actualizat cu succes!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        System.out.println("err");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void putNotification(Post post){
        Intent intent = getIntent();
        String username1 = "anonim";
        if(intent.hasExtra("username")) username1 = intent.getStringExtra("username");
        if(intent.hasExtra("tr")) trackingnumber= intent.getStringExtra("tr");
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        List<Alert> list = new ArrayList<>();
        finalUsername = username1;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot u : snapshot.getChildren()){
                    User curr = u.getValue(User.class);
                    assert curr != null;
                    if(curr.getUsername().equals(finalUsername)){
                        Alert mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Cazul #"+ trackingnumber +" a fost rezolvat cu succes. "+desc.getText().toString());
                        DataSnapshot ref = u.child("alertList");
                        for(DataSnapshot reference : ref.getChildren()){
                            list.add(reference.getValue(Alert.class));
                        }
                        list.add(mAlert);

                        DatabaseReference rootref = u.getRef();
                        DatabaseReference arr = rootref.child("alertList");
                        Map<String,Object> map = new HashMap<>();
                        System.out.println(arr.getKey());
                        arr.setValue(list);
                        sendEmail(curr.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void sendEmail(String email){

        try {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.setType("plain/text");
            send.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            send.putExtra(Intent.EXTRA_SUBJECT, "Cazul #" + trackingnumber);
            //if (uri != null) {
            ////    send.putExtra(Intent.EXTRA_STREAM, uri);
            //}
            //send.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.putExtra(Intent.EXTRA_TEXT, "Cazul #" + trackingnumber + " a fost rezolvat de " + primarie.getLoggedInUser().getEmail() + " . "+desc.getText().toString());
            startActivity(Intent.createChooser(send,"Sending mail"));
            System.out.println("fdsfdsfsdsdf");
            GivePoints();
        }catch (Throwable t){
            System.out.println(t.toString());
            Toast.makeText(this,"Sending email has failed, email may be invalid "+t.toString(),Toast.LENGTH_LONG).show();
        }

    }
    public void GivePoints(){
        DatabaseReference ref = firebaseDatabase.getReference("Primarie");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie x = dataSnapshot.getValue(Primarie.class);
                    assert x != null;
                    if(x.getEmail().equals(primarie.getLoggedInUser().getEmail())){
                        dataSnapshot.child("points").getRef().setValue(x.getPoints()+30);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}