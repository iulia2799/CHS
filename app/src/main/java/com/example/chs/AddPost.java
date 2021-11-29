package com.example.chs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                           // searchlocation.setText(location.getLatitude()+","+location.getLongitude());
                           getAddress(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
    }
    public void clickAddPost(View view){
        Context context = getApplicationContext();
        String text = "the post was added";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }
    public void getAddress(double latitude, double longitude){
        String strAdd = "";
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
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}