package com.example.chs.data.model;

public class Act {
    private String link;
    private String nume;
    public Act(){

    }
    public Act(String link){
        this.link = link;
    }
    public Act(String nume,String link){
        this.nume = nume;
        this.link = link;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNume() {
        return nume;
    }

    public String getLink() {
        return link;
    }
}
