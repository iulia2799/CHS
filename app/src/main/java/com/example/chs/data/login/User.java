package com.example.chs.data.login;

public class User implements Login {
    private String username;
    private String password;
    public User(){

    }
    public User(String username,String password){
        //if user does not exist in json then do this
        this.username = username;
        this.password = password;
    }
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
}
