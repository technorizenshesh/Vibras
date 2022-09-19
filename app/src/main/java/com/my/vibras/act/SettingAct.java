package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.SelectViberLoginAct;
import com.my.vibras.databinding.ActivitySettingBinding;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.utility.SharedPreferenceUtility;

public class SettingAct extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);

        binding.RRtrasaction.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,TransactionAct.class));
        });

        binding.RRAbout.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,TermsCondition.class));
        });

        binding.RRprivacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,TermsCondition.class));
        });

        binding.RRcSubsCription.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,PaymentsAct.class));
        });

        binding.RRchangePassword.setOnClickListener(v ->
                {
                    startActivity(new Intent(SettingAct.this,ChangePassAct.class));
                }
                );

        binding.tvLogout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent intent = new Intent(SettingAct.this,
                            SelectViberLoginAct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                );

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}