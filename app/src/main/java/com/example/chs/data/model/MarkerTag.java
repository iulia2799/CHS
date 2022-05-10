package com.example.chs.data.model;

import com.example.chs.data.Post;
import com.google.android.gms.maps.model.Marker;

public class MarkerTag {
    private Post mPost;
    private String url;
    public MarkerTag(){

    }
    public MarkerTag(Post p){
        this.mPost=p;
    }
    public MarkerTag(Post p, String url){
        this.mPost=p;
        this.url =url;
    }

    public Post getmPost() {
        return mPost;
    }

    public String getUrl() {
        return url;
    }

    public void setmPost(Post mPost) {
        this.mPost = mPost;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
