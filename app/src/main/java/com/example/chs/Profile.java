package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.material.button.MaterialButton;

public class Profile extends AppCompatActivity {
    private UserLocalStorage userLocalStorage;
    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = findViewById(R.id.button6);
        setContentView(R.layout.activity_profile);
    }
    private boolean auth(){
        return this.userLocalStorage.getUserLoggedIn();
    }
    protected void displayUser(){
        User user = this.userLocalStorage.getLoggedInUser();
        Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        if(auth()){
            displayUser();
        }
    }
}