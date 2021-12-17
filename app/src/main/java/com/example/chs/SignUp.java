package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.chs.data.login.DAOUser;
import com.example.chs.data.login.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private Button btn;
    private Switch swps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        email = findViewById(R.id.emailaddsignup);
        pass = findViewById(R.id.passads);
        btn = findViewById(R.id.signbtn);
        swps = (Switch) findViewById(R.id.swps);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("message");
    }
    public boolean validateEmail(String email){
        if(email.equals(""))
            return false;
        Pattern p = Pattern.compile("^(.+)@(.+)$");
        Matcher m  = p.matcher(email);
        return m.matches();
    }
    public boolean validatePassword(String pass){
        if(pass.equals("") || pass.length()<8){
            return false;
        }
        return true;
    }
    public boolean checkCred(String email,String pass){
        if(!validateEmail(email) || !validatePassword(pass))
            return false;
        return true;
    }
    public void clickSignUp(View view){
        DAOUser daoUser = new DAOUser();
        User user = new User(email.getText().toString(), pass.getText().toString());
        if(!checkCred(user.getEmail(), user.getPassword())){
            Toast.makeText(this,"Email must be name@email.com and password must be at least 8 characters",Toast.LENGTH_SHORT).show();
            return;
            //go to reloaded dashboard layout
        }
        if(swps.isChecked()){
            Toast.makeText(this,"primariile vor veni in curand",Toast.LENGTH_SHORT).show();
            return;
            //go to reloaded dashboard layout
        }
        daoUser.add(user).addOnSuccessListener(suc -> {
            Toast.makeText(getApplicationContext(), "Succesfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        }).addOnFailureListener(fail -> {
            Toast.makeText(getApplicationContext(), "Failed to add user " + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}