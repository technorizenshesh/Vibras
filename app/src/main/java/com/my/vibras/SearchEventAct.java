package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.my.vibras.act.FilterAct;
import com.my.vibras.act.SearchAct;
import com.my.vibras.adapter.MyEventsAdapter;
import com.my.vibras.adapter.SearchAdapter;
import com.my.vibras.databinding.ActivitySavedEventsBinding;
import com.my.vibras.databinding.ActivitySearchEventBinding;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.model.SuccessResMyEventRes;
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

public class SearchEventAct extends AppCompatActivity implements PostClickListener {

    ActivitySearchEventBinding binding;
    private VibrasInterface apiInterface;

    private ArrayList<SuccessResMyEventRes.Result> eventsList = new ArrayList<>();
    private MyEventsAdapter myEventsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_search_event);

        binding.ivBack.setOnClickListener(v -> finish());

        apiInterface  = ApiClient.getClient().create(VibrasInterface.class);

        myEventsAdapter = new MyEventsAdapter(SearchEventAct.this,eventsList,this);

        binding.rvEvents.setHasFixedSize(true);

        binding.rvEvents.setLayoutManager(new LinearLayoutManager(SearchEventAct.this));

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

        Call<SuccessResMyEventRes> call = apiInterface.searchEvent(map);

        call.enqueue(new Callback<SuccessResMyEventRes>() {
            @Override
            public void onResponse(Call<SuccessResMyEventRes> call, Response<SuccessResMyEventRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMyEventRes data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();
                        
                    } else if (data.status.equals("0")) {
                        showToast(SearchEventAct.this, data.message);
                        eventsList.clear();
                        myEventsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResMyEventRes> call, Throwable t) {
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