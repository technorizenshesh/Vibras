package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityFilterBinding;

public class FilterAct extends AppCompatActivity {

    ActivityFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding= DataBindingUtil.setContentView(this,R.layout.activity_filter);

         binding.RRback.setOnClickListener(v -> {
             onBackPressed();
         });

    }
}