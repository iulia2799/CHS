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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrimarieDashboard extends AppCompatActivity implements PostAdapter.OnItemListener {
   private TextView title;
   private FloatingActionButton settings;
   private RecyclerView posts;
   private Post[] lists;
    private Categorie[] categories = {
            new Categorie("drumuri publice"),
            new Categorie("animale"),
            new Categorie("parcuri"),
            new Categorie("cladiri"),
            new Categorie("test")
    };
    private List<Post> list = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = (TextView) findViewById(R.id.dashboardp);
        settings = (FloatingActionButton) findViewById(R.id.managep);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primarie_dashboard);

       /* list = new Post[]{
                new Post("name1","location1","desc \n\n fdsfds"),
                new Post("name1","location1","desc"),
                new Post("name1","location1","desc")
        };*/


        for(Categorie cat: categories){
            //System.out.println(cat.getNume());
            DatabaseReference databaseReference = database.getReference(cat.getNume());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postsnap : snapshot.getChildren()){
                        Post mPost = postsnap.getValue(Post.class);
                        //System.out.println(mPost.getLocation());
                        list.add(mPost);
                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.postprimarie);
        PostAdapter postAdapter = new PostAdapter(list,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ReviewPost.class);
        Post newpost = list.get(position);
        intent.putExtra("namep",newpost.getName());
        intent.putExtra("locationp",newpost.getLocation());
        intent.putExtra("descp",newpost.getDescription());
        intent.putExtra("post_image",newpost.getImages());
        intent.putExtra("post_op",newpost.getOp().getEmail());
        startActivity(intent);
    }

    public void clickOnSettings(View view){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

}