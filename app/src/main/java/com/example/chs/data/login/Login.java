package com.example.chs.data.login;

import android.widget.Button;
import android.widget.EditText;

public interface Login {
    //abstract void NewUser(String user, String password);
    abstract void SetPassword(String new_password);
    abstract void Setusername(String newusername);
    abstract void delete();
    //abstract void clickAuth(View view);
    //abstract void checkCred(String email,String password);
}
