package com.example.chs.data;

public class Categorie {
    private String nume;
    public Categorie(){

    }
    public Categorie(String nume){
        this.nume = nume;
    }
    public String getNume(){
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}
