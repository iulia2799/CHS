package com.example.chs.data.model;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.chs.R;

public class TermsandConditions extends AppCompatActivity {
    private ImageView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);
        terms = findViewById(R.id.termeni);
    }
}