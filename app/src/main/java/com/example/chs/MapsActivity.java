package com.example.chs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.chs.data.Categorie;
import com.example.chs.data.Post;
import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.PrimarieLocalStorage;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private UserLocalStorage userLocalStorage;
    private PrimarieLocalStorage primarieLocalStorage;
    private List<Post> posts = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/");
    private Categorie[] categories = {
      new Categorie("animale"),
            new Categorie("cladiri"),
            new Categorie("drumuri publice"),
            new Categorie("parcuri"),
            new Categorie("test")
    };
    private String items[] = new String[]{"drumuri publice","parcuri","animale","cladiri","test","rezolvate"};

    private Button ranking;
    private Button add;
    private Button profile;
    private ArrayAdapter<String> adapter;


    private Spinner dropdowncat;
    //LocationCallback mLocationCallback =null;

    private Button button;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dropdowncat = (Spinner)findViewById(R.id.mapspinner);
        dropdowncat.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        button=  (Button)findViewById(R.id.currentLoc);
        ranking = (Button)findViewById(R.id.button3);
        add = (Button)findViewById(R.id.button4);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(primarieLocalStorage.getUserLoggedIn()) {
            add.setVisibility(View.GONE);
        }
        profile = (Button)findViewById(R.id.button5);
        searchView = (SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LatLng location = getLocationFromAddress(getApplicationContext(),s);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_activity);
        mapFragment.getMapAsync(this);



    }
    @Override
    public void onPause() {
        super.onPause();
        if(mFusedLocationClient !=null){
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private List<Post> postList = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //add markers
        Toast.makeText(this,"Se incarca, va rog sa asteptati...",Toast.LENGTH_LONG).show();
       for(Categorie cat : categories){
           DatabaseReference ref = database.getReference(cat.getNume());
           ref.addValueEventListener(new ValueEventListener() {
               private static final String TAG = "error";
               Primarie primarie = primarieLocalStorage.getLoggedInUser();
               String locationp = primarie.getLocation();
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot postsnap : snapshot.getChildren()){
                       if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                       Post mPost = postsnap.getValue(Post.class);

                       //
                       assert mPost != null;
                       mPost.setImages(postsnap.child("images").getValue(String.class));
                       String trackingnumber = postsnap.getKey();
                       mPost.setTrackingnumber(trackingnumber);
                       mPost.setCat(cat);
                       String location = mPost.getLocation();
                       //System.out.println(location);
                       //System.out.println(locationp);
                       if(locationp !=null && location!=null){
                           if(!location.contains(locationp)) continue;
                       }
                       if(mPost.getStatus().startsWith("SOLVED") || mPost.getStatus().startsWith("Rezolvat")) continue;
                       if(location !=null){
                           LatLng latLng = getLocationFromAddress(getApplicationContext(),location);
                           MarkerOptions options = new MarkerOptions().position(latLng).title(mPost.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                           Marker marker = mMap.addMarker(options);
                           posts.add(mPost);
                           //System.out.println(mPost.getImages());
                           int days = (int) ((System.currentTimeMillis()- mPost.getDatet())/ (1000*60*60*24));
                           if(days>30 && !mPost.getStatus().contains("posted") && !mPost.getStatus().contains("SOLVED")&& !mPost.getStatus().contains("Rezolvat")){
                               ScorePoints();
                           }

                           //if(days>30 && !mPost.getStatus().contains("posted") && !mPost.getStatus().contains("SOLVED")&& !mPost.getStatus().contains("Rezolvat")){
                          //     ScorePoints();
                          // }
                           assert marker != null;
                           marker.setTag(mPost);
                           System.out.println(mPost.getImages());
                           mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                               @Override
                               public boolean onMarkerClick(@NonNull Marker marker) {
                                   Post post = (Post) marker.getTag();
                                   //System.out.println(post.getImages());
                                   //Toast.makeText(getApplicationContext(),post.getImages().toString(),Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(getApplicationContext(),ReviewPost.class);
                                   intent.putExtra("namep",post.getName());
                                   intent.putExtra("trackingnumber",post.getTrackingnumber());
                                   intent.putExtra("locationp",post.getLocation());
                                   intent.putExtra("descp",post.getDescription());
                                   intent.putExtra("post_image",post.getImages());
                                   System.out.println(post.getImages());
                                   if(post.getOp()!=null)
                                      intent.putExtra("post_op",post.getOp().getUsername());
                                   else intent.putExtra("post_op","anonim");
                                   //String categ = newpost.getCategorie();
                                   intent.putExtra("status",post.getStatus());
                                   intent.putExtra("voturi",post.getVoturi());
                                   intent.putExtra("categorie",post.getCategorie());
                                   //System.out.println(categ);
                                   startActivity(intent);
                                   return true;
                               }
                           });
                       }

                   }
                   searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                       @Override
                       public boolean onQueryTextSubmit(String s) {
                           System.out.println("searching...");
                           searchPost(s);
                           return false;
                       }

                       @Override
                       public boolean onQueryTextChange(String s) {
                           return false;
                       }
                   });
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   throw error.toException();
               }
           });
       }
       //Toast.makeText(this,posts.size(),Toast.LENGTH_SHORT).show();
       //addMarkers();System.out.println(this.posts.size());

    }

    public void searchPost(String s){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Post found = null;
            for(Post p : posts){
                if(p.getTrackingnumber().equals(s)) {
                    found = p;
                }
            }
            if(found !=null) {
                LatLng location = getLocationFromAddress(getApplicationContext(),found.getLocation());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title(found.getTrackingnumber());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mCurrLocationMarker.setTag(found);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
            }

        }
    }

    public void ScorePoints() {
        if(authenticatePrimarie()){
            checkPrimarie(primarieLocalStorage.getLoggedInUser().getEmail());
        }
    }

    public void checkPrimarie(String email){

        DatabaseReference reference =  FirebaseDatabase.getInstance("https://proiect-chs-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Primarie");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot usersnapshot : snapshot.getChildren()){
                    Primarie mUser = usersnapshot.getValue(Primarie.class);
                    if(mUser.getEmail().equals(email)){
                        int points = mUser.getPoints()-10;
                        usersnapshot.child("points").getRef().setValue(points);
                    }else{

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: "+error.getCode());
            }
        });
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                //mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }

        }
    };
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        primarieLocalStorage = new PrimarieLocalStorage(this);
        if(authenticate()){
            displayUserDetails();
        }
        else if(authenticatePrimarie()) displayPrimarieDetails();
    }
    private boolean authenticate(){
        return userLocalStorage.getUserLoggedIn();
    }
    private boolean authenticatePrimarie(){return primarieLocalStorage.getUserLoggedIn();}
    private void displayUserDetails(){
        User pm = userLocalStorage.getLoggedInUser();
        //Toast.makeText(this,pm.getEmail(),Toast.LENGTH_SHORT).show();
    }
    private void displayPrimarieDetails(){
        Primarie pm = primarieLocalStorage.getLoggedInUser();
        //Toast.makeText(this,pm.getEmail(),Toast.LENGTH_SHORT).show();
    }

    public void ClickButton(View view){
        dropdowncat.setVisibility(View.VISIBLE);
        dropdowncat.setAdapter(adapter);
        dropdowncat.performClick();
        dropdowncat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dropdowncat.getSelectedItem().toString().equals("rezolvate"))
                    findRez();
                else SelectItem(new Categorie(dropdowncat.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dropdowncat.setVisibility(View.GONE);
            }
        });
    }

    public void findRez(){
        mMap.clear();
        postList.clear();
        System.out.println("here");
        Toast.makeText(this,"Se incarca, va rog sa asteptati...",Toast.LENGTH_LONG).show();
        for(Categorie cat : categories){
            DatabaseReference ref = database.getReference(cat.getNume());
            ref.addValueEventListener(new ValueEventListener() {
                private static final String TAG = "error";
                Primarie primarie = primarieLocalStorage.getLoggedInUser();
                String locationp = primarie.getLocation();
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postsnap : snapshot.getChildren()){
                        if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                        Post mPost = postsnap.getValue(Post.class);

                        //
                        assert mPost != null;
                        mPost.setImages(postsnap.child("images").getValue(String.class));
                        String trackingnumber = postsnap.getKey();
                        mPost.setTrackingnumber(trackingnumber);
                        mPost.setCat(cat);
                        String location = mPost.getLocation();
                        //System.out.println(location);
                        //System.out.println(locationp);
                        if(locationp !=null && location!=null){
                            if(!location.contains(locationp)) continue;
                        }
                        if(mPost.getStatus().startsWith("Rezolvat") || mPost.getStatus().startsWith("SOLVED"))
                        if(location !=null){
                            LatLng latLng = getLocationFromAddress(getApplicationContext(),location);
                            MarkerOptions options = new MarkerOptions().position(latLng).title(mPost.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                            Marker marker = mMap.addMarker(options);
                            posts.add(mPost);
                            //System.out.println(mPost.getImages());
                            assert marker != null;
                            marker.setTag(mPost);
                            System.out.println(mPost.getImages());
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    Post post = (Post) marker.getTag();
                                    //System.out.println(post.getImages());
                                    //Toast.makeText(getApplicationContext(),post.getImages().toString(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),ReviewPost.class);
                                    intent.putExtra("namep",post.getName());
                                    intent.putExtra("trackingnumber",post.getTrackingnumber());
                                    intent.putExtra("locationp",post.getLocation());
                                    intent.putExtra("descp",post.getDescription());
                                    intent.putExtra("post_image",post.getImages());
                                    System.out.println(post.getImages());
                                    if(post.getOp()!=null)
                                        intent.putExtra("post_op",post.getOp().getUsername());
                                    else intent.putExtra("post_op","anonim");
                                    //String categ = newpost.getCategorie();
                                    intent.putExtra("status",post.getStatus());
                                    intent.putExtra("voturi",post.getVoturi());
                                    intent.putExtra("categorie",post.getCategorie());
                                    //System.out.println(categ);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }
                    }
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            System.out.println("searching...");
                            searchPost(s);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            return false;
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
        }
    }
    public void SelectItem(Categorie cat){
        mMap.clear();
        posts.clear();
        Toast.makeText(this,"Se incarca, va rog sa asteptati...",Toast.LENGTH_LONG).show();
        DatabaseReference ref = database.getReference(cat.getNume());
        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "error";
            Primarie primarie = primarieLocalStorage.getLoggedInUser();
            String locationp = primarie.getLocation();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postsnap : snapshot.getChildren()){
                    if(!postsnap.exists()) Log.e(TAG, "onDataChange: No data");
                    Post mPost = postsnap.getValue(Post.class);
                    assert mPost != null;
                    mPost.setImages(postsnap.child("images").getValue(String.class));
                    String trackingnumber = postsnap.getKey();
                    System.out.println(trackingnumber);
                    mPost.setTrackingnumber(trackingnumber);
                    mPost.setCat(cat);
                    String location = mPost.getLocation();
                    //System.out.println(location);
                    //System.out.println(locationp);
                    if(locationp !=null && location!=null){
                        if(!location.contains(locationp)) continue;
                    }
                    if(mPost.getStatus().startsWith("SOLVED") || mPost.getStatus().startsWith("Rezolvat")) continue;
                    //System.out.println(location);
                    if(location !=null){
                        LatLng latLng = getLocationFromAddress(getApplicationContext(),location);
                        //System.out.println(latLng.toString());
                        System.out.println("Fetched from firebase"+postsnap.child("images").getValue(String.class));
                        System.out.println("Location: "+mPost.getLocation());
                        System.out.println("Images:"+mPost.getImages());
                        MarkerOptions options = new MarkerOptions().position(latLng).title(mPost.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        Marker marker = mMap.addMarker(options);
                        posts.add(mPost);
                        marker.setTag(mPost);
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                Post post = (Post) marker.getTag();
                                //System.out.println("Images:"+post.getImages());
                                Toast.makeText(getApplicationContext(),post.getName(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),ReviewPost.class);
                                intent.putExtra("namep",post.getName());
                                intent.putExtra("locationp",post.getLocation());
                                intent.putExtra("trackingnumber",post.getTrackingnumber());
                                intent.putExtra("descp",post.getDescription());
                                intent.putExtra("post_image",post.getImages());
                                if(post.getOp()!=null)
                                intent.putExtra("post_op",post.getOp().getUsername());
                                else intent.putExtra("post_op","anonim");
                                //String categ = newpost.getCategorie();
                                intent.putExtra("status",post.getStatus());
                                intent.putExtra("voturi",post.getVoturi());
                                intent.putExtra("categorie",post.getCategorie());
//                                intent.putExtra("obj", (Parcelable) post);
                                //System.out.println(post.getImages());
                                //System.out.println(categ);
                                startActivity(intent);
                                return true;
                            }
                        });
                    }
                }
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        System.out.println("searching...");
                        searchPost(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

    }
    public void addMarkers(Post post){
        posts.add(post);
    }
    public LatLng getLocationFromAddress(Context context, String strAddress){
        LatLng p1;
        try{
            Geocoder coder = new Geocoder(context);
            List<Address> addressList;
            addressList = coder.getFromLocationName(strAddress,5);
            if(addressList ==null) return null;
            Address location = addressList.get(0);
           //System.out.println(location.toString());
            p1 = new LatLng(location.getLatitude(),location.getLongitude());

        }catch (Exception e){
            e.printStackTrace();
            p1 = new LatLng(34,42);
        }
        return p1;
    }

    public void ClickAdd(View view){
        Intent intent = new Intent(this,AddPost.class);
        startActivity(intent);
    }
    public void Ranking(View view){
        Intent intent = new Intent(this,Ranking.class);
        startActivity(intent);

    }
    public void Profile(View view){

        if(authenticatePrimarie()){
            Intent intent = new Intent(this,Profile.class);
            startActivity(intent);
        }
        if(authenticate()){
            Intent intent = new Intent(this,Profile.class);
            startActivity(intent);
        }


        //String categ = newpost.getCategorie();
        //System.out.println(categ);
        //startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        userLocalStorage.clearUserData();
        primarieLocalStorage.clearUserData();
        finish();
    }
}