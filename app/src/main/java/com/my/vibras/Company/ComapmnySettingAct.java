package com.my.vibras.Company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.SelectViberLoginAct;
import com.my.vibras.act.ChangePassAct;
import com.my.vibras.act.NotificationScreenAct;
import com.my.vibras.act.PaymentsAct;
import com.my.vibras.act.SettingAct;
import com.my.vibras.act.TransactionAct;
import com.my.vibras.databinding.ActivityComapmnySettingBinding;

public class ComapmnySettingAct extends AppCompatActivity {

    ActivityComapmnySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_comapmny_setting);

        binding.RRpayment.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, PaymentsAct.class));
        });

        binding.RRtrasaction.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, TransactionAct.class));
        });

        binding.RRNotification.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, NotificationScreenAct.class));
        });

        binding.RRpasword.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, ChangePassAct.class));
        });

        binding.RRSignOut.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, SelectViberLoginAct.class));
            finish();
        });
    }
}