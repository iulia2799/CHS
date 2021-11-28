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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<User> userList = new ArrayList<>();
    public void checkUser(String email,String pass){

        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                DAOUser daoUser = new DAOUser();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    if(mUser.getEmail().equals(email) && mUser.getPassword().equals(pass)){
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                    }else{
                        daoUser.add(mUser).addOnSuccessListener(suc -> {
                            Toast.makeText(getApplicationContext(), "Succesfully added user", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(intent);

                        }).addOnFailureListener(fail -> {
                            Toast.makeText(getApplicationContext(), "Failed to add user " + fail.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    userList.add(mUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }
    public void clickLogin(View view) {

        User user = new User(email.getText().toString(), pass.getText().toString());
        if(checkCred(user.getEmail(),user.getPassword())) {
            checkUser(user.getEmail(),user.getPassword());
        }else{
            Toast.makeText(this,"Email must be name@email.com and password must be at least 8 characters",Toast.LENGTH_SHORT).show();
        }
    }
}