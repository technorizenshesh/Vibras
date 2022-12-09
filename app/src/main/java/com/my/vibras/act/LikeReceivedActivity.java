package com.my.vibras.act;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.LikeRecivedAdapter;
import com.my.vibras.databinding.ActivityLikeReceivedBinding;
import com.my.vibras.model.SuccessFollowersRes;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class LikeReceivedActivity extends AppCompatActivity {

    ActivityLikeReceivedBinding binding;
    LikeRecivedAdapter mAdapter;
    private ArrayList<SuccessFollowersRes.Result> usersList = new ArrayList<>();
    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_like_received);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        try {
            String id = getIntent().getStringExtra("id");
            getAllUsers(id);
        } catch (Exception e) {

        }

        binding.RRFrnd.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void getAllUsers(String userId) {
        Log.e("TAG", "getAllUsers:  userIduserIduserIduserIduserIduserId " +userId);

        DataManager.getInstance().showProgressMessage(LikeReceivedActivity.this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessFollowersRes> call = apiInterface.get_followers(map);
        call.enqueue(new Callback<SuccessFollowersRes>() {
            @Override
            public void onResponse(Call<SuccessFollowersRes> call, Response<SuccessFollowersRes> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessFollowersRes data = response.body();
                    Log.e("data", data.getStatus());
                    if (data.getStatus().equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        mAdapter = new LikeRecivedAdapter(LikeReceivedActivity.this, usersList);
                        binding.rvFrnd.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                LikeReceivedActivity.this);
                        binding.rvFrnd.setLayoutManager(linearLayoutManager);
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvFrnd.setAdapter(mAdapter);

                    } else if (data.getStatus().equals("0")) {
                        showToast(LikeReceivedActivity.this, data.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessFollowersRes> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}