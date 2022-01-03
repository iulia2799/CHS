package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//aici vom vedea informatii despre o anume primarie
public class ViewPrimarie extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_primarie);

        button = findViewById(R.id.button2);

    }
    public void clickOnButton(View view){
        startActivity(new Intent(this,PrimarieDashboard.class));
    }
}