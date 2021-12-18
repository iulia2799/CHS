package com.example.chs.data.login;

public class Primarie implements Login{
    private String primarie;
    private String password;
    private String location;
    private String email;

    public Primarie(){

    }
    public Primarie(String email, String password){
        this.email =email;
        this.password =password;
    }
    public Primarie(String nume, String password, String location){
        // if user does not exits in json
        this.primarie=nume;
        this.password = password;
        this.location = location;
    }
    @Override
    public void SetPassword(String new_password) {
        this.password = new_password;
    }

    @Override
    public void Setusername(String newusername) {
        this.primarie = newusername;
    }

    @Override
    public void delete() {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPrimarie() {
        return primarie;
    }
}
