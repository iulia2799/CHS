package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.chs.data.Post;

public class Solve extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private TextView desc;
    private Switch aSwitch;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        button = findViewById(R.id.resp);
        textView = findViewById(R.id.solve);
        desc = findViewById(R.id.response);
        aSwitch = findViewById(R.id.couldnotsolve);
    }

    void OnClickSolve(View view){
        //update to firebase
        //intent.putExtra("names",newpost.getName());
        //intent.putExtra("locations",newpost.getLocation());
       // intent.putExtra("descs",newpost.getDescription());

       // intent.putExtra("post_op",newpost.getOp().getEmail());
        if(aSwitch.isChecked()) post.setStatus("unsolved");
        else post.setStatus("solved");
        //see login to retrieve info
    }
}