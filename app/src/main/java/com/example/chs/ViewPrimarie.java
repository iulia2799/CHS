package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

//aici vom vedea informatii despre o anume primarie la log in
public class ViewPrimarie extends AppCompatActivity {
    private Button button;
    private TextView nume;
    private TextView contact;
    private RecyclerView infoacte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_primarie);

        button = findViewById(R.id.button2);
        nume  = findViewById(R.id.nume_primarie);
        contact = findViewById(R.id.contact);
        infoacte = findViewById(R.id.infoacte);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        String email = intent.getStringExtra("emailp");
        contact.setText(email);
    }
    public void clickOnButton(View view){
        startActivity(new Intent(this,PrimarieDashboard.class));
    }
}