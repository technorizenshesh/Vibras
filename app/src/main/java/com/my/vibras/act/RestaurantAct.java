package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.SearchRestAct;
import com.my.vibras.adapter.ViewAllRestaurentAdapter;
import com.my.vibras.databinding.ActivityRestaurantBinding;
import com.my.vibras.model.SuccessResMyRestaurantRes;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.PostClickListener;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class RestaurantAct extends AppCompatActivity implements PostClickListener {
    ActivityRestaurantBinding binding;
    VibrasInterface apiInterface;
    private ViewAllRestaurentAdapter myEventsAdapter;
    private ArrayList<SuccessResMyRestaurantRes.Result> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_restaurant);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        myEventsAdapter = new ViewAllRestaurentAdapter(RestaurantAct.this,eventsList);
        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        binding.rvRestaurants.setAdapter(myEventsAdapter);
        binding.RRSearch.setOnClickListener(v ->
                {
                    startActivity(new Intent(RestaurantAct.this, SearchRestAct.class));
                }
                );

        getMyRestaurantsApi();
    }

    public void getMyRestaurantsApi()
    {
        DataManager.getInstance().showProgressMessage(RestaurantAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(RestaurantAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(RestaurantAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","");
        map.put("lon","");
        Call<SuccessResMyRestaurantRes> call = apiInterface.getNearbyRestaurnat(map);
        call.enqueue(new Callback<SuccessResMyRestaurantRes>() {
            @Override
            public void onResponse(Call<SuccessResMyRestaurantRes> call, Response<SuccessResMyRestaurantRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMyRestaurantRes data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(RestaurantAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResMyRestaurantRes> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void selectLike(int position, String status) {
        
    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position) {

    }

    @Override
    public void savePost(View param1, String postID, boolean isUser, int position) {

    }

}