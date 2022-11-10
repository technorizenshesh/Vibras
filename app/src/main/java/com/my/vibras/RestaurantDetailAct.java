package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import com.my.vibras.act.AllCommentsAct;

import com.my.vibras.act.EventsDetailsScreen;
import com.my.vibras.act.PostCommentAct;
import com.my.vibras.adapter.CommentRestaurantAdapter;
import com.my.vibras.adapter.EventsImagesAdapter;
import com.my.vibras.adapter.RestaurantCommentAdapter;
import com.my.vibras.databinding.ActivityRestaurantDetailBinding;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetRestaurantComment;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class RestaurantDetailAct extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    ActivityRestaurantDetailBinding binding;
    private VibrasInterface apiInterface;
    private SuccessResGetRestaurants.Result requestModel;
    private EventsImagesAdapter multipleImagesAdapter;
    private RestaurantCommentAdapter commentAdapter;
    private ArrayList<String> imagesList = new ArrayList<>();
    private ArrayList<SuccessResGetRestaurantComment.Result> commentList = new ArrayList<>();
    String strLat = "",strLng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_restaurant_detail);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
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
           startActivity(new Intent(RestaurantDetailAct.this,AllCommentsAct.class)
                    .putExtra("from","rest").putExtra("id",requestModel.getId()));
        });

        binding.llPostComent.setOnClickListener(v -> {
            startActivity(new Intent(RestaurantDetailAct.this, PostCommentAct.class)
                    .putExtra("from","restaurant").putExtra("id",requestModel.getId()));
        });

        binding.tvLikeCount.setText(requestModel.getResCountLike()+"");
        binding.tvCommentCount.setText(requestModel.getTotalComments()+"");
        if(requestModel.getLikeStatus().equalsIgnoreCase("false"))
        {
            binding.ivLike.setImageResource(R.drawable.ic_rest_unlike);
        }else
        {
            binding.ivLike.setImageResource(R.drawable.ic_rest_like);
        }

        Glide.with(RestaurantDetailAct.this)
                .load(requestModel.getImage())
                .into(binding.ivRestaurant);
        binding.tvRestaurantName.setText(requestModel.getRestaurantName());
        binding.tvRestaurantLocation.setText(requestModel.getAddress());
        binding.tvDetails.setText(requestModel.getDescription());
        imagesList.clear();

        strLat = requestModel.getLat();
        strLng = requestModel.getLat();


        for(SuccessResGetRestaurants.RestaurantGallery eventGallery:requestModel.getRestaurantGallery())
        {
           imagesList.add(eventGallery.getImageFile());
        }
        multipleImagesAdapter = new EventsImagesAdapter(RestaurantDetailAct.this, imagesList);
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(RestaurantDetailAct.this, LinearLayoutManager.HORIZONTAL,false));
        binding.rvImages.setAdapter(multipleImagesAdapter);
        commentAdapter = new RestaurantCommentAdapter(RestaurantDetailAct.this,commentList);
        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(RestaurantDetailAct.this));
        binding.rvComments.setAdapter(commentAdapter);
        getComment();

        binding.llLike.setOnClickListener(v ->
                {
                    addLike(requestModel.getId());
                }
                );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(RestaurantDetailAct.this);

    }

    private void addLike(String postId) {

        String userId = SharedPreferenceUtility.getInstance(RestaurantDetailAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(RestaurantDetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("restaurant_id",postId);
        Call<SuccessResAddLike> call = apiInterface.addRestaurantLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status+"");
                    if(data.status==0)
                    {
                        binding.ivLike.setImageResource(R.drawable.ic_rest_unlike);
                    }else
                    {
                        binding.ivLike.setImageResource(R.drawable.ic_rest_like);
                    }

                    binding.tvLikeCount.setText(data.getTotalLikes());
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
        DataManager.getInstance().showProgressMessage(RestaurantDetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("restaurant_id",requestModel.getId());
        Call<SuccessResGetRestaurantComment> call = apiInterface.getRestaurantComments(map);
        call.enqueue(new Callback<SuccessResGetRestaurantComment>() {
            @Override
            public void onResponse(Call<SuccessResGetRestaurantComment> call, Response<SuccessResGetRestaurantComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetRestaurantComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentList.clear();
                        commentList.addAll(data.getResult());
                        commentAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        commentList.clear();
                        showToast(RestaurantDetailAct.this,data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetRestaurantComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            gMap = googleMap;
            double  lat = Double.parseDouble(strLat);
            double  lng = Double.parseDouble(strLng);
            LatLng sydney = new LatLng(lat, lng);
            gMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Marker"));
            //   gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();

            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }catch (Exception e)
        {
            Log.d("TAG", "onMapReady: "+e);
        }


    }


}