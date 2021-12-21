package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chs.data.Categorie;
import com.example.chs.data.DAOPost;
import com.example.chs.data.Post;
import com.example.chs.data.login.User;
import com.example.chs.data.login.UserLocalStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPost extends AppCompatActivity {
    private TextView addpost;
    private TextView name;
    private TextView desc;
    private Button button;
    private TextView searchlocation;
    private Switch anonymous;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button cameraButton;
    private ImageView imageView;
    private UserLocalStorage userLocalStorage;
    private String strAdd;
    private Spinner dropdowncat;
    private String items[] = new String[]{"drumuri publice","parcuri","animale","cladiri","test"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        addpost = (TextView) findViewById(R.id.textView2);
        name = (TextView) findViewById(R.id.postname);
        desc = (TextView) findViewById(R.id.desc);
        searchlocation = (TextView) findViewById(R.id.location_gps);
        anonymous = (Switch) findViewById(R.id.anon);
        button = (Button) findViewById(R.id.loadimage);
        cameraButton = (Button) findViewById(R.id.camerabtn);
        imageView = (ImageView) findViewById(R.id.imageView2);
        dropdowncat = (Spinner) findViewById(R.id.categoriespinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdowncat.setAdapter(adapter);


        button.setOnClickListener(view -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        });

        if(ContextCompat.checkSelfPermission(AddPost.this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddPost.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100
            );

        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getLocation();
            }
        };
        new Thread(runnable).start();

    }

    public void getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                location -> {
                    if (location != null)
                        getAddress(location.getLatitude(),
                                location.getLongitude());
                });
    }

    public void clickAddPost(View view){
        Context context = getApplicationContext();
        Post post = new Post(name.getText().toString(),strAdd,desc.getText().toString(),new Categorie(dropdowncat.getSelectedItem().toString()));
        DAOPost daopost = new DAOPost(post.getCategorie());
        daopost.add(post).addOnSuccessListener(suc -> {
            Toast.makeText(getApplicationContext(), "Succesfully added post", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getApplicationContext(), PrimarieDashboard.class);
            //startActivity(intent);

        }).addOnFailureListener(fail -> {
            Toast.makeText(getApplicationContext(), "Failed to add post " + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    public void getAddress(double latitude, double longitude){
        strAdd = "";
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try{
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            if(addressList !=null){
                Address returnedAddress = addressList.get(0);
                StringBuilder strreturnedaddress = new StringBuilder("");
                for(int i=0;i<=returnedAddress.getMaxAddressLineIndex();i++){
                    strreturnedaddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strreturnedaddress.toString();
                searchlocation.setText(strAdd);
            }else{
                Toast.makeText(getApplicationContext(),"no address found",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        if(requestCode == 100){
            Bitmap capture = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(capture);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        userLocalStorage = new UserLocalStorage(this);
        if(authenticate()){
            displayUserDetails();
        }
    }
    private boolean authenticate(){
        return userLocalStorage.getUserLoggedIn();
    }
    private void displayUserDetails(){
        User user = userLocalStorage.getLoggedInUser();
        Toast.makeText(this,user.getEmail(),Toast.LENGTH_SHORT).show();
    }

    public void ClickCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

}