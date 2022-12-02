package com.my.vibras.Company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.SelectViberLoginAct;
import com.my.vibras.act.ChangePassAct;
import com.my.vibras.act.HelpAct;
import com.my.vibras.act.NotificationScreenAct;
import com.my.vibras.act.PrivacyPolicyAct;
import com.my.vibras.act.SavedEventsAct;
import com.my.vibras.act.SavedRestaurantAct;
import com.my.vibras.act.SettingAct;
import com.my.vibras.act.TransactionAct;
import com.my.vibras.databinding.ActivityComapmnySettingBinding;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.utility.SharedPreferenceUtility;

public class ComapmnySettingAct extends AppCompatActivity {

    ActivityComapmnySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_comapmny_setting);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRpayment.setOnClickListener(v -> {
//            startActivity(new Intent(ComapmnySettingAct.this, PaymentsAct.class));
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

        binding.RRPP.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, PrivacyPolicyAct.class)
                    .putExtra("from","company"));
        });

        binding.RRFAQ.setOnClickListener(v ->
                {
                    startActivity(new Intent(ComapmnySettingAct.this, HelpAct.class));
                }
        );

        binding.RRpasword.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, ChangePassAct.class));
        });

        binding.rlRestaurant.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, SavedRestaurantAct.class));
        });

        binding.rlEvents.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, SavedEventsAct.class));
        });

        binding.RRSignOut.setOnClickListener(v -> {
           /* startActivity(new Intent(ComapmnySettingAct.this, SelectViberLoginAct.class));
            finish();*/
            SharedPreferenceUtility.getInstance(getApplicationContext())
                    .putBoolean(Constant.IS_USER_LOGGED_IN, false);
            Intent intent = new Intent(ComapmnySettingAct.this,
                    SelectViberLoginAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

}