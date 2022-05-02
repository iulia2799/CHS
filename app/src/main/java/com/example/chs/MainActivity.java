package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    private Button button;
    private ImageView textView;
    private ImageView imageView;
    private Button mapButton;
    private FloatingActionButton setbtn;
    private UserLocalStorage userLocalStorage;
    private PrimarieLocalStorage primarieLocalStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (ImageView) findViewById(R.id.imageView);
        imageView = (ImageView) findViewById(R.id.imageView2);
        button=  (Button)findViewById(R.id.button);
        mapButton = (Button)findViewById(R.id.signbutton);

        button.setAlpha(0f);
        button.setTranslationY(500);

        button.animate().alpha(1f).translationYBy(-500).setDuration(1000);

        mapButton.setAlpha(0f);
        mapButton.setTranslationY(500);

        mapButton.animate().alpha(1f).translationYBy(-500).setDuration(1000);

        textView.setAlpha(0f);
        textView.setTranslationY(500);

        textView.animate().alpha(1f).translationYBy(-500).setDuration(1000);

        imageView.setAlpha(0f);
        imageView.setTranslationY(500);

        imageView.animate().alpha(1f).translationYBy(-500).setDuration(1000);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        userLocalStorage = new UserLocalStorage(this);
        userLocalStorage.clearUserData();
        primarieLocalStorage = new PrimarieLocalStorage(this);
        primarieLocalStorage.clearUserData();
        //setbtn = (FloatingActionButton)findViewById(R.id.setbutton);
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float ts) {
                if(tx>1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
                else if(tx<-1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
            }
        });
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float tx, float ty, float ts) {
                if(ts>1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }else if(ts<-1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        accelerometer.register();
        gyroscope.register();
    }
    @Override
    protected void onPause(){
        super.onPause();
        accelerometer.unregister();
        gyroscope.unregister();
    }
    public void ClickButton(View view){
        Intent intent = new Intent(this, Login.class); //switch to Tabel
        startActivity(intent);

    }
    public void ClickSignUp(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void ClickSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}