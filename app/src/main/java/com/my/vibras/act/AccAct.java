package com.my.vibras.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.SearchRestAct;
import com.my.vibras.adapter.NEarmeAccAdapter;
import com.my.vibras.adapter.ViewAllRestaurentAdapter;
import com.my.vibras.databinding.ActivityAccBinding;
import com.my.vibras.databinding.ActivityRestaurantBinding;
import com.my.vibras.model.SuccessAccList;
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
import static io.agora.rtc.gl.VideoFrame.TextureBuffer.TAG;

public class AccAct extends AppCompatActivity implements PostClickListener {
    ActivityAccBinding binding;
    VibrasInterface apiInterface;
    private ArrayList<SuccessResMyRestaurantRes.Result> eventsList = new ArrayList<>();
    private ArrayList<SuccessAccList.Result> resultArrayList = new ArrayList<>();
    NEarmeAccAdapter nEarmeAccAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_acc);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRSearch.setOnClickListener(v ->
                {
                    startActivity(new Intent(AccAct.this, SearchRestAct.class));
                }
                );
        nEarmeAccAdapter = new NEarmeAccAdapter(getApplicationContext(), resultArrayList);
        binding.recycleAcc.setHasFixedSize(true);
        binding.recycleAcc.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.HORIZONTAL, false));
        binding.recycleAcc.setAdapter(nEarmeAccAdapter);
        getMyRestaurantsApi();
    }

    public void getMyRestaurantsApi()
    {
        DataManager.getInstance().showProgressMessage(AccAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(AccAct.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lat", "");
        map.put("lon","");
        Log.e(TAG, "getRestaurants:   getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+map );
        Call<SuccessAccList> call = apiInterface.getAccomadtion(map);
        call.enqueue(new Callback<SuccessAccList>() {
            @Override
            public void onResponse(Call<SuccessAccList> call, Response<SuccessAccList> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessAccList data = response.body();
                    Log.e("data", data.getStatus());
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+ dataResponse);

                    if (data.getStatus().equals("1")) {
                        resultArrayList.clear();
                        resultArrayList.addAll(data.getResult());
                        nEarmeAccAdapter.notifyDataSetChanged();
                    } else if (data.getStatus().equals("0")) {
                        showToast(AccAct.this, data.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessAccList> call, Throwable t) {
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