package com.example.chs.data.login;

import com.example.chs.data.model.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Login {
    private String username;
    private String email;
    private String password;
    private List<Alert> alertList = new ArrayList<>();
    private int points;
    private String informatii;
    public User(){

    }
    public User(String email,String password){
        //if user does not exist in json then do this
        this.email = email;
        this.password = password;
        this.informatii = "Scrie datele de contact aici";
        this.points=0;
    }
    public User(Map<String,String> user){
        this.username = user.get("username");
        this.password = user.get("password");
        this.email = user.get("email");

    }
    public User(String email,String password,List<Alert> alerts){
        //if user does not exist in json then do this
        this.email = email;
        this.password = password;
        for(Alert a : alerts){
            this.alertList.add(a);
        }
        this.informatii = "Scrie datele de contact aici";
        this.points=0;
    }
    public User(String email,String username,String password){
        this.email = email;
        this.username = username;
        this.password = password;
        this.informatii = "Scrie datele de contact aici";
        this.points=0;
    }
    public User(String email,String username,String password,List<Alert> alerts){
        this.email = email;
        this.username = username;
        this.password = password;
        for(Alert a : alerts){
            this.alertList.add(a);
        }
        this.informatii = "Scrie datele de contact aici";
        this.points=0;
    }

    public String getEmail(){return this.email;}
    @Override
    public void SetPassword(String new_password) {
        this.password=new_password;
    }

    @Override
    public void Setusername(String newusername) {
        this.username = newusername;
    }



    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }



    public String getInformatii(){
        return this.informatii;
    }

    public void setInformatii(String newinfo){
        this.informatii = newinfo;
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<Alert> alerts) {
        for(Alert a : alerts){
            this.alertList.add(a);
        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
