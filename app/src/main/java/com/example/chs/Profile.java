package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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

import com.example.chs.data.ActAdapter;
import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.example.chs.data.model.Act;
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
    public MaterialButton prinfoedit;
    private MaterialButton editareinfo;
    private RecyclerView recyclerView;
    public User userlog;
    public TextView infotitle;
    private TextView puncte;
    public PopupWindow window;
    private boolean editmode = false;
    private Intent i;
    private List<Act> plinks = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<Primarie> primarieList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private Categorie[] categories = {
            new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("curatenie"),
            new Categorie("altele")
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        i = getIntent();
        button = findViewById(R.id.options);
        editareinfo = findViewById(R.id.clickedit);
        informatii = findViewById(R.id.info);
        informatii.setText("Informatii");
        editinfo = findViewById(R.id.editare);
        editinfo.setVisibility(View.GONE);
        infotitle = findViewById(R.id.username2);
        username = findViewById(R.id.Username);
        username.setText("username");
        prinfoedit = findViewById(R.id.acte);
        puncte = findViewById(R.id.puncte);
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
                        String placeholder = mUser.getPoints() + " puncte";
                        puncte.setText(placeholder);
                    }else{
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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
                        String placeholder = mUser.getPoints() + " puncte";
                        puncte.setText(placeholder);
                    }else{
                        System.out.println(primarielog.getEmail()+","+mUser.getEmail());
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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

    protected void getUserInfo(String user_name){
        userlog = this.userLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    User mUser = usersnapshot.getValue(User.class);
                    if(mUser.getUsername().equals(user_name)){
                        informatii.setText(mUser.getInformatii());
                        username.setText(mUser.getUsername());
                        String placeholder = mUser.getPoints() + " puncte";
                        puncte.setText(placeholder);
                    }else{
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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

    protected void getIns(String primarie){
        primarielog = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                primarieList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getPrimarie().equals(primarie)){
                        informatii.setText(mUser.getInformatii());
                        username.setText(mUser.getPrimarie());
                        String placeholder = mUser.getPoints() + " puncte";
                        puncte.setText(placeholder);
                    }else{
                        System.out.println(primarielog.getEmail()+","+mUser.getEmail());
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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

    public void EditInfo(View view){
        if(authp()){
            editPinfo(view);
        }else{
            editUserInfo(view);
        }
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
                            //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
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
    public void editPinfo(View view){
        if(!editmode) {
            informatii.setVisibility(View.GONE);
            editinfo.setVisibility(View.VISIBLE);
            editinfo.setText(informatii.getText().toString());
            editmode=true;
        }else{
            primarielog.setInformatii(editinfo.getText().toString());
            editmode = false;
            DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    primarieList.clear();
                    for(DataSnapshot usersnapshot : snapshot.getChildren()){
                        Primarie mUser = usersnapshot.getValue(Primarie.class);
                        assert mUser != null;
                        if(mUser.getEmail().equals(primarielog.getEmail())){
                            usersnapshot.child("informatii").getRef().setValue(primarielog.getInformatii());
                        }else{
                            System.out.println(mUser.getEmail());
                            //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                        }
                        primarieList.add(mUser);
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
            prinfoedit.setVisibility(View.GONE);
            if(!getIntent().hasExtra("username")) {
                displayUser();
                getUserInfo();
                findPosts();
            } else {
                editareinfo.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                if(getIntent().getStringExtra("type").equals("user")) {
                    getUserInfo(getIntent().getStringExtra("username"));
                    findPosts(getIntent().getStringExtra("username"));
                } else {
                    getIns(getIntent().getStringExtra("username"));
                    findLinks(getIntent().getStringExtra("username"));
                }

            }

        }else if(authp()){
            if(!getIntent().hasExtra("username")) {
                display();
                getIns();
                findLinks();
            } else {
                editareinfo.setVisibility(View.GONE);
                prinfoedit.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                if (getIntent().getStringExtra("type").equals("user")) {
                    getUserInfo(getIntent().getStringExtra("username"));
                    findPosts(getIntent().getStringExtra("username"));
                } else {
                    getIns(getIntent().getStringExtra("username"));
                    findLinks(getIntent().getStringExtra("username"));
                }

            }

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
                        if(postsnap.child("voturi").exists() && postsnap.child("op").exists()) {
                            if(postsnap.child("status").exists() && postsnap.child("op").getValue(User.class).getEmail().equals(userLocalStorage.getLoggedInUser().getEmail())){
                                if(!postsnap.child("status").getValue(String.class).contains("SOLVED") && !postsnap.child("status").getValue(String.class).contains("Rezolvat")){
                                    Post mPost = postsnap.getValue(Post.class);
                                    assert mPost != null;
                                    mPost.setCat(new Categorie(postsnap.child("categorie").getValue(String.class)));
                                    mPost.setTrackingnumber(postsnap.getKey());
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
                            Post post = postList.get(position);
                            Intent intent = new Intent(getApplicationContext(),ReviewPost.class);
                            intent.putExtra("namep",post.getName());
                            intent.putExtra("trackingnumber",post.getTrackingnumber());
                            intent.putExtra("locationp",post.getLocation());
                            intent.putExtra("descp",post.getDescription());
                            intent.putExtra("post_image",post.getImages());
                            System.out.println(post.getImages());
                            if(post.getOp()!=null)
                                intent.putExtra("post_op",post.getOp().getUsername());
                            else intent.putExtra("post_op","anonim");
                            //String categ = newpost.getCategorie();
                            intent.putExtra("status",post.getStatus());
                            intent.putExtra("voturi",post.getVoturi());
                            intent.putExtra("categorie",post.getCategorie());
                            //System.out.println(categ);
                            startActivity(intent);
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
        plinks.clear();
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
                        if(dataSnapshot.child("links").exists()){
                            for(DataSnapshot link : dataSnapshot.child("links").getChildren()){
                                System.out.println(link.getKey());

                                Act act  = link.getValue(Act.class);
                                plinks.add(act);
                            }
                        }
                    }else{
                        //System.out.println(primarielog.getEmail()+","+mUser.getEmail());
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                    }
                    primarieList.add(mUser);
                }
                ActAdapter adapter = new ActAdapter(plinks, new ActAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(int position) {
                        String l=  plinks.get(position).getLink();
                        String name = plinks.get(position).getNume();
                        System.out.println(l);
                        downloadFile(l,name);
                    }
                });
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }

    public void findPosts(String user_name){
        for(Categorie cat : categories){
            postList.clear();
            DatabaseReference ref = database.getReference(cat.getNume());
            ref.addValueEventListener(new ValueEventListener() {
                private static final String TAG = "error";
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postsnap : snapshot.getChildren()){
                        if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                        if(postsnap.child("voturi").exists() && postsnap.child("op").exists()) {
                            if(postsnap.child("status").exists() && postsnap.child("op").child("username").exists() && postsnap.child("op").getValue(User.class).getUsername().equals(user_name)){
                                if(!postsnap.child("status").getValue(String.class).contains("SOLVED") && !postsnap.child("status").getValue(String.class).contains("Rezolvat")){
                                    Post mPost = postsnap.getValue(Post.class);
                                    assert mPost != null;
                                    mPost.setCat(new Categorie(postsnap.child("categorie").getValue(String.class)));
                                    mPost.setTrackingnumber(postsnap.getKey());
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
                            Post post = postList.get(position);
                            Intent intent = new Intent(getApplicationContext(),ReviewPost.class);
                            intent.putExtra("namep",post.getName());
                            intent.putExtra("locationp",post.getLocation());
                            intent.putExtra("trackingnumber",post.getTrackingnumber());
                            intent.putExtra("descp",post.getDescription());
                            intent.putExtra("post_image",post.getImages());
                            if(post.getOp()!=null)
                                intent.putExtra("post_op",post.getOp().getUsername());
                            else intent.putExtra("post_op","anonim");
                            //String categ = newpost.getCategorie();
                            intent.putExtra("status",post.getStatus());
                            intent.putExtra("voturi",post.getVoturi());
                            intent.putExtra("categorie",post.getCategorie());
//                                intent.putExtra("obj", (Parcelable) post);
                            //System.out.println(post.getImages());
                            //System.out.println(categ);
                            startActivity(intent);
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

    public void findLinks(String user_name){
        plinks.clear();
        primarielog = this.primarieLocalStorage.getLoggedInUser();
        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                primarieList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Primarie mUser = dataSnapshot.getValue(Primarie.class);
                    assert mUser != null;
                    if(mUser.getPrimarie().equals(user_name)){
                        if(dataSnapshot.child("links").exists()){
                            for(DataSnapshot link : dataSnapshot.child("links").getChildren()){
                                System.out.println(link.getKey());

                                Act act  = link.getValue(Act.class);
                                plinks.add(act);
                            }
                        }
                    }else{
                        //System.out.println(primarielog.getEmail()+","+mUser.getEmail());
                        //Toast.makeText(getApplicationContext(),"oops...",Toast.LENGTH_SHORT).show();
                    }
                    primarieList.add(mUser);
                }
                ActAdapter adapter = new ActAdapter(plinks, new ActAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(int position) {
                        String l=  plinks.get(position).getLink();
                        String name = plinks.get(position).getNume();
                        System.out.println(l);
                        downloadFile(l,name);
                    }
                });
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }

    private String down_name="";
    private String down_link="";
    public void downloadFile(String link,String filename){
        //DownloadManager manager;
        down_name = filename;
        down_link = link;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,1000);
                System.out.println("yet to grant");
            }else{
                System.out.println("granted1");
               download();
            }
        }else{
            System.out.println("granted2");
           download();
        }

        //manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //Uri uri = Uri.parse(link);
       // DownloadManager.Request request = new DownloadManager.Request(uri);
       // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
       // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
       // long reference = manager.enqueue(request);
    }

    public void download(){
        DownloadManager manager;
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(down_link);
        System.out.println("downloading...");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Downloading file...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,down_name);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ){
        switch (requestCode){
            case 1000:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("granted3");
                   download();
                }else{
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void EditareActe(View view){
        Intent intent = new Intent(this,CreateActe.class);
        startActivity(intent);
    }
}