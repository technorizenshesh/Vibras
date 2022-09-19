package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivitySubsCriptionBinding;

public class SubsCriptionAct extends AppCompatActivity {

    ActivitySubsCriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_subs_cription);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.llBasic.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class));
        });

        binding.llSTANDARD.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class));
        });

        binding.llSPREMIUM.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class));
        });
    }
}