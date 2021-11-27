package com.example.chs.data.login;

public class User implements Login {
    private String username;
    private String email;
    private String password;
    public User(){

    }
    public User(String email,String password){
        //if user does not exist in json then do this
        this.email = email;
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
}
