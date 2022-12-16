package com.my.vibras.act;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityHelpContactBinding;

public class HelpContactActivity extends AppCompatActivity {
    ActivityHelpContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_help_contact);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });


    }
}