package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.ViewAllEventstAdapter;
import com.my.vibras.databinding.ActivityGetEventsByCategoryIdBinding;
import com.my.vibras.model.SuccessResGetEvents;
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

public class GetEventsByCategoryIdAct extends AppCompatActivity {

    ActivityGetEventsByCategoryIdBinding binding;

    private VibrasInterface apiInterface;
    private ViewAllEventstAdapter myEventsAdapter;
    private ArrayList<SuccessResGetEvents.Result> eventsList = new ArrayList<>();
    private String categoryId="",categoryName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_get_events_by_category_id);
        categoryId = getIntent().getExtras().getString("id");
        categoryName = getIntent().getExtras().getString("name");

        binding.RRback.setOnClickListener(v -> finish());

        binding.txtName.setText(""+categoryName);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        myEventsAdapter = new ViewAllEventstAdapter(GetEventsByCategoryIdAct.this,eventsList);
        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        binding.rvRestaurants.setAdapter(myEventsAdapter);

        getEventsByCategory();

    }

    public void getEventsByCategory()
    {
        DataManager.getInstance().showProgressMessage(GetEventsByCategoryIdAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("event_category",categoryId);
        Call<SuccessResGetEvents> call = apiInterface.getEventsByCategory(map);
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
                        showToast(GetEventsByCategoryIdAct.this, data.message);
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