package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.login.DAOUser;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    public UserLocalStorage userLocalStorage;
    public Primarie primarielog;
    public PrimarieLocalStorage primarieLocalStorage;
    private MaterialButton button;
    public TextView username;
    public TextView informatii;
    public EditText editinfo;
    private MaterialButton editareinfo;
    public User userlog;
    public TextView infotitle;
    private boolean editmode = false;
    private List<User> userList = new ArrayList<>();
    private List<Primarie> primarieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button = findViewById(R.id.options);
        editareinfo = findViewById(R.id.clickedit);
        informatii = findViewById(R.id.info);
        informatii.setText("Informatii");
        editinfo = findViewById(R.id.editare);
        editinfo.setVisibility(View.GONE);
        infotitle = findViewById(R.id.username2);
        username = findViewById(R.id.Username);
        username.setText("username");
    }
    private boolean auth(){
        return this.userLocalStorage.getUserLoggedIn();
    }
    protected void displayUser(){
        User user = this.userLocalStorage.getLoggedInUser();
        Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
    }
    private boolean authp(){return this.primarieLocalStorage.getUserLoggedIn();}
    protected void display(){
        Primarie primarie = this.primarieLocalStorage.getLoggedInUser();
    }

    protected void getUserInfo(){
        userlog = this.userLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    if(mUser.getEmail().equals(userlog.getEmail())){
                        informatii.setText(mUser.getInformatii());
                        username.setText(mUser.getUsername());
                    }else{
                        Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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

    protected void getIns(){
        primarielog = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                primarieList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    if(mUser.getEmail().equals(userlog.getEmail())){
                        informatii.setText(mUser.getInformatii());
                        username.setText(mUser.getPrimarie());
                    }else{
                        Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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

    public void editUserInfo(View view){
        if(!editmode) {
            informatii.setVisibility(View.GONE);
            editinfo.setVisibility(View.VISIBLE);
            editinfo.setText(informatii.getText().toString());
            editmode=true;
        }else{
            userlog.setInformatii(editinfo.getText().toString());
            editmode = false;
            DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    for(DataSnapshot usersnapshot : snapshot.getChildren()){
                        User mUser = usersnapshot.getValue(User.class);
                        if(mUser.getEmail().equals(userlog.getEmail())){
                            usersnapshot.child("informatii").getRef().setValue(userlog.getInformatii());
                        }else{
                            Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                        }
                        userList.add(mUser);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("The read failed: "+error.getCode());
                }
            });
            informatii.setVisibility(View.VISIBLE);
            editinfo.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(auth()){
            displayUser();
            getUserInfo();
        }else if(authp()){
            display();
            getIns();
        }
    }
}