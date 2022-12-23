package com.my.vibras.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.SearchAdapter;
import com.my.vibras.adapter.SearchHistorySuggactionAdapter;
import com.my.vibras.adapter.SearchHistorySuggactionAdapter2;
import com.my.vibras.databinding.ActivitySearchBinding;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.model.SuccessSearchHistoryRes;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;
import com.my.vibras.utility.SuggactionClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.firebase.messaging.Constants.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class SearchAct extends AppCompatActivity implements SuggactionClick {

    ActivitySearchBinding binding;

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();
    private ArrayList<SuccessSearchHistoryRes.Result> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.ivBack.setOnClickListener(v -> finish());
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRfilter.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(SearchAct.this, FilterAct.class)));});
        binding.etSearch.requestFocus();
        binding.clearSearchHistory.setOnClickListener(v -> {removeAll();});
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.toString().trim().equalsIgnoreCase("")) {
                    binding.suggactionLay.setVisibility(View.VISIBLE);
                } else {
                    binding.suggactionLay.setVisibility(View.GONE);
                    getUsers(cs.toString());
                    //addSearchHistory(cs.toString());
                }}
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        getSearchHistory();
    }

    @Override
    protected void onResume() {
     /*  binding. etSearch.setText("");
        binding.suggactionLay.setVisibility(View.VISIBLE);
        binding.suggactionLay.setVisibility(View.GONE);*/

        super.onResume();
    }

    private void getUsers(String title) {

        String userId = SharedPreferenceUtility.getInstance(SearchAct.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("search", title);
        map.put("user_id", userId);
        Call<SuccessResGetUsers> call = apiInterface.searchUser(map);
        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        binding.rvUsers.setHasFixedSize(true);
                        binding.rvUsers.setLayoutManager(new LinearLayoutManager(SearchAct.this));
                        binding.rvUsers.setAdapter(new SearchAdapter(SearchAct.this, usersList,
                                true, title));
                    } else if (data.status.equals("0")) {
                        showToast(SearchAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getSearchHistory() {
        String userId = SharedPreferenceUtility.getInstance(SearchAct.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessSearchHistoryRes> call = apiInterface.search_history(map);
        call.enqueue(new Callback<SuccessSearchHistoryRes>() {
            @Override
            public void onResponse(Call<SuccessSearchHistoryRes> call, Response<SuccessSearchHistoryRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessSearchHistoryRes data = response.body();
                    String dataResponse = new Gson().toJson(response.body());

                    Log.e(TAG, "dataResponsedataResponsedataResponse: " + dataResponse);
                    if (data.getStatus().equals("1")) {
                        searchList.clear();
                        searchList.addAll(data.getResult());
                        binding.suggationList.setHasFixedSize(true);
                        binding.suggationList2.setHasFixedSize(true);
                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                        layoutManager.setFlexDirection(FlexDirection.ROW);
                        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                        binding.suggationList.setLayoutManager(layoutManager);
                        binding.suggationList2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.suggationList.setAdapter(
                                new SearchHistorySuggactionAdapter(SearchAct.this, searchList
                                        , SearchAct.this::suggactionClick));
                        binding.suggationList2.setAdapter(
                                new SearchHistorySuggactionAdapter2(SearchAct.this, searchList
                                        , SearchAct.this::suggactionClick));

                    } else if (data.getStatus().equals("0")) {
                        //showToast(SearchAct.this, data.message);
                         binding.suggactionLay.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessSearchHistoryRes> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void removeAll() {
        binding.suggactionLay.setVisibility(View.GONE);
        String userId = SharedPreferenceUtility.getInstance(getApplicationContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<ResponseBody> call = apiInterface.clear_search_history(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    getSearchHistory();
                  /*  JSONObject jsonObject = new JSONObject(response.body().string());
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void suggactionClick(SuccessSearchHistoryRes.Result r) {
        binding.etSearch.setText(r.getSearch());
       // getUsers(r.getSearch());
    }
}