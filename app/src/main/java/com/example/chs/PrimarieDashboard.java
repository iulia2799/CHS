package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PrimarieDashboard extends AppCompatActivity {
   private TextView title;
   private FloatingActionButton settings;
   private RecyclerView posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primarie_dashboard);

        title = (TextView) findViewById(R.id.dashboardp);
        settings = (FloatingActionButton) findViewById(R.id.managep);
        Post[] posts = new Post[]{
                new Post("name1","location1","desc"),
                new Post("name1","location1","desc"),
                new Post("name1","location1","desc")
        };
        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.postsp);
        PostAdapter postAdapter = new PostAdapter(posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);
    }
}