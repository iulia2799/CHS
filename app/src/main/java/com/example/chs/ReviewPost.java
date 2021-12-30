package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;

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
         startActivity(i);
    }
    private boolean authenticate(){
        return userLocalStorage.getUserLoggedIn();
    }

}