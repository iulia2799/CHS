package com.example.chs.data;

import com.example.chs.data.login.Primarie;
import com.example.chs.data.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOPost {
    private DatabaseReference databaseReference;

    public DAOPost(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(com.example.chs.data.Post.class.getSimpleName());


    }
    public Task<Void> add(Post post){
        return databaseReference.push().setValue(post);
    }
    public Task<Void> update(String key, HashMap<String,Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);

    }
}
