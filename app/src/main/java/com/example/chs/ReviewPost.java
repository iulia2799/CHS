package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chs.data.Post;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
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
    private int voturi_i;
    private String categorie;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");




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


        i = getIntent();
        i.getExtras();
        post.setText(getIntent().getStringExtra("namep"));
        location.setText(getIntent().getStringExtra("locationp"));
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
        getStatus();
        findPost();
        //LoadImageFromWebOperations(image);


    }
    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        if(authenticate()){
            button.setEnabled(false);
            preia.setEnabled(false);
            button.setVisibility(View.GONE);
            preia.setVisibility(View.GONE);
        }

    }

    public void getStatus(){
        if(status.startsWith("SOLVED") || status.startsWith("Rezolvat")){
            raporteaza.setVisibility(View.GONE);
            like.setVisibility(View.GONE);
            dislike.setVisibility(View.GONE);
            voturi.setVisibility(View.GONE);
            if(authenticate()){
                resolution.setVisibility(View.VISIBLE);
                incurs.setVisibility(View.GONE);
                curs.setVisibility(View.GONE);
            }
        }else if(status.startsWith("NOT SOLVED") || status.startsWith("Nerezolvat")){
            resolution.setVisibility(View.VISIBLE);
            incurs.setVisibility(View.GONE);
            curs.setVisibility(View.GONE);


        }else if(status.startsWith("In curs")){
            resolution.setVisibility(View.GONE);
            incurs.setVisibility(View.VISIBLE);
            curs.setVisibility(View.VISIBLE);
            if(authenticate()){

            }

        }else{
            curs.setVisibility(View.GONE);
            incurs.setVisibility(View.GONE);
            resolution.setVisibility(View.GONE);
            if(authenticatep()){
                raporteaza.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                dislike.setVisibility(View.GONE);
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
                    User mUser = usersnapshot.child("op").getValue(User.class);
                    assert mPost != null;
                    if(mPost.getName().contentEquals(post.getText().toString()) && mUser.getUsername().equals(user.getText().toString())){
                        usersnapshot.child("voturi").getRef().setValue(mPost.getVoturi()+i);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clickPreia(View view){

    }
    public void clickDislike(View view){
        votePost(-1);
    }
    public void clickReport(View view){
        penalizeUser();
        findPost();
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
                        Toast.makeText(getApplicationContext(),"User will be penalized",Toast.LENGTH_SHORT).show();
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
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    Post mPost= usersnapshot.getValue(Post.class);
                    User mUser = usersnapshot.child("op").getValue(User.class);
                    if(mPost.getName().equals(post.getText())){
                        //usersnapshot.child("spam").getRef().setValue(mPost.getSpam()+1);
                        LoadImageFromWebOperations(usersnapshot.child("images").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void clickSolve(View view){
         Intent ii = new Intent(this,Solve.class);
         ii.putExtra("names",post.getText().toString());
         ii.putExtra("locations",location.getText().toString());
         ii.putExtra("descs",description.getText().toString());
         ii.putExtra("post_ops",user.getText().toString());
         ii.putExtra("cat",i.getStringExtra("cat"));
         //image is not necessary in this case
         startActivity(ii);
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
}