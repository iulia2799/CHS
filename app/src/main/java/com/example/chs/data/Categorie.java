package com.example.chs.data;

public class Categorie {
    private String Nume;
    private static Integer id=0;
    public Categorie(){

    }
    public Categorie(String nume){
        this.Nume = nume;
        id++;
    }
    public String getNume(){
        return Nume;
    }

    public void setNume(String nume) {
        this.Nume = nume;
    }
}
