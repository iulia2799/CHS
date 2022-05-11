package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.login.DAOPrimarie;
import com.example.chs.data.login.DAOUser;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.User;
import com.example.chs.data.model.Alert;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private Button btn;
    private Switch swps;
    private EditText username;
    private ImageView bubble;
    private ImageView track;
    private TextView q;
    private EditText confirmaParola;
    private TextView confirmatext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        email = findViewById(R.id.emailaddsignup);
        username = findViewById(R.id.useradd);
        pass = findViewById(R.id.passads);
        btn = findViewById(R.id.signbtn);
        swps = (Switch) findViewById(R.id.swps);
        bubble = findViewById(R.id.bubble1);
        track = findViewById(R.id.bordertrack);
        q=findViewById(R.id.q2);
        confirmaParola = findViewById(R.id.passads2);
        confirmatext = findViewById(R.id.confirma);

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
    public boolean ConfirmedPass(String pass, String confirmedPass){
        if(!pass.equals(confirmedPass)){
            return false;
        }
        return true;
    }
    public void clickSignUp(View view){
        DAOUser daoUser = new DAOUser();
        List<Alert> alertList = new ArrayList<>();
        alertList.add(new Alert("0","Welcome to app"));
        User user = new User(email.getText().toString(),username.getText().toString(), pass.getText().toString(),alertList);
        if(!checkCred(user.getEmail(), user.getPassword())){
            Toast.makeText(this,"Email must be name@email.com and password must be at least 8 characters",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!ConfirmedPass(user.getPassword(),confirmaParola.getText().toString())){
            Toast.makeText(this,"Please write the same password !",Toast.LENGTH_SHORT).show();
            return;
        }
        if(swps.isChecked()){
            //Toast.makeText(this,"primariile vor veni in curand",Toast.LENGTH_SHORT).show();
            DAOPrimarie daoPrimarie = new DAOPrimarie();
            Primarie primarie = new Primarie(email.getText().toString(),pass.getText().toString(),username.getText().toString());
            daoPrimarie.add(primarie).addOnSuccessListener(suc -> {
                Toast.makeText(getApplicationContext(), "Succesfully added primarie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PrimarieDashboard.class);
                startActivity(intent);

            }).addOnFailureListener(fail -> {
                Toast.makeText(getApplicationContext(), "Failed to add user " + fail.getMessage(), Toast.LENGTH_SHORT).show();
            });
            return;

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