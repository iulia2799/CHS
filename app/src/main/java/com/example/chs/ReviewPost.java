package com.example.chs;

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
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;

import java.io.InputStream;
import java.net.URL;

public class ReviewPost extends AppCompatActivity {
    private TextView title;
    private TextView post;
    private TextView user;
    private TextView location;
    private TextView description;
    private ImageView imageView;
    private Button button;
    private UserLocalStorage userLocalStorage;
    private Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewpost);

        title = findViewById(R.id.solvepost);
        post = findViewById(R.id.namep);
        user = findViewById(R.id.userp);
        location = findViewById(R.id.locationp);
        description = findViewById(R.id.descp);
        imageView = findViewById(R.id.imagep);
        button = findViewById(R.id.postr);


        i = getIntent();
        i.getExtras();
        post.setText(getIntent().getStringExtra("namep"));
        location.setText(getIntent().getStringExtra("locationp"));
        description.setText(getIntent().getStringExtra("descp"));
        user.setText(getIntent().getStringExtra("post_op"));
        String image = getIntent().getStringExtra("post_image");
        LoadImageFromWebOperations(image);

    }
    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        if(authenticate()){
            button.setEnabled(false);
        }
    }
    public void clickSolve(View view){
         Intent i = new Intent(this,Solve.class);
         i.putExtra("names",post.getText().toString());
         i.putExtra("locations",location.getText().toString());
         i.putExtra("descs",description.getText().toString());
         i.putExtra("post_ops",user.getText().toString());
         //image is not necessary in this case
         startActivity(i);
    }
    private boolean authenticate(){
        return userLocalStorage.getUserLoggedIn();
    }
    public void LoadImageFromWebOperations(String url) {
        /*try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, url);
            return d;
        } catch (Exception e) {
            return null;
        }*/
        Glide.with(this).load(url).into(imageView);
    }
}