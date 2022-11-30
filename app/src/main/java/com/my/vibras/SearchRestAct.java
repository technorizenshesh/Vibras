package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.my.vibras.adapter.ViewAllRestaurentAdapter;
import com.my.vibras.databinding.ActivitySearchRestBinding;
import com.my.vibras.model.SuccessResMyRestaurantRes;
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

public class SearchRestAct extends AppCompatActivity {

    ActivitySearchRestBinding binding ;
    private ViewAllRestaurentAdapter myEventsAdapter;
    private VibrasInterface apiInterface;
    private ArrayList<SuccessResMyRestaurantRes.Result> eventsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_search_rest);
        binding.ivBack.setOnClickListener(v -> finish());

        apiInterface  = ApiClient.getClient().create(VibrasInterface.class);

        myEventsAdapter = new ViewAllRestaurentAdapter(SearchRestAct.this,eventsList);
        binding.rvEvents.setHasFixedSize(true);

        binding.rvEvents.setLayoutManager(new GridLayoutManager(SearchRestAct.this,2));

        binding.rvEvents.setAdapter(myEventsAdapter);

        binding.etSearch.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                getUsers(cs.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }

    private void getUsers(String title) {

        Map<String,String> map = new HashMap<>();

        map.put("search",title);

        Call<SuccessResMyRestaurantRes> call = apiInterface.searchRest(map);

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
                        showToast(SearchRestAct.this, data.message);
                        eventsList.clear();
                        myEventsAdapter.notifyDataSetChanged();
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


}