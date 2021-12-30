package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Solve extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private TextView desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        button = findViewById(R.id.resp);
        textView = findViewById(R.id.solve);
        desc = findViewById(R.id.response);

    }

    void OnClickSolve(View view){

    }
}