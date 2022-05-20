package com.example.chs.data.login;

import android.content.Context;
import android.content.SharedPreferences;

public class PrimarieLocalStorage {
    public static final String SP_NAME = "primarieDetails";
    SharedPreferences primarieLocalDatabase;
    public PrimarieLocalStorage(Context context){
        primarieLocalDatabase = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }
    public void storeUserData(Primarie user){
        SharedPreferences.Editor speditor = primarieLocalDatabase.edit();
        speditor.putString("email",user.getEmail());
        speditor.putString("username",user.getPrimarie());
        speditor.putString("password",user.getPassword());
        speditor.putString("location",user.getLocation());
        speditor.commit();
    }

    public Primarie getLoggedInUser(){
        String email = primarieLocalDatabase.getString("email","");
        String username = primarieLocalDatabase.getString("username","");
        String pass = primarieLocalDatabase.getString("password","");
        String location = primarieLocalDatabase.getString("location","");
        Primarie storedUser = new Primarie(email,username,location,pass);
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
