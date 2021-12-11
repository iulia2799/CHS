package com.example.chs.data;

import android.graphics.Bitmap;

public class Post {
    private String name;
    private String location;
    private String description;
    private Bitmap[] images;

    public Post(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
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
}
