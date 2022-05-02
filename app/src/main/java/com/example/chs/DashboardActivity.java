package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements PostAdapter.OnItemListener {
    private TextView dash;
    private FloatingActionButton add;
    private FloatingActionButton settings;
    private FloatingActionButton map;
    private Post[] postss;
    private PrimarieLocalStorage primarie;
    private UserLocalStorage userLocalStorage;
    private Categorie[] categories = {
            new Categorie("drumuri publice"),
            new Categorie("animale"),
            new Categorie("parcuri"),
            new Categorie("cladiri"),
            new Categorie("test")
    };
    private List<Post> posts = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dash = (TextView)findViewById(R.id.dashboard);
        add = (FloatingActionButton)findViewById(R.id.addbutton);
        settings  = (FloatingActionButton)findViewById(R.id.manage);
        map = (FloatingActionButton)findViewById(R.id.mapmode);
        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        //if (authenticate()) add.setVisibility(FloatingActionButton.INVISIBLE);
        /*posts = new Post[]{
                new Post("name1","location1","desc \n fdsfd\ndfsfgds\n"),
                new Post("name1","location1","desc"),
                new Post("name1","location1","desc")
        };*/
        //if(authenticate()) add.setClickable(false);
        for(Categorie cat: categories){
            //System.out.println(cat.getNume());
            DatabaseReference databaseReference = database.getReference(cat.getNume());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postsnap : snapshot.getChildren()){
                        Post mPost = postsnap.getValue(Post.class);
                        //System.out.println(mPost.getLocation());
                        posts.add(mPost);
                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.posts);
        PostAdapter postAdapter = new PostAdapter(posts,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

    }
    public void clickMap(View view){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
    public void clickManage(View view){
        Intent intent = new Intent(this, Notification.class);
        startActivity(intent);
    }
    public void clickAdd(View view){
        Intent intent = new Intent(this,AddPost.class);
        startActivity(intent);
    }

    private boolean authenticate(){
        primarie = new PrimarieLocalStorage(this);
        if(primarie.getLoggedInUser() == null) return false;
        return primarie.getUserLoggedIn();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ReviewPost.class);
        Post newpost = posts.get(position);
        intent.putExtra("namep",newpost.getName());
        intent.putExtra("locationp",newpost.getLocation());
        intent.putExtra("descp",newpost.getDescription());
        intent.putExtra("post_image",newpost.getImages());
        intent.putExtra("post_op",newpost.getOp().getEmail());
        //String categ = newpost.getCategorie();
        //System.out.println(categ);
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        //primarie = new PrimarieLocalStorage(this);

    }
}