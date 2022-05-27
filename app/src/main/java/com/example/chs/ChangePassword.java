package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chs.data.login.Primarie;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    private String code;
    private String from;
    private MaterialButton confirma;
    private EditText old_pass;
    private EditText new_pass;
    private EditText confirm_new_pass;
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        code = getIntent().getStringExtra("user_code");
        from = getIntent().getStringExtra("intent");
        confirma = findViewById(R.id.button7);
        old_pass = findViewById(R.id.passad2);
        new_pass = findViewById(R.id.passad3);
        confirm_new_pass = findViewById(R.id.passad4);



    }

    public void onClick(View view){
        Validate(new_pass.getText().toString());
    }

    public boolean CheckPasswords(String old, String typed_old) {
        return old.equals(typed_old) && new_pass.getText().toString().equals(confirm_new_pass.getText().toString())
                && typed_old.length()>=8 && new_pass.getText().toString().length()>=8 && confirm_new_pass.getText().toString().length()>=8;
    }
    public void Validate(String new_password) {
        DatabaseReference ref;
        if(from.equals("u")) {
            ref = firebase.getReference("User");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(code).exists()) {
                        DatabaseReference s = snapshot.child(code).child("password").getRef();
                        if(CheckPasswords(snapshot.child(code).child("password").getValue(String.class),old_pass.getText().toString())){
                            SetValue(s,new_password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Trebuie sa completati toate campurile si parola trebuie sa fie " +
                                    "de cel putin 8 caractere", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if(from.equals("p")) {
            ref = firebase.getReference("Primarie");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(code).exists()) {
                        DatabaseReference s = snapshot.child(code).child("password").getRef();
                        if(CheckPasswords(snapshot.child(code).child("password").getValue(String.class),old_pass.getText().toString())){
                            SetValue(s,new_password);
                        }else {
                            Toast.makeText(getApplicationContext(), "Trebuie sa completati toate campurile si parola trebuie sa fie " +
                                    "de cel putin 8 caractere", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void SetValue(DatabaseReference ref,String pass) {
        ref.setValue(pass);
        Toast.makeText(this, "Parola a fost schimbata cu succes!", Toast.LENGTH_SHORT).show();
    }
}