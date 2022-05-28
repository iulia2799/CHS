package com.example.chs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.example.chs.data.model.Alert;
import com.example.chs.data.model.NotificationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Notification extends AppCompatActivity implements NotificationAdapter.OnItemListener {
    private TextView textView;
    private RecyclerView recyclerView;
    private UserLocalStorage userLocalStorage;
    private PrimarieLocalStorage primarieLocalStorage;
    private User user;
    private Primarie primarie;
    private HashMap<String,String> notificationlist  = new HashMap<String,String>();
    private List<Alert> alerts = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //textView = findViewById(R.id.notext);
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        textView = findViewById(R.id.notext);
        if(userLocalStorage.getUserLoggedIn()) {
            user = userLocalStorage.getLoggedInUser();
            DatabaseReference databaseReference = database.getReference("User");
            //System.out.println("before");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User mUser = dataSnapshot.getValue(User.class);
                        assert mUser != null;
                        if (mUser.getEmail().equals(user.getEmail())) {
                            if (!dataSnapshot.child("alertList").exists()) {
                                //Alert mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Welcome to app");
                                //dataSnapshot.child("alertList").child("0").getRef().setValue(mAlert);
                                //alerts.add(mAlert);
                            } else
                                for (DataSnapshot ing : dataSnapshot.child("alertList").getChildren()) {

                                    Alert mAlert = ing.getValue(Alert.class);
                                    assert mAlert != null;
                                    //System.out.println(mAlert.getDescription());
                                    alerts.add(mAlert);
                                    //System.out.println(alerts.get(0).getDate());
                                    //System.out.println("in");

                                }

                        }
                        NotificationAdapter postAdapter = new NotificationAdapter(alerts, new NotificationAdapter.OnItemListener() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(postAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (primarieLocalStorage.getUserLoggedIn()) {
            primarie = primarieLocalStorage.getLoggedInUser();
            DatabaseReference databaseReference = database.getReference("Primarie");
            //System.out.println("before");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Primarie mUser = dataSnapshot.getValue(Primarie.class);
                        assert mUser != null;
                        if (mUser.getEmail().equals(primarie.getEmail())) {
                            if (!dataSnapshot.child("alertList").exists()) {
                                //Alert mAlert = new Alert(String.valueOf(System.currentTimeMillis()), "Welcome to app");
                                //dataSnapshot.child("alertList").child("0").getRef().setValue(mAlert);
                                //alerts.add(mAlert);
                            } else
                                for (DataSnapshot ing : dataSnapshot.child("alertList").getChildren()) {

                                    Alert mAlert = ing.getValue(Alert.class);
                                    assert mAlert != null;
                                    //System.out.println(mAlert.getDescription());
                                    alerts.add(mAlert);
                                    //System.out.println(alerts.get(0).getDate());
                                    //System.out.println("in");

                                }

                        }
                        NotificationAdapter postAdapter = new NotificationAdapter(alerts, new NotificationAdapter.OnItemListener() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(postAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        System.out.println("after");
        //System.out.println(alerts.size());
        //alerts.add(new Alert("0","0"));
        recyclerView = (RecyclerView) findViewById(R.id.recynot);
        //databaseReference.addListenerForSingleValueEvent(listener);
        System.out.println(alerts.size());
        //NotificationAdapter postAdapter = new NotificationAdapter(alerts,this);
        //recyclerView.setHasFixedSize(true);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setAdapter(postAdapter);

    }

    @Override
    public void onItemClick(int position) {

    }

    private void readData(DataCallback callback){

        //databaseReference.addValueEventListener(listener);

    }

    public interface DataCallback{
        void onCallback(Alert alert);
    }

    public void Clear(View view){
        if(userLocalStorage.getUserLoggedIn()) {
            user = userLocalStorage.getLoggedInUser();
            DatabaseReference databaseReference = database.getReference("User");
            //System.out.println("before");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User mUser = dataSnapshot.getValue(User.class);
                        assert mUser != null;
                        if (mUser.getEmail().equals(user.getEmail())) {
                            //System.out.println(dataSnapshot.toString());
                            Iterable<DataSnapshot> snap = dataSnapshot.child("alertList").getChildren();
                            for (DataSnapshot ing : dataSnapshot.child("alertList").getChildren()) {

                                Alert mAlert = ing.getValue(Alert.class);
                                if (!mAlert.getDescription().startsWith("Welcome")) {
                                    ing.getRef().removeValue();
                                }

                                assert mAlert != null;
                                //System.out.println(mAlert.getDescription());
                                alerts.add(mAlert);
                                //System.out.println(alerts.get(0).getDate());
                                //System.out.println("in");

                            }

                        }
                    }
                    alerts.add(new Alert(Calendar.getInstance().getTime().toString(),"Notifications cleared"));
                    NotificationAdapter postAdapter = new NotificationAdapter(alerts, new NotificationAdapter.OnItemListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(getApplicationContext(),"fdsfdsfds",Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(postAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if(primarieLocalStorage.getUserLoggedIn()) {
            primarie = primarieLocalStorage.getLoggedInUser();
            DatabaseReference databaseReference = database.getReference("Primarie");
            //System.out.println("before");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Primarie mUser = dataSnapshot.getValue(Primarie.class);
                        assert mUser != null;
                        if (mUser.getEmail().equals(primarie.getEmail())) {
                            //System.out.println(dataSnapshot.toString());
                            Iterable<DataSnapshot> snap = dataSnapshot.child("alertList").getChildren();
                            for (DataSnapshot ing : dataSnapshot.child("alertList").getChildren()) {

                                Alert mAlert = ing.getValue(Alert.class);
                                if (!mAlert.getDescription().startsWith("Welcome")) {
                                    ing.getRef().removeValue();
                                }

                                assert mAlert != null;
                                //System.out.println(mAlert.getDescription());
                                //alerts.add(mAlert);
                                //System.out.println(alerts.get(0).getDate());
                                //System.out.println("in");

                            }

                        }
                    }
                    alerts.add(new Alert(String.valueOf(System.currentTimeMillis()),"Notifications cleared"));
                    NotificationAdapter postAdapter = new NotificationAdapter(alerts, new NotificationAdapter.OnItemListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(getApplicationContext(),"fdsfdsfds",Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(postAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}