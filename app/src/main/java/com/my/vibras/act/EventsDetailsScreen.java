package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.RestaurantDetailAct;
import com.my.vibras.adapter.EventCommentAdapter;
import com.my.vibras.adapter.EventsImagesAdapter;
import com.my.vibras.adapter.MultipleImagesAdapter;
import com.my.vibras.adapter.RestaurantCommentAdapter;
import com.my.vibras.databinding.ActivityEventsDetailsScreenBinding;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResGetEventComment;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetEventComment;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.ImageCancelClick;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class EventsDetailsScreen extends AppCompatActivity {

    ActivityEventsDetailsScreenBinding binding;

    private SuccessResGetEvents.Result requestModel;

    private EventsImagesAdapter multipleImagesAdapter;

    private ArrayList<String> imagesList = new ArrayList<>();

    private VibrasInterface apiInterface;

    private EventCommentAdapter commentAdapter;

    private ArrayList<SuccessResGetEventComment.Result> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_events_details_screen);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
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
            startActivity(new Intent(EventsDetailsScreen.this,AllCommentsAct.class)
                    .putExtra("from","events").putExtra("id",requestModel.getId()));
        });

        binding.llPostComent.setOnClickListener(v -> {
            startActivity(new Intent(EventsDetailsScreen.this,PostCommentAct.class)
                    .putExtra("from","events").putExtra("id",requestModel.getId()));
        });

        Glide.with(EventsDetailsScreen.this)
                .load(requestModel.getImage())
                .into(binding.ivEvent);

        binding.tvEventName.setText(requestModel.getEventName());

        binding.tvEventDate.setText(parseDateToddMMyyyy(requestModel.getDateTimeEvent()));

        binding.tvEventTime.setText(requestModel.getEventStartTime());

        binding.eventLocation.setText(requestModel.getAddress());

        binding.tvDetails.setText(requestModel.getDescription());

        if(requestModel.getLikeStatus().equalsIgnoreCase("false"))
        {
            binding.ivLikes.setImageResource(R.drawable.ic_rest_unlike);
        }else
        {
            binding.ivLikes.setImageResource(R.drawable.ic_rest_like);
        }

        binding.tvLikesCount.setText(requestModel.getTotalLike()+"");
        binding.tvCommentCount.setText(requestModel.getTotalComments()+"");

        imagesList.clear();
                for(SuccessResGetEvents.EventGallery eventGallery:requestModel.getEventGallery())
                {
                    imagesList.add(eventGallery.getImageFile());
                }

        multipleImagesAdapter = new EventsImagesAdapter(EventsDetailsScreen.this, imagesList);
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(EventsDetailsScreen.this, LinearLayoutManager.HORIZONTAL,false));
        binding.rvImages.setAdapter(multipleImagesAdapter);

        commentAdapter = new EventCommentAdapter(EventsDetailsScreen.this,commentList);
        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(EventsDetailsScreen.this));
        binding.rvComments.setAdapter(commentAdapter);

        getComment();

        binding.llLikeEvent.setOnClickListener(v ->
                {
                    addLike(requestModel.getId());
                }
                );

    }

    private void addLike(String postId) {

        String userId = SharedPreferenceUtility.getInstance(EventsDetailsScreen.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(EventsDetailsScreen.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("event_id",postId);
        Call<SuccessResAddLike> call = apiInterface.addEventLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status+" : likeUnlike");

                    if(data.status==0)
                    {
                        binding.ivLikes.setImageResource(R.drawable.ic_rest_unlike);
                    }else
                    {
                        binding.ivLikes.setImageResource(R.drawable.ic_rest_like);
                    }

                    binding.tvLikesCount.setText(data.getTotalLikes());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getComment() {

        DataManager.getInstance().showProgressMessage(EventsDetailsScreen.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("event_id",requestModel.getId());

        Call<SuccessResGetEventComment> call = apiInterface.getEventComments(map);

        call.enqueue(new Callback<SuccessResGetEventComment>() {
            @Override
            public void onResponse(Call<SuccessResGetEventComment> call, Response<SuccessResGetEventComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEventComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentList.clear();
                        commentList.addAll(data.getResult());
                        commentAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        commentList.clear();
                        showToast(EventsDetailsScreen.this,data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetEventComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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