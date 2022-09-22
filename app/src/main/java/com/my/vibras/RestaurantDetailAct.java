package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.act.AllCommentsAct;

import com.my.vibras.act.PostCommentAct;
import com.my.vibras.adapter.EventsImagesAdapter;
import com.my.vibras.databinding.ActivityRestaurantDetailBinding;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetRestaurants;

import java.util.ArrayList;

public class RestaurantDetailAct extends AppCompatActivity {
    
    ActivityRestaurantDetailBinding binding;

    private SuccessResGetRestaurants.Result requestModel;

    private EventsImagesAdapter multipleImagesAdapter;

    private ArrayList<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_restaurant_detail);

        Intent in = getIntent();
        if (in!=null)
        {
            String result = in.getStringExtra("data");
            requestModel = new Gson().fromJson(result,SuccessResGetRestaurants.Result.class);
        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtViewAllComents.setOnClickListener(v -> {
            startActivity(new Intent(RestaurantDetailAct.this, AllCommentsAct.class));
        });

        binding.llPostComent.setOnClickListener(v -> {
            startActivity(new Intent(RestaurantDetailAct.this, PostCommentAct.class));
        });

        Glide.with(RestaurantDetailAct.this)
                .load(requestModel.getImage())
                .into(binding.ivRestaurant);

        binding.tvRestaurantName.setText(requestModel.getRestaurantName());

        binding.tvRestaurantLocation.setText(requestModel.getAddress());

        binding.tvDetails.setText(requestModel.getDescription());

        imagesList.clear();

        for(SuccessResGetRestaurants.RestaurantGallery eventGallery:requestModel.getRestaurantGallery())
        {
            imagesList.add(eventGallery.getImageFile());
        }

        multipleImagesAdapter = new EventsImagesAdapter(RestaurantDetailAct.this, imagesList);
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(RestaurantDetailAct.this, LinearLayoutManager.HORIZONTAL,false));
        binding.rvImages.setAdapter(multipleImagesAdapter);
        

    }
    
    
    
    
}