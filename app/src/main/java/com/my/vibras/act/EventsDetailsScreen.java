package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityEventsDetailsScreenBinding;

public class EventsDetailsScreen extends AppCompatActivity {

    ActivityEventsDetailsScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_events_details_screen);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtViewAllComents.setOnClickListener(v -> {
            startActivity(new Intent(EventsDetailsScreen.this,AllCommentsAct.class));
        });

        binding.llPostComent.setOnClickListener(v -> {
            startActivity(new Intent(EventsDetailsScreen.this,PostCommentAct.class));
        });

    }
}