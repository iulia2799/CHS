package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateActe extends AppCompatActivity {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://proiect-chs.appspot.com");
    private PrimarieLocalStorage primarieLocalStorage = new PrimarieLocalStorage(this);
    private Primarie log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(primarieLocalStorage.getUserLoggedIn()){
            log= primarieLocalStorage.getLoggedInUser();
            find();
        }else
            log= null;

        setContentView(R.layout.activity_create_acte);
    }

    public void find(){
        log = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getEmail().equals(log.getEmail())){
                        if(dataSnapshot.child("links").exists()){
                            for(DataSnapshot link : dataSnapshot.getChildren()){

                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }
}