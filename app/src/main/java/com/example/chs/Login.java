package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chs.data.login.DAOUser;
import com.example.chs.data.login.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = findViewById(R.id.emailadd);
        pass = findViewById(R.id.passad);
        btn = findViewById(R.id.logbtn);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");
    }
    public boolean validateEmail(String email){
        if(email == "")
            return false;
        Pattern p = Pattern.compile("^(.+)@(.+)$");
        Matcher m  = p.matcher(email);
        if(m.matches()){
            return true;
        }
        return false;
    }
    public boolean validatePassword(String pass){
        if(pass == ""){
            return false;
        }
        return false;
    }
    public boolean isUser(String email,String pass){
        if(!validateEmail(email) || !validatePassword(pass))
            return false;
        return true;
    }
    public void clickLogin(View view) {
        DAOUser daoUser = new DAOUser();
        User user = new User(email.getText().toString(), pass.getText().toString());

        daoUser.add(user).addOnSuccessListener(suc -> {
            Toast.makeText(this, "Succesfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        }).addOnFailureListener(fail -> {
            Toast.makeText(this, "Failed to add user " + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}