package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityNotificationScreenBinding;

public class NotificationScreenAct extends AppCompatActivity {

    ActivityNotificationScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_notification_screen);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}