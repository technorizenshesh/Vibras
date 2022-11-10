package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityGoodVibesBinding;

public class GoodVibesAct extends AppCompatActivity {

    ActivityGoodVibesBinding binding;

    private String userName = "",image="",id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this,R.layout.activity_good_vibes);
    binding.ivBack.setOnClickListener(v -> finish());



    }

}