package com.example.chs.data;

import android.graphics.Bitmap;

import com.example.chs.data.login.User;

public class Post {
    private String name;
    private String location;
    private String description;
    private Bitmap[] images;
    private String status;
    private User op;
    private Categorie cat;
    private static Integer trackingid=0;
    public Post(){}
    public Post(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = new Categorie("test");
        trackingid++;

    }
    public Post(String name, String location, String description,Categorie categorie) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = categorie;
        trackingid++;

    }
    public Post(String name,String location,String description,User user){
        this.name = name;
        this.location = location;
        this.description = description;
        this.op = user;
        this.status = "posted";
        this.cat = new Categorie("test");
        trackingid++;

    }
    public String getName(){
        return this.name;
    }
    public String getLocation(){
        return this.location;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(Bitmap[] list){
        this.images=list;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }

    public static Integer getTrackingid() {
        return trackingid;
    }
    public String getCategorie(){
        return cat.getNume();
    }
}
