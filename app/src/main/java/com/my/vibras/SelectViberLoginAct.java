package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.databinding.ActivitySelectViberLoginBinding;

public class SelectViberLoginAct extends AppCompatActivity {

    ActivitySelectViberLoginBinding binding;
    String loginType="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_select_viber_login);

        binding.cardLoginUser.setOnClickListener(v -> {
             loginType="user";
            binding.RadiobtnPlumber.setChecked(true);
            binding.RadiobtnUser.setChecked(false);
        });

        binding.cardLoginCompany.setOnClickListener(v -> {
             loginType="company";
            binding.RadiobtnPlumber.setChecked(false);
            binding.RadiobtnUser.setChecked(true);
        });

        binding.RRContinew.setOnClickListener(v -> {
            Intent intent=new Intent(SelectViberLoginAct.this,LoginAct.class).putExtra("loginType",loginType);
            startActivity(intent);
        });
    }
}