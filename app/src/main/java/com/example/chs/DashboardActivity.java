package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements PostAdapter.OnItemListener {
    private TextView dash;
    private FloatingActionButton add;
    private FloatingActionButton settings;
    private FloatingActionButton map;
    private Post[] posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dash = (TextView)findViewById(R.id.dashboard);
        add = (FloatingActionButton)findViewById(R.id.addbutton);
        settings  = (FloatingActionButton)findViewById(R.id.manage);
        map = (FloatingActionButton)findViewById(R.id.mapmode);
        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        posts = new Post[]{
                new Post("name1","location1","desc \n fdsfd\ndfsfgds\n"),
                new Post("name1","location1","desc"),
                new Post("name1","location1","desc")
        };
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
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void clickAdd(View view){
        Intent intent = new Intent(this,AddPost.class);
        startActivity(intent);
    }

    public void clickOnPost(){

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ReviewPost.class);
        Post newpost = posts[position];
        intent.putExtra("namep",newpost.getName());
        intent.putExtra("locationp",newpost.getLocation());
        intent.putExtra("descp",newpost.getDescription());
        startActivity(intent);
    }
}