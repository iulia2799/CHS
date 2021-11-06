package com.example.chs.data.posts;

import android.media.Image;

import com.example.chs.data.login.User;

public class Post {
    private String denumire;
    private String location;
    private User user;
    private String descriere;
    private Image[] images;
    public Post(String denumire, String descriere, String location){
        this.denumire = denumire;
        this.descriere = descriere;
        this.location = location;
    }
    public Post(String denumire, String descriere, String location,Image[] images){
        this.denumire = denumire;
        this.descriere = descriere;
        this.location = location;
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public Image[] getImages() {
        return images;
    }

    public String getDenumire() {
        return denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public User getUser() {
        return user;
    }
}
