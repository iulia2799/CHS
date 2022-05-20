package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.chs.data.Cities;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EnumSet;

public class Setari extends AppCompatActivity {

    private PrimarieLocalStorage primarieLocalStorage;
    private UserLocalStorage userLocalStorage;
    private MaterialButton changePassword;
    private Spinner spinner;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference primarieref = database.getReference("Primarie");
    private DatabaseReference userref = database.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari);

        spinner = findViewById(R.id.locations);
        changePassword = findViewById(R.id.button7);
        String[] items = getCities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
        //spinner.performClick();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chooseLocation(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public String[] getCities(){
        String[] list = new String[Cities.values().length];
        int i=0;
        for(Cities city : Cities.values()){
            list[i++] = city.getName();
        }
        return list;
    }

    public boolean authenticate(){
        return primarieLocalStorage.getUserLoggedIn();
    }
    public boolean authUser(){
        return userLocalStorage.getUserLoggedIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(authUser()){
            spinner.setVisibility(View.GONE);
        }
    }

    public void chooseLocation(String location){
        if(authenticate()){
            primarieref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        System.out.println("hello"+dataSnapshot.getValue(Primarie.class).getPrimarie());
                        if(dataSnapshot.child("primarie").exists()){
                            Primarie x = dataSnapshot.getValue(Primarie.class);
                            Primarie y = primarieLocalStorage.getLoggedInUser();
                            assert x != null;
                            if(y.getPrimarie().equals(x.getPrimarie()))
                             dataSnapshot.child("location").getRef().setValue(location);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else
            System.out.println("error");
    }
    public void changePassword(View view){

    }


}