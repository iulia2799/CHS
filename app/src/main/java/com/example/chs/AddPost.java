package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

public class AddPost extends AppCompatActivity {
    private TextView addpost;
    private TextView name;
    private TextView desc;
    private TextView searchlocation;
    private Switch anonymous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        addpost = (TextView) findViewById(R.id.textView2);
        name = (TextView) findViewById(R.id.postname);
        desc = (TextView) findViewById(R.id.desc);
        searchlocation = (TextView) findViewById(R.id.location_gps);
        anonymous = (Switch) findViewById(R.id.anon);
    }
}