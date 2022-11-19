package com.my.vibras.VideoCalling;


import android.content.Context;

/**
 * Created by Ravindra Birla on 04,July,2022
 */
public class AppController extends android.app.Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppController.context = getApplicationContext();
    }

    public static Context getContext() {
        return AppController.context;
    }


}