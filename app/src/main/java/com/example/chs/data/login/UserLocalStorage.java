package com.example.chs.data.login;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStorage {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStorage(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }
    public void storeUserData(User user){
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        speditor.putString("email",user.getEmail());
        speditor.putString("password",user.getPassword());
        speditor.commit();
    }

    public User getLoggedInUser(){
        String email = userLocalDatabase.getString("email","");
        String pass = userLocalDatabase.getString("password","");
        User storedUser = new User(email,pass);
        return storedUser;
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        speditor.putBoolean("loggedIn",loggedIn);
        speditor.commit();
    }
    public void clearUserData(){
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        speditor.clear();
        speditor.commit();
    }
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        }else{
            return false;
        }
    }
}
