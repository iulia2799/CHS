package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {
    private TextView dash;
    private FloatingActionButton add;
    private FloatingActionButton settings;
    private FloatingActionButton map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dash = (TextView)findViewById(R.id.dashboard);
        add = (FloatingActionButton)findViewById(R.id.addbutton);
        settings  = (FloatingActionButton)findViewById(R.id.manage);
        map = (FloatingActionButton)findViewById(R.id.mapmode);
        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }
}