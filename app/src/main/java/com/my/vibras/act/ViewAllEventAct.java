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
import com.my.vibras.SearchEventAct;
import com.my.vibras.SearchRestAct;
import com.my.vibras.adapter.ViewAllEventstAdapter;
import com.my.vibras.adapter.ViewAllRestaurentAdapter;
import com.my.vibras.databinding.ActivityViewAllEventBinding;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetEvents;
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

public class ViewAllEventAct extends AppCompatActivity {
    
    VibrasInterface apiInterface;
    private ViewAllEventstAdapter myEventsAdapter;
    private ArrayList<SuccessResGetEvents.Result> eventsList = new ArrayList<>();
    ActivityViewAllEventBinding binding;
    private String fromWhere="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_all_event);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        myEventsAdapter = new ViewAllEventstAdapter(ViewAllEventAct.this,eventsList);
        binding.RRback.setOnClickListener(v -> finish());
        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        binding.rvRestaurants.setAdapter(myEventsAdapter);
        binding.RRSearch.setOnClickListener(v ->
                {
                    startActivity(new Intent(ViewAllEventAct.this, SearchEventAct.class));
                }
        );

        fromWhere = getIntent().getStringExtra("from");

        if(fromWhere.equalsIgnoreCase("events"))
        {
            getMyRestaurantsApi();
        }else  if(fromWhere.equalsIgnoreCase("my"))
        {
            getMyEventsApis();
            binding.RRSearch.setVisibility(View.GONE);
        }

    }

    public void getMyRestaurantsApi()
    {
        DataManager.getInstance().showProgressMessage(ViewAllEventAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(ViewAllEventAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ViewAllEventAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","");
        map.put("lon","");
        Call<SuccessResGetEvents> call = apiInterface.getEvents(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(ViewAllEventAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getMyEventsApis()
    {
        DataManager.getInstance().showProgressMessage(ViewAllEventAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(ViewAllEventAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ViewAllEventAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetEvents> call = apiInterface.getMyJoinedEvents(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(ViewAllEventAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}