package com.example.chs.data.login;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOPrimarie {
    private DatabaseReference databaseReference;

    public DAOPrimarie(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(Primarie.class.getSimpleName());


    }
    public Task<Void> add(Primarie user){
        return databaseReference.push().setValue(user);
    }
    public Task<Void> update(String key, HashMap<String,Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);

    }
}
