package com.example.chs;

import static android.widget.Toast.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private ImageView switchd;
    private UserLocalStorage userLocalStorage;
    private String strAdd;
    private Spinner dropdowncat;
    private String items[] = new String[]{"drumuri publice","parcuri","animale","cladiri","test"};
    private Bitmap capture;
    private User user;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://proiect-chs.appspot.com");
    private Date date = Calendar.getInstance().getTime();
    private String post_location="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        addpost = (TextView) findViewById(R.id.reviewp);
        name = (TextView) findViewById(R.id.postname);
        desc = (TextView) findViewById(R.id.desc);
        searchlocation = (TextView) findViewById(R.id.location_gps);
        anonymous = (Switch) findViewById(R.id.anon);
        button = (Button) findViewById(R.id.loadimage);
        switchd = findViewById(R.id.anons);
        cameraButton = (Button) findViewById(R.id.camerabtn);
        imageView = (ImageView) findViewById(R.id.imagep);
        dropdowncat = (Spinner) findViewById(R.id.mapspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdowncat.setAdapter(adapter);

        searchlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    post_location = searchlocation.getText().toString();
                    System.out.println(post_location);
                }
            }
        });


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
        System.out.println(post_location);
        StorageReference imagesref= storageReference.child("posts/"+date.toString());
        final String[] imageurl = {""};
        if(capture != null || imageView.getDrawable() !=null){
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imagesref.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                imagesref.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageurl[0] = uri.toString();
                    //makeText(getApplicationContext(), imageurl[0], LENGTH_SHORT).show();
                    addToFirebase(imageurl[0]);
                }).addOnFailureListener(fail -> System.out.println("FAILED TO GET DOWNLOAD URL \n\n\n\n\n"));
            }).addOnFailureListener(exception -> {
                System.out.println("failure to add images");
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
        }else {
            Post post;
            if (user == null) {
                Toast.makeText(this, "You can't post!!!", LENGTH_SHORT).show();
                return;
            }
            if (anonymous.isChecked()) {
                post = new Post(name.getText().toString(), searchlocation.getText().toString(), desc.getText().toString(), new Categorie(dropdowncat.getSelectedItem().toString()), System.currentTimeMillis());
            } else
                post = new Post(name.getText().toString(), searchlocation.getText().toString(), desc.getText().toString(), user, new Categorie(dropdowncat.getSelectedItem().toString()), System.currentTimeMillis());
            DAOPost daopost = new DAOPost(post.getCategorie());
            daopost.add(post).addOnSuccessListener(suc -> {
                makeText(getApplicationContext(), "Succesfully added post", LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), PrimarieDashboard.class);
                //startActivity(intent);

            }).addOnFailureListener(fail -> makeText(getApplicationContext(), "Failed to add post " + fail.getMessage(), LENGTH_SHORT).show());
        }

    }
    public void addToFirebase(String imageurl){
        Post post;
        if(anonymous.isChecked()){
            post = new Post(name.getText().toString(),searchlocation.getText().toString(),desc.getText().toString(),new Categorie(dropdowncat.getSelectedItem().toString()),imageurl,System.currentTimeMillis());
        }
        else post = new Post(name.getText().toString(),searchlocation.getText().toString(),desc.getText().toString(),user,new Categorie(dropdowncat.getSelectedItem().toString()),imageurl,System.currentTimeMillis());
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
                post_location = strAdd;
                System.out.println("First : "+post_location);
                searchlocation.setText(strAdd, TextView.BufferType.EDITABLE);
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
            //capture = (Bitmap) data.getExtras().get("data");
            System.out.println("Get from camera : "+currentPhotoPath);
            Uri uri = Uri.fromFile(new File(currentPhotoPath));
            //imageView.setImageBitmap(capture);
            imageView.setImageURI(uri);

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
        // Ensure that there's a camera activity to handle the intent
        //if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("error");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent,100);
            }
        //}else System.out.println("error");

    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println(currentPhotoPath);
        return image;
    }
}