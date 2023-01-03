package com.my.vibras.VideoCalling;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;

/**
 * Created by Ravindra Birla on 04,July,2022
 */
public class AppController extends Application   implements LifecycleObserver {
    private static Context context;
    private HttpProxyCacheServer proxy;

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



    public static HttpProxyCacheServer getProxy(Context context) {
        AppController app = (AppController) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
       /* return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(40)
                .cacheDirectory(getVideoCacheDir(this))
                .build();*/


        return   new HttpProxyCacheServer(this);

    }

    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }


   /* public static HttpProxyCacheServer shutDown(){

    }*/

    private HttpProxyCacheServer newProxy1() {
        //return new HttpProxyCacheServer(this);
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(getVideoCacheDir(this))
                .maxCacheFilesCount(40)
                .maxCacheSize(1024 * 1024 * 1024)
                .build();
    }



}