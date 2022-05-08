package com.example.chs.data.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Login {
    private String username;
    private String email;
    private String password;
    private HashMap<String,String> notifications = new HashMap<>();
    private String informatii;
    public User(){

    }
    public User(String email,String password){
        //if user does not exist in json then do this
        this.email = email;
        this.password = password;
        this.informatii = "";
    }
    public User(String email,String password,HashMap<String,String> list){
        //if user does not exist in json then do this
        this.email = email;
        this.password = password;
        this.notifications = list;
        this.informatii = "";
    }
    public User(String email,String username,String password){
        this.email = email;
        this.username = username;
        this.password = password;
        this.informatii = "";
    }
    public User(String email,String username,String password,HashMap<String,String> list){
        this.email = email;
        this.username = username;
        this.password = password;
        this.notifications = list;
        this.informatii = "";
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

    @Override
    public void delete() {

    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }

    public HashMap<String, String> getNotifications() {
        return notifications;
    }

    public void setNotifications(HashMap<String, String> notifications) {
        this.notifications = notifications;
    }

    public String getInformatii(){
        return this.informatii;
    }

    public void setInformatii(String newinfo){
        this.informatii = newinfo;
    }
}
