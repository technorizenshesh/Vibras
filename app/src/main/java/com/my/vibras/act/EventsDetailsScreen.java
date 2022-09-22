package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.EventsImagesAdapter;
import com.my.vibras.adapter.MultipleImagesAdapter;
import com.my.vibras.databinding.ActivityEventsDetailsScreenBinding;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.utility.ImageCancelClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventsDetailsScreen extends AppCompatActivity {

    ActivityEventsDetailsScreenBinding binding;

    private SuccessResGetEvents.Result requestModel;

    private EventsImagesAdapter multipleImagesAdapter;

    private ArrayList<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_events_details_screen);

        Intent in = getIntent();
        if (in!=null)
        {
            String result = in.getStringExtra("data");
            requestModel = new Gson().fromJson(result,SuccessResGetEvents.Result.class);
        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtViewAllComents.setOnClickListener(v -> {
            startActivity(new Intent(EventsDetailsScreen.this,AllCommentsAct.class));
        });

        binding.llPostComent.setOnClickListener(v -> {
            startActivity(new Intent(EventsDetailsScreen.this,PostCommentAct.class));
        });

        Glide.with(EventsDetailsScreen.this)
                .load(requestModel.getImage())
                .into(binding.ivEvent);

        binding.tvEventName.setText(requestModel.getEventName());

        binding.tvEventDate.setText(parseDateToddMMyyyy(requestModel.getDateTimeEvent()));

        binding.tvEventTime.setText(requestModel.getEventStartTime());

        binding.eventLocation.setText(requestModel.getAddress());

        binding.tvDetails.setText(requestModel.getDescription());

        imagesList.clear();

                for(SuccessResGetEvents.EventGallery eventGallery:requestModel.getEventGallery())
                {
                    imagesList.add(eventGallery.getImageFile());
                }

        multipleImagesAdapter = new EventsImagesAdapter(EventsDetailsScreen.this, imagesList);
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(EventsDetailsScreen.this, LinearLayoutManager.HORIZONTAL,false));
        binding.rvImages.setAdapter(multipleImagesAdapter);

    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "MMM d, yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }





}