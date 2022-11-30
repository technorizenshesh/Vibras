package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.AllGroupChatAdapter;
import com.my.vibras.adapter.ViewAllGroupChatAdapter;
import com.my.vibras.databinding.ActivityViewAllGroupsBinding;
import com.my.vibras.model.SuccessResGetGroup;
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

public class ViewAllGroupsAct extends AppCompatActivity {

    ActivityViewAllGroupsBinding binding;
    private VibrasInterface apiInterface;
    private String fromWhere = "";
    private String otherUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_all_groups);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> finish());
        fromWhere = getIntent().getStringExtra("from");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fromWhere.equalsIgnoreCase("all"))
        {
            getAllGroups();
        } else if(fromWhere.equalsIgnoreCase("my"))
        {
            getMyGroupList();
        }else if(fromWhere.equalsIgnoreCase("other"))
        {
            otherUserId = getIntent().getStringExtra("id");
            getOtherGroupList(otherUserId);
        }
    }

    private ArrayList<SuccessResGetGroup.Result> groupList = new ArrayList<>();
    AllGroupChatAdapter groupChatAdapter;
    private void getAllGroups() {
        Map<String,String> map = new HashMap<>();
        Call<SuccessResGetGroup> call = apiInterface.getAllGroupApi(map);
        call.enqueue(new Callback<SuccessResGetGroup>() {
            @Override
            public void onResponse(Call<SuccessResGetGroup> call, Response<SuccessResGetGroup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroup data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        groupList.clear();
                        groupList.addAll(data.getResult());
                        groupChatAdapter = new AllGroupChatAdapter(ViewAllGroupsAct.this,groupList);
                        binding.rvGrp.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllGroupsAct.this);
                        binding.rvGrp.setLayoutManager(new GridLayoutManager(ViewAllGroupsAct.this,2));
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvGrp.setAdapter(new ViewAllGroupChatAdapter(ViewAllGroupsAct.this,groupList));
                    } else if (data.status.equals("0")) {
                        showToast(ViewAllGroupsAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetGroup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getOtherGroupList(String userId) {
        DataManager.getInstance().showProgressMessage(ViewAllGroupsAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetGroup> call = apiInterface.getMygroupApi(map);
        call.enqueue(new Callback<SuccessResGetGroup>() {
            @Override
            public void onResponse(Call<SuccessResGetGroup> call, Response<SuccessResGetGroup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroup data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        groupList.clear();
                        groupList.addAll(data.getResult());
                        groupChatAdapter = new AllGroupChatAdapter(ViewAllGroupsAct.this,groupList);
                        binding.rvGrp.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllGroupsAct.this);
                        binding.rvGrp.setLayoutManager(new GridLayoutManager(ViewAllGroupsAct.this,2));
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvGrp.setAdapter(new ViewAllGroupChatAdapter(ViewAllGroupsAct.this,groupList));
                    } else if (data.status.equals("0")) {
                        showToast(ViewAllGroupsAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetGroup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getMyGroupList() {
        DataManager.getInstance().showProgressMessage(ViewAllGroupsAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(ViewAllGroupsAct.this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetGroup> call = apiInterface.getMygroupApi(map);
        call.enqueue(new Callback<SuccessResGetGroup>() {
            @Override
            public void onResponse(Call<SuccessResGetGroup> call, Response<SuccessResGetGroup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroup data = response.body();
                    String dataResponse = new Gson().toJson(response.body());

                    Log.e("data--------------------------------------",dataResponse);
                    if (data.status.equals("1")) {
                        groupList.clear();
                        groupList.addAll(data.getResult());
                        groupChatAdapter = new AllGroupChatAdapter(ViewAllGroupsAct.this,groupList);
                        binding.rvGrp.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllGroupsAct.this);
                        binding.rvGrp.setLayoutManager(new GridLayoutManager(ViewAllGroupsAct.this,2));
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvGrp.setAdapter(new ViewAllGroupChatAdapter(ViewAllGroupsAct.this,groupList));

                    } else if (data.status.equals("0")) {
                        showToast(ViewAllGroupsAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetGroup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }





}