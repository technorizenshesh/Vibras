package com.my.vibras.VideoCalling;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by Ravindra Birla on 04,July,2022
 */
public class AppController extends Application   implements LifecycleObserver {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppController.context = getApplicationContext();
    }

    public static Context getContext() {
        return AppController.context;
    }
    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {


    }

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {



    }

}