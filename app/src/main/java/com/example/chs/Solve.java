package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.Categorie;
import com.example.chs.data.DAOPost;
import com.example.chs.data.Post;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Solve extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private TextView desc;
    private Switch aSwitch;
    private Post post;
    private PrimarieLocalStorage primarie;
    private String p;
    private Categorie[] categories = {
            new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("test")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        button = findViewById(R.id.resp);
        textView = findViewById(R.id.solve);
        desc = findViewById(R.id.response);
        aSwitch = findViewById(R.id.couldnotsolve);
    }

    private boolean authenticate(){
        primarie = new PrimarieLocalStorage(this);
        if(primarie.getLoggedInUser() == null) return false;
        return primarie.getUserLoggedIn();
    }

    public void OnClickSolve(View view){
        //update to firebase
        //intent.putExtra("names",newpost.getName());
        //intent.putExtra("locations",newpost.getLocation());
       // intent.putExtra("descs",newpost.getDescription());
        Intent i = getIntent();
        i.getExtras();
        if(authenticate()){
            p = primarie.getLoggedInUser().getEmail();
        }
       // intent.putExtra("post_op",newpost.getOp().getEmail());
        List<Post> posts = new ArrayList<>();
        String postname= i.getStringExtra("names");
        DAOPost daoPost = new DAOPost();
        FirebaseDatabase database =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
        for(Categorie cat : categories){
            DatabaseReference ref = database.getReference(cat.getNume());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();
                    System.out.println("HERE");
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Post mPost= dataSnapshot.getValue(Post.class);
                        assert mPost != null;
                        System.out.println("HERE 1");
                        if(postname.equals(mPost.getName())){
                            if(!aSwitch.isChecked())
                            {dataSnapshot.child("status").getRef().setValue("SOLVED BY : "+p + "-> "+desc.getText().toString());
                            Toast.makeText(getApplicationContext(),"Succesfully updated!",Toast.LENGTH_SHORT).show();}
                            else{
                                dataSnapshot.child("status").getRef().setValue("NOT SOLVED : "+p + "-> "+desc.getText().toString());
                                Toast.makeText(getApplicationContext(),"Succesfully updated!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        //see login to retrieve info
    }
}