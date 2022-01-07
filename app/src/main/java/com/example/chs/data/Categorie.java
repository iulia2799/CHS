package com.example.chs.data;

public class Categorie {
    private String nume;
    private static Integer id=0;
    public Categorie(){

    }
    public Categorie(String nume){
        this.nume = nume;
        id++;
    }
    public String getNume(){
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}
