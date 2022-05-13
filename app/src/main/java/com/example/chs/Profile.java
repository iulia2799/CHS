package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
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
import java.util.Collections;
import java.util.Comparator;
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
    private RecyclerView recyclerView;
    public User userlog;
    public TextView infotitle;
    public PopupWindow window;
    private boolean editmode = false;
    private List<User> userList = new ArrayList<>();
    private List<Primarie> primarieList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
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
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = new PopupWindow(inflater.inflate(R.layout.popup,null,false),100,100,true);
        recyclerView = findViewById(R.id.userpost);
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
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                primarieList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getEmail().equals(primarielog.getEmail())){
                        informatii.setText(mUser.getInformatii());
                        username.setText(mUser.getPrimarie());
                    }else{
                        System.out.println(primarielog.getEmail()+","+mUser.getEmail());
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
                        assert mUser != null;
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

    public void onOptions(View view){
        Clickoptions(view);
    }

    public void Clickoptions(View view){
        //Intent intent = new Intent(this, Notification.class);
        //startActivity(intent);
        View v = View.inflate(this,R.layout.popup, null);
        ImageView notificon = v.findViewById(R.id.bell);
        TextView n = v.findViewById(R.id.textnot);
        ImageView seticon = v.findViewById(R.id.rot);
        TextView s = v.findViewById(R.id.textrot);
        window = new PopupWindow(v,600, 300, true);
        window.showAsDropDown(view,-100,0);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Setari.class);
                startActivity(intent);
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Notification.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(auth()){
            displayUser();
            getUserInfo();
            findPosts();
        }else if(authp()){
            display();
            getIns();
        }
    }

    public void findPosts(){
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
                            if(postsnap.child("status").exists() && postsnap.child("op").getValue(User.class).getEmail().equals(userLocalStorage.getLoggedInUser().getEmail())){
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

    public void findLinks(){

    }
}