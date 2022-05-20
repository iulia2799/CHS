package com.example.chs.data.login;

import com.example.chs.data.model.Alert;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAONotification {
    private DatabaseReference databaseReference;

    public DAONotification(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(User.class.getSimpleName());
    }
    public DAONotification(String key){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(User.class.getSimpleName()+"/"+key+"/alertList/");
        System.out.println(databaseReference.toString());
    }
    public Task<Void> add(Alert alert){
        return databaseReference.push().setValue(alert);
    }
}
