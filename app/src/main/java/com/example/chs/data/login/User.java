package com.example.chs.data.login;

import java.util.ArrayList;
import java.util.List;

public class User implements Login {
    private String username;
    private String email;
    private String password;
    private List<String> notifications = new ArrayList<>();
    public User(){

    }
    public User(String email,String password){
        //if user does not exist in json then do this
        this.email = email;
        this.password = password;
    }
    public User(String email,String username,String password){
        this.email = email;
        this.username = username;
        this.password = password;
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

    public List<String> getNotifications() {
        return notifications;
    }
    public void setNotifications(String notification){
        notifications.add(notification);
    }
}
