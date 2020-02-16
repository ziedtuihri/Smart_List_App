package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String FIRSTNAME = "FIRSTNAME";
    public static final String EMAIL = "EMAIL";
    public static final String TEL = "TEL";
    public static final String LASTNAME = "LASTNAME";
    public static final String DATEOFBIRTH = "DATEOFBIRTH";
    public static final String LOCATION = "LOCATION";
    public static final String TOKEN = "TOKEN";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession( String firstname, String email, String lastname ,String token ,String tel ,String dateofbirth, String location){

        editor.putBoolean(LOGIN, true);
        editor.putString(FIRSTNAME, firstname);
        editor.putString(TOKEN, token);
        editor.putString(LASTNAME, lastname);
        editor.putString(DATEOFBIRTH, dateofbirth);
        editor.putString(LOCATION, location);
        editor.putString(EMAIL, email);
        editor.putString(TEL, tel);
        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((Activity_Profile) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(FIRSTNAME, sharedPreferences.getString(FIRSTNAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(TEL, sharedPreferences.getString(TEL, null));
        user.put(LASTNAME, sharedPreferences.getString(LASTNAME, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        user.put(DATEOFBIRTH, sharedPreferences.getString(DATEOFBIRTH, null));
        user.put(LOCATION, sharedPreferences.getString(LOCATION, null));

        return user;
    }

    public void logout(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((Activity_Profile) context).finish();

    }

}