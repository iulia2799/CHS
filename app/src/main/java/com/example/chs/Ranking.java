package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.example.chs.data.login.User;
import com.example.chs.data.model.NotificationAdapter;
import com.example.chs.data.model.UserAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private TextView title;
    private MaterialButton button;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private Categorie[] categories = {
            new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("test")
    };
    private String items[] = new String[]{"drumuri publice","parcuri","animale","cladiri","test","users"};
    private ArrayAdapter<String> adapter;
    private List<Post> postList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        title = findViewById(R.id.rank);
        button = findViewById(R.id.filter);
        spinner = findViewById(R.id.mapspinner2);
        spinner.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        recyclerView = (RecyclerView) findViewById(R.id.rankview);
        findAll();



    }

    public void findAll(){
        for(Categorie cat : categories){
            postList.clear();
            DatabaseReference ref = database.getReference(cat.getNume());
            ref.addValueEventListener(new ValueEventListener() {
                private static final String TAG = "error";
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postsnap : snapshot.getChildren()){
                        if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                        if(postsnap.child("voturi").exists()) {
                            if(postsnap.child("status").exists()){
                                if(!postsnap.child("status").getValue(String.class).contains("SOLVED") && !postsnap.child("status").getValue(String.class).contains("Rezolvat")){
                                    Post mPost = postsnap.getValue(Post.class);
                                    postList.add(mPost);
                                }
                            }

                        }
                    }
                    Collections.sort(postList, new Comparator<Post>() {
                        @Override
                        public int compare(Post post, Post t1) {
                            return t1.getVoturi()-post.getVoturi();
                        }
                    });

                    PostAdapter postAdapter = new PostAdapter(postList, new PostAdapter.OnItemListener() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(postAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
        }
    }
    public void findUsers(){
        DatabaseReference ref = database.getReference("User");
        userList.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    userList.add(mUser);
                }
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        return t1.getPoints()-user.getPoints();
                    }
                });
                UserAdapter adapter = new UserAdapter(userList, new UserAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ClickFilter(View view){
        spinner.setVisibility(View.VISIBLE);
        spinner.setAdapter(adapter);
        spinner.performClick();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("users")){
                    findUsers();
                }else
                SelectItem(new Categorie(spinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    public void SelectItem(Categorie cat){
        postList.clear();
        DatabaseReference ref = database.getReference(cat.getNume());
        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "error";

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnap : snapshot.getChildren()){
                    if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                    if(postsnap.child("voturi").exists()) {
                        if(postsnap.child("status").exists()){
                            if(!postsnap.child("status").getValue(String.class).contains("SOLVED") && !postsnap.child("status").getValue(String.class).contains("Rezolvat")){
                                Post mPost = postsnap.getValue(Post.class);
                                postList.add(mPost);
                            }
                        }

                    }

                }
                Collections.sort(postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post post, Post t1) {
                        return t1.getVoturi()-post.getVoturi();
                    }
                });

                PostAdapter postAdapter = new PostAdapter(postList, new PostAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}