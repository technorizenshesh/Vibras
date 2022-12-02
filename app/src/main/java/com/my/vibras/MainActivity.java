package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.act.HomeUserAct;
import com.my.vibras.databinding.ActivityMainBinding;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.utility.SharedPreferenceUtility;

import static com.my.vibras.retrofit.Constant.USER_TYPE;

public class MainActivity extends AppCompatActivity {

    private boolean isUserLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isUserLoggedIn = SharedPreferenceUtility.getInstance(MainActivity.this).getBoolean(Constant.IS_USER_LOGGED_IN);

        handlerMethod();
    }

    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isUserLoggedIn) {
                    String type = SharedPreferenceUtility.getInstance(MainActivity.this).getString(USER_TYPE);
                    if(type.equalsIgnoreCase("user"))
                    {
                        startActivity(new Intent(MainActivity.this, HomeUserAct.class));
                        finish();
                    }else
                    {
                        startActivity(new Intent(MainActivity.this, HomeComapnyAct.class));
                        finish();
                    }
                } else {
                    Intent intent=new Intent(MainActivity.this, SelectViberLoginAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}