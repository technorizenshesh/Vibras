package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityPostCommentBinding;

public class PostCommentAct extends AppCompatActivity {

    ActivityPostCommentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_post_comment);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}