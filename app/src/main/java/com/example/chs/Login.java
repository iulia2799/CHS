package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.login.DAOUser;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
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
    private Switch swp;
    private UserLocalStorage userLocalStorage;
    private PrimarieLocalStorage primarieLocalStorage;
    private TextView question;
    private ImageView bubble;
    private ImageView bubble2;
    private ImageView track;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = findViewById(R.id.emailadd);
        pass = findViewById(R.id.passad);
        btn = findViewById(R.id.logbtn);
        swp = findViewById(R.id.swp);
        question = findViewById(R.id.intrebare);
        bubble = findViewById(R.id.bubble2);
        bubble2 = findViewById(R.id.bubble3);
        track = findViewById(R.id.bordertrackl);
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
    private List<Primarie> primarieList = new ArrayList<>();
    public void checkUser(String email,String pass){

        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    User xUser = new User(mUser.getEmail(),mUser.getUsername(),mUser.getPassword());
                    if(mUser.getEmail().equals(email) && mUser.getPassword().equals(pass)){
                        userLocalStorage.storeUserData(xUser);
                        userLocalStorage.setUserLoggedIn(true);
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                    }else{

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
    public void checkPrimarie(String email,String pass){

        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                primarieList.clear();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    Primarie mUser = usersnapshot.getValue(Primarie.class);
                    Primarie xUser = new Primarie(mUser.getEmail(),mUser.getPrimarie(),mUser.getLocation(),mUser.getPassword());
                    if(mUser.getEmail().equals(email) && mUser.getPassword().equals(pass)){
                        primarieLocalStorage.storeUserData(xUser);
                        primarieLocalStorage.setUserLoggedIn(true);
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        intent.putExtra("emailp",email);
                        intent.putExtra("numep","nume primarie aici");
                        startActivity(intent);
                    }else{

                    }
                    primarieList.add(mUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }
    public void clickLogin(View view) {
        String storedEmail = email.getText().toString();
        String storedpass = pass.getText().toString();
        //userLocalStorage = new UserLocalStorage(this);

        if(!checkCred(storedEmail,storedpass)){
            Toast.makeText(this,"Email must be name@email.com and password must be at least 8 characters",Toast.LENGTH_SHORT).show();

        }
        else if(!swp.isChecked()) {
            userLocalStorage = new UserLocalStorage(this);
            User user = new User(storedEmail, storedpass);
            checkUser(user.getEmail(),user.getPassword());



        }
        else if(swp.isChecked()){
            primarieLocalStorage = new PrimarieLocalStorage(this);
            Primarie primarie = new Primarie(storedEmail,storedpass);
            checkPrimarie(primarie.getEmail(), primarie.getPassword());



        }else{
            Toast.makeText(this,"unknowm error occured",Toast.LENGTH_SHORT).show();
        }
    }
}