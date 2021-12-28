package com.example.chs;

import static android.widget.Toast.*;

import androidx.annotation.NonNull;
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
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
    private Bitmap capture;
    private User user;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://proiect-chs.appspot.com");
    private Date date = Calendar.getInstance().getTime();


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
        Runnable runnable = this::getLocation;
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

        StorageReference imagesref= storageReference.child("images/"+date.toString());
        final String[] imageurl = {""};
        if(capture != null){
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imagesref.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                imagesref.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageurl[0] = uri.toString();
                    makeText(getApplicationContext(), imageurl[0], LENGTH_SHORT).show();
                    addToFirebase(imageurl[0]);
                }).addOnFailureListener(fail -> System.out.println("FAILED TO GET DOWNLOAD URL \n\n\n\n\n"));
            });
           /*imagesref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   String selecteddownload = uri.toString();


               }
           }).addOnFailureListener(fail ->{
               Toast.makeText(this,"failed : \n" + fail.getMessage(),Toast.LENGTH_SHORT).show();
           });*/
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        Post post = new Post(name.getText().toString(),strAdd,desc.getText().toString(),user,new Categorie(dropdowncat.getSelectedItem().toString()));
        DAOPost daopost = new DAOPost(post.getCategorie());
        daopost.add(post).addOnSuccessListener(suc -> {
            makeText(getApplicationContext(), "Succesfully added post", LENGTH_SHORT).show();
            //Intent intent = new Intent(getApplicationContext(), PrimarieDashboard.class);
            //startActivity(intent);

        }).addOnFailureListener(fail -> makeText(getApplicationContext(), "Failed to add post " + fail.getMessage(), LENGTH_SHORT).show());


    }
    public void addToFirebase(String imageurl){
        Post post = new Post(name.getText().toString(),strAdd,desc.getText().toString(),user,new Categorie(dropdowncat.getSelectedItem().toString()),imageurl);
        DAOPost daopost = new DAOPost(post.getCategorie());
        daopost.add(post).addOnSuccessListener(suc -> {
            makeText(getApplicationContext(), "Succesfully added post", LENGTH_SHORT).show();
            //Intent intent = new Intent(getApplicationContext(), PrimarieDashboard.class);
            //startActivity(intent);

        }).addOnFailureListener(fail -> makeText(getApplicationContext(), "Failed to add post " + fail.getMessage(), LENGTH_SHORT).show());

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
                makeText(getApplicationContext(),"no address found", LENGTH_SHORT).show();
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
            imageView.setImageURI(selectedImage);
            //imageView.getLayoutParams().width-=20;
        }
        if(requestCode == 100){
            capture = (Bitmap) data.getExtras().get("data");
            //Uri uri = data.getData();
            imageView.setImageBitmap(capture);
            //imageView.setImageURI(uri);

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
        user = userLocalStorage.getLoggedInUser();
        //Toast.makeText(this,user.getEmail(),Toast.LENGTH_SHORT).show();
    }

    public void ClickCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

}