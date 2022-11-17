package com.my.vibras.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.my.vibras.LoginAct;
public class Session extends Object {
    private static final String TAG = Session.class.getSimpleName();
    private static final String PREF_NAME = "Rapidine_pref2";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String FormStatus = "formstatus";
    private static final String Mobile = "mobile";
    private static final String UserId = "user_id";
    private static final String User_name = "user_name";
    private static final String FireBaseToken = "fcmid";
    private static final String HOME_LAT = "home_lat";
    private static final String HOME_LONG = "home_long";
    private static final String ChatName = "chat_name";
    private static final String ChatImage = "chat_image";
    private Context _context;
    private SharedPreferences Rapidine_pref;
    private SharedPreferences.Editor editor;

    public Session(Context context) {
        this._context = context;
        Rapidine_pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = Rapidine_pref.edit();
        editor.apply();
    }




    public String getMobile() {
        return Rapidine_pref.getString(Mobile, "");

    }


    public String getUser_name() {
        return Rapidine_pref.getString(User_name, "");

    }

    public void setUser_name(String user_name) {
        editor.putString(User_name, user_name);
        this.editor.apply();
    }



    public String getChatName() {
        return Rapidine_pref.getString(ChatName, "");

    }

    public void setChatName(String user_name) {
        editor.putString(ChatName, user_name);
        this.editor.apply();
    }

    public String getChatImage() {
        return Rapidine_pref.getString(ChatImage, "");

    }

    public void setChatImage(String user_name) {
        editor.putString(ChatImage, user_name);
        this.editor.apply();
    }


    public String get_FormStatus() {
        return Rapidine_pref.getString(FormStatus, "");
    }

    public void set_FormStatus(String fm) {
        editor.putString(FormStatus, fm);
        editor.apply();
        editor.commit();
    }

    public String getUserId() {
        return Rapidine_pref.getString(UserId, "");
    }

    public void setUserId(String userId) {
        editor.putString(UserId, userId);
        this.editor.apply();
    }

    public String getHOME_LAT() {
        return Rapidine_pref.getString(HOME_LAT, "");
    }

    public void setHOME_LAT(String userId) {
        editor.putString(HOME_LAT, userId);
        this.editor.apply();
    }

    public String getHOME_LONG() {
        return Rapidine_pref.getString(HOME_LONG, "");
    }

    public void setHOME_LONG(String userId) {
        editor.putString(HOME_LONG, userId);
        this.editor.apply();
    }


    public void logout() {
        editor.clear();
        editor.apply();
        Intent showLogin = new Intent(_context, LoginAct.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }


    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return Rapidine_pref.getBoolean(IS_LOGGEDIN, false);
    }

    public String getFireBaseToken() {
        return Rapidine_pref.getString(FireBaseToken, "");
    }

    public void setFireBaseToken(String fcmid) {
        editor.putString(FireBaseToken, fcmid);
        this.editor.apply();
    }

}
