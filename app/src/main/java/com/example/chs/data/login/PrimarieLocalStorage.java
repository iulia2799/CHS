package com.example.chs.data.login;

import android.content.Context;
import android.content.SharedPreferences;

public class PrimarieLocalStorage {
    public static final String SP_NAME = "primarieDetails";
    SharedPreferences primarieLocalDatabase;
    public PrimarieLocalStorage(Context context){
        primarieLocalDatabase = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }
    public void storeUserData(User user){
        SharedPreferences.Editor speditor = primarieLocalDatabase.edit();
        speditor.putString("email",user.getEmail());
        speditor.putString("password",user.getPassword());
        speditor.commit();
    }

    public User getLoggedInUser(){
        String email = primarieLocalDatabase.getString("email","");
        String pass = primarieLocalDatabase.getString("password","");
        User storedUser = new User(email,pass);
        return storedUser;
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor speditor = primarieLocalDatabase.edit();
        speditor.putBoolean("loggedIn",loggedIn);
        speditor.commit();
    }
    public void clearUserData(){
        SharedPreferences.Editor speditor = primarieLocalDatabase.edit();
        speditor.clear();
        speditor.commit();
    }
    public boolean getUserLoggedIn(){
        if(primarieLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        }else{
            return false;
        }
    }
}
