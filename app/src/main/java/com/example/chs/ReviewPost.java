package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.example.chs.data.model.Alert;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReviewPost extends AppCompatActivity {
    private TextView curs;
    private TextView voturi;
    private Button like;
    private Button dislike;
    private Button raporteaza;
    private TextView post;
    private TextView user;
    private TextView location;
    private TextView description;
    private ImageView imageView;
    private ImageView incurs;
    private TextView resolution;
    private Button button;
    private Button preia;
    private UserLocalStorage userLocalStorage;
    private PrimarieLocalStorage primarieLocalStorage;
    private String status;
    private Intent i;
    private TextView display_tracking;
    private MaterialButton feedback;
    private int voturi_i;
    private String trackingnumber;
    private String categorie;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private Categorie[] categories = {
            new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("test")
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewpost);

        curs = findViewById(R.id.curs);
        voturi = findViewById(R.id.points);
        like = findViewById(R.id.postr4);
        dislike = findViewById(R.id.postr5);
        raporteaza = findViewById(R.id.postr3);
        incurs = findViewById(R.id.imageView3);
        resolution = findViewById(R.id.resolution);
        post = findViewById(R.id.namep);
        user = findViewById(R.id.userp);
        location = findViewById(R.id.locationp);
        description = findViewById(R.id.descp);
        imageView = findViewById(R.id.imagep);
        button = findViewById(R.id.postr);
        preia = findViewById(R.id.postr2);
        display_tracking = findViewById(R.id.trakid);
        feedback = findViewById(R.id.postr6);

        i = getIntent();
        i.getExtras();
        post.setText(getIntent().getStringExtra("namep"));
        location.setText(getIntent().getStringExtra("locationp"));
        trackingnumber = getIntent().getStringExtra("trackingnumber");
        display_tracking.setText(trackingnumber);
        description.setText(getIntent().getStringExtra("descp"));
        user.setText(getIntent().getStringExtra("post_op"));
        String image = getIntent().getStringExtra("post_image");
        status = getIntent().getStringExtra("status");
        voturi_i = getIntent().getIntExtra("voturi",0);
        categorie = getIntent().getStringExtra("categorie");
        String result = "Voturi: "+String.valueOf(voturi_i);
        voturi.setText(result);
        //Toast.makeText(this,image.,Toast.LENGTH_SHORT).show();
        primarieLocalStorage = new PrimarieLocalStorage(this);
        userLocalStorage = new UserLocalStorage(this);
        resolution.setText(status);
        System.out.println(trackingnumber);
        LoadImageFromWebOperations(image);
        getStatus();
        if(status.startsWith("In curs")){
            preia.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(authenticate()){
            button.setEnabled(false);
            preia.setEnabled(false);
            button.setVisibility(View.GONE);
            preia.setVisibility(View.GONE);
        }else{
            if(authenticatep()){
                button.setEnabled(true);
                preia.setEnabled(true);
                //button.setVisibility(View.VISIBLE);
               // preia.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getStatus(){
        if(status.startsWith("SOLVED") || status.startsWith("Rezolvat")){
            raporteaza.setVisibility(View.GONE);
            feedback.setVisibility(View.VISIBLE);
            preia.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            like.setVisibility(View.GONE);
            dislike.setVisibility(View.GONE);
            //voturi.setVisibility(View.GONE);
            if(authenticate()){
                resolution.setVisibility(View.VISIBLE);
                incurs.setVisibility(View.GONE);
                curs.setVisibility(View.GONE);
            }else if(authenticatep()){
                feedback.setVisibility(View.GONE);
                resolution.setVisibility(View.VISIBLE);
                incurs.setVisibility(View.GONE);
                curs.setVisibility(View.GONE);
            }
        }else if(status.startsWith("NOT SOLVED") || status.startsWith("Nerezolvat")){
            System.out.println(status);
            resolution.setVisibility(View.VISIBLE);
            preia.setVisibility(View.GONE);
            feedback.setVisibility(View.GONE);
            incurs.setVisibility(View.GONE);
            curs.setVisibility(View.GONE);


        }else if(status.startsWith("In curs")){
            resolution.setVisibility(View.GONE);
            feedback.setVisibility(View.GONE);
            incurs.setVisibility(View.VISIBLE);
            curs.setVisibility(View.VISIBLE);
            if(authenticatep()){
                raporteaza.setVisibility(View.GONE);
                preia.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                dislike.setVisibility(View.GONE);
                return;
            }

        }else{
            curs.setVisibility(View.GONE);
            incurs.setVisibility(View.GONE);
            feedback.setVisibility(View.GONE);
            resolution.setVisibility(View.GONE);
            if(authenticatep()){
                raporteaza.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                dislike.setVisibility(View.GONE);
                preia.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        }

    }
    public void clickLike(View view){
        votePost(1);
    }

    private void votePost(int i) {
        DatabaseReference ref = database.getReference(categorie);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    Post mPost= usersnapshot.getValue(Post.class);
                    assert mPost !=null;
                    /*User mUser = usersnapshot.child("op").getValue(User.class);
                    assert mPost != null;
                    if(mPost.getName().contentEquals(post.getText().toString())) {
                        assert mUser != null;
                        System.out.println(mUser.getUsername());
                        System.out.println(user.getText().toString());
                        if (mUser.getUsername().equals(user.getText().toString())) {
                            usersnapshot.child("voturi").getRef().setValue(mPost.getVoturi() + i);
                            break;
                        }
                    }

                     */
                    if(usersnapshot.getKey().equals(trackingnumber)){
                        usersnapshot.child("voturi").getRef().setValue(mPost.getVoturi()+i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this,"Votul tau a fost inregistrat cu succes",Toast.LENGTH_SHORT).show();
    }

    public void clickPreia(View view){
        Primarie primarie;
        if(authenticatep()){
            primarie = primarieLocalStorage.getLoggedInUser();
            //findPost();
        }else{

        }
        Intent intent = new Intent(this,Preia.class);
        intent.putExtra("trackingnumber",trackingnumber);
        intent.putExtra("categorie",categorie);
        intent.putExtra("username",user.getText().toString());
        startActivity(intent);

    }
    public void clickDislike(View view){
        votePost(-1);
    }
    public void clickReport(View view){
        spamPost();
    }

    public void penalizeUser(){
        DatabaseReference ref = database.getReference("User");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    if(mUser.getUsername().equals(user.getText().toString())){
                        usersnapshot.child("points").getRef().setValue(mUser.getPoints()-10);
                        Toast.makeText(getApplicationContext(),"Cel care a postat va fi penalizat",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    snapshot.child(trackingnumber).child("status").getRef().setValue("In curs");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void spamPost(){
        DatabaseReference ref = database.getReference(categorie);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(trackingnumber).exists()){
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    Integer spam = snapshot.child(trackingnumber).child("spam").getValue(Integer.class);
                    List<String> reporters = new ArrayList<>();
                    for( DataSnapshot s : snapshot.child(trackingnumber).child("reporters").getChildren()) {
                        reporters.add(s.getValue(String.class));
                    }
                    if (authenticate()) {
                        User reporter = userLocalStorage.getLoggedInUser();
                        if (reporters.contains(reporter.getEmail())) {
                            Toast.makeText(getApplicationContext(),"Ati raportat deja aceasta problema!",Toast.LENGTH_SHORT).show();
                        } else {
                            snapshot.child(trackingnumber).child("spam").getRef().setValue(spam+1);
                            String email = reporter.getEmail();
                            reporters.add(email);
                            DatabaseReference rootref = snapshot.child(trackingnumber).getRef();
                            DatabaseReference arr = rootref.child("reporters");
                            System.out.println(arr.getKey());
                            arr.setValue(reporters);
                            penalizeUser();
                            if(spam+1>=10){
                                sendSpamEmail();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendSpamEmail(){
        try {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.setType("plain/text");

            send.putExtra(Intent.EXTRA_EMAIL, new String[]{"iuliasarah27@gmail.com"});
            send.putExtra(Intent.EXTRA_SUBJECT, "Cazul #" + trackingnumber+" este Spam!");
            //if (uri != null) {
            ////    send.putExtra(Intent.EXTRA_STREAM, uri);
            //}
            send.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.putExtra(Intent.EXTRA_TEXT, "Cazul #" + trackingnumber + " a fost marcat ca Spam/abuziv de 10 de utilizatori. Poti scrie aici motivul");
            startActivity(send);
            System.out.println("fdsfdsfsdsdf");
            findPost();
        }catch (Throwable t){
            System.out.println(t.toString());
            Toast.makeText(this,"Sending email has failed, email may be invalid "+t.toString(),Toast.LENGTH_LONG).show();
        }

    }
    public void clickSolve(View view){
         Intent ii = new Intent(this,Solve.class);
         ii.putExtra("username",user.getText().toString());
         DatabaseReference ref = database.getReference(categorie);
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(trackingnumber).exists()){
                    ii.putExtra("names",snapshot.child(trackingnumber).child("name").getValue(String.class));
                    Primarie primarie = primarieLocalStorage.getLoggedInUser();
                    Primarie t1 = snapshot.child(trackingnumber).child("assignee").getValue(Primarie.class);
                    if(t1.getEmail().equals(primarie.getEmail())){
                        System.out.println(t1.getEmail());
                        System.out.println(primarie.getEmail());
                        ii.putExtra("tr",trackingnumber);
                        startActivity(ii);
                    }
                       //startActivity(ii);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
         });
         //startActivity(ii);
    }
    private boolean authenticate(){
        return userLocalStorage.getUserLoggedIn();
    }
    private boolean authenticatep(){
        return primarieLocalStorage.getUserLoggedIn();
    }
    public void LoadImageFromWebOperations(String url) {
        /*try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, url);
            return d;
        } catch (Exception e) {
            return null;
        }*/
        //System.out.println(url);
        Glide.with(this).load(url).into(imageView);
    }

    public void clickFeedback(View view){
        User user = null;
        if (authenticate()) {
            user = userLocalStorage.getLoggedInUser();
        }
        if(status.startsWith("Nerezolvat")){
            Toast.makeText(this,"Aceasta postare a fost marcata deja ca nerezolvata",Toast.LENGTH_SHORT).show();
            return;
        }
        if (user!=null && user.getUsername().equals(this.user.getText().toString())) {
            System.out.println("here");
            DatabaseReference ref = database.getReference(categorie);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(trackingnumber).exists()) {
                        if (snapshot.child(trackingnumber).child("status").exists()) {
                            DataSnapshot ds = snapshot.child(trackingnumber);
                            String[] status = ds.child("status").getValue(String.class).split(":");
                            snapshot.child(trackingnumber).child("datet").getRef().setValue(System.currentTimeMillis());
                            ds.child("status").getRef().setValue("Nerezolvat - cetateanul este nemultumit: " + status[1]);
                            Primarie primarie = snapshot.child(trackingnumber).child("assignee").getValue(Primarie.class);
                            RetractPoints(primarie);

                        }
                    } else System.out.println("error");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void ViewUserProfile(View view){
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("username",user.getText().toString());
        intent.putExtra("type","user");
        startActivity(intent);
    }

    public void ViewPrimarieProfile(View view) {
        Intent intent = new Intent(this,Profile.class);
        DatabaseReference ref =database.getReference(categorie);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(trackingnumber).exists()) {
                    String username = snapshot.child(trackingnumber).child("assignee").child("primarie").getValue(String.class);
                    System.out.println("toString"+snapshot.child(trackingnumber).toString().length());
                    intent.putExtra("username",username);
                    intent.putExtra("type","primarie");
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ViewRez(View view) {
        Intent intent = new Intent(this,Profile.class);
        DatabaseReference ref =database.getReference(categorie);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(trackingnumber).exists()) {
                    String username = snapshot.child(trackingnumber).child("assignee").child("primarie").getValue(String.class);
                    System.out.println(username);
                    intent.putExtra("username",username);
                    intent.putExtra("type","primarie");
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void RetractPoints(Primarie primarie){
        List<Alert> list = new ArrayList<>();
        DatabaseReference ref = database.getReference("Primarie");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    Primarie mUser = usersnapshot.getValue(Primarie.class);
                    System.out.println(mUser.getPrimarie() + "," + primarie.getPrimarie());
                    if(mUser.getPrimarie().equals(primarie.getPrimarie())){
                        usersnapshot.child("points").getRef().setValue(mUser.getPoints()-10);
                        Alert mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Cazul #"+trackingnumber+"a fost marcat ca nerezolvat : cetatean nemultumit!");
                        DataSnapshot ref = usersnapshot.child("alertList");
                        for(DataSnapshot reference : ref.getChildren()){
                            list.add(reference.getValue(Alert.class));
                        }
                        list.add(mAlert);

                        DatabaseReference rootref = usersnapshot.getRef();
                        DatabaseReference arr = rootref.child("alertList");
                        System.out.println(arr.getKey());
                        arr.setValue(list);
                        Toast.makeText(getApplicationContext(),"Cel care a rezolvat va fi penalizat",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}