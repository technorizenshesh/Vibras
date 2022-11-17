package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.MyRestaurantAdapter;
import com.my.vibras.databinding.ActivitySavedRestaurantBinding;
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

public class SavedRestaurantAct extends AppCompatActivity implements PostClickListener {

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResMyRestaurantRes.Result> eventsList = new ArrayList<>();

    private MyRestaurantAdapter myEventsAdapter;

    ActivitySavedRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_saved_restaurant);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> finish());
        myEventsAdapter = new MyRestaurantAdapter(SavedRestaurantAct.this,eventsList,this);
        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new LinearLayoutManager(SavedRestaurantAct.this));
        binding.rvRestaurants.setAdapter(myEventsAdapter);
        getMyRestaurantsApi();
    }

    public void getMyRestaurantsApi()
    {
        DataManager.getInstance().showProgressMessage(SavedRestaurantAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(SavedRestaurantAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(SavedRestaurantAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResMyRestaurantRes> call = apiInterface.getMyRestaurant(map);
        call.enqueue(new Callback<SuccessResMyRestaurantRes>() {
            @Override
            public void onResponse(Call<SuccessResMyRestaurantRes> call, Response<SuccessResMyRestaurantRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMyRestaurantRes data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(SavedRestaurantAct.this, data.message);
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