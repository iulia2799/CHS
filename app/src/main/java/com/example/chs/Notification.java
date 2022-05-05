package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.example.chs.data.model.NotificationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Notification extends AppCompatActivity implements NotificationAdapter.OnItemListener {
    private TextView textView;
    private RecyclerView recyclerView;
    private UserLocalStorage userLocalStorage;
    private User user;
    private HashMap<String,String> notificationlist  = new HashMap<String,String>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textView = findViewById(R.id.notext);
        userLocalStorage = new UserLocalStorage(this);
        user= userLocalStorage.getLoggedInUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        DatabaseReference databaseReference = database.getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User mUser = dataSnapshot.getValue(User.class);
                    assert mUser != null;
                    if(mUser.getEmail().equals(user.getEmail()))
                    {notificationlist = mUser.getNotifications();
                     System.out.println(mUser.getNotifications());
                     break;
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recynot);
        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationlist,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notificationAdapter);
        System.out.println(notificationlist);

    }

    @Override
    public void onItemClick(int position) {

    }
}