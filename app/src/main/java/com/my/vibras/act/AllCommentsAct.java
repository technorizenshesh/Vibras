package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.my.vibras.AccomdDetailAct;
import com.my.vibras.R;
import com.my.vibras.adapter.AccCommentAdapter;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.EventCommentAdapter;
import com.my.vibras.adapter.RestaurantCommentAdapter;
import com.my.vibras.databinding.ActivityAllCommentsBinding;
import com.my.vibras.model.AccomadListResSuccess;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetEventComment;
import com.my.vibras.model.SuccessResGetRestaurantComment;
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

public class AllCommentsAct extends AppCompatActivity {

    ActivityAllCommentsBinding binding;
    AllCpmentAdapter mAdapter;
    private ArrayList<HomModel> modelListbrouse=new ArrayList<>();
    private String fromWhere = "",comment="",postId="";
    private ArrayList<SuccessResGetEventComment.Result> commentList = new ArrayList<>();
    private ArrayList<AccomadListResSuccess.Result> commentList2 = new ArrayList<>();

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetRestaurantComment.Result> commentRestList = new ArrayList<>();
    AccCommentAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_all_comments);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.RRback.setOnClickListener(v -> finish());

        fromWhere = getIntent().getExtras().getString("from");

        postId = getIntent().getExtras().getString("id");

        if(fromWhere.equalsIgnoreCase("events"))
        {
            getEventComment();
        }
        else if (fromWhere.equalsIgnoreCase("acc")){

            commentAdapter = new AccCommentAdapter(AllCommentsAct.this,commentList2);
            binding.recycleAllComent.setHasFixedSize(true);
            binding.recycleAllComent.setLayoutManager(new LinearLayoutManager(AllCommentsAct.this));
            binding.recycleAllComent.setAdapter(commentAdapter);
            getComment();
        }else
        {
            getRestComment();
        }
    }

    private void setAdapter()
    {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

        mAdapter = new AllCpmentAdapter(AllCommentsAct.this,modelListbrouse);
        binding.recycleAllComent.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllCommentsAct.this);
        binding.recycleAllComent.setLayoutManager(new GridLayoutManager(this,2));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleAllComent.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AllCpmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }
    private void getComment() {
        DataManager.getInstance().showProgressMessage(AllCommentsAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("accommodation_id",postId);
        Call<AccomadListResSuccess> call = apiInterface.get_accommodation_comment(map);
        call.enqueue(new Callback<AccomadListResSuccess>() {
            @Override
            public void onResponse(Call<AccomadListResSuccess> call, Response<AccomadListResSuccess> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    AccomadListResSuccess data = response.body();
                    Log.e("data",data.getStatus());
                    if (data.getStatus().equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentList2.clear();
                        commentList2.addAll(data.getResult());
                        commentAdapter.notifyDataSetChanged();
                    } else if (data.getStatus().equals("0")) {
                        commentList.clear();
                        showToast(AllCommentsAct.this,data.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AccomadListResSuccess> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getEventComment() {

        DataManager.getInstance().showProgressMessage(AllCommentsAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("event_id",postId);

        Call<SuccessResGetEventComment> call = apiInterface.getEventComments(map);

        call.enqueue(new Callback<SuccessResGetEventComment>() {
            @Override
            public void onResponse(Call<SuccessResGetEventComment> call, Response<SuccessResGetEventComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEventComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentList.clear();
                        commentList.addAll(data.getResult());
                        binding.recycleAllComent.setHasFixedSize(true);
                        binding.recycleAllComent.setLayoutManager(new GridLayoutManager(AllCommentsAct.this,2));
                        binding.recycleAllComent.setAdapter( new EventCommentAdapter(AllCommentsAct.this,commentList));
                    } else if (data.status.equals("0")) {
                        showToast(AllCommentsAct.this,data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetEventComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getRestComment() {

        DataManager.getInstance().showProgressMessage(AllCommentsAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("restaurant_id",postId);

        Call<SuccessResGetRestaurantComment> call = apiInterface.getRestaurantComments(map);

        call.enqueue(new Callback<SuccessResGetRestaurantComment>() {
            @Override
            public void onResponse(Call<SuccessResGetRestaurantComment> call, Response<SuccessResGetRestaurantComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetRestaurantComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentRestList.clear();
                        commentRestList.addAll(data.getResult());
                        binding.recycleAllComent.setHasFixedSize(true);
                        binding.recycleAllComent.setLayoutManager(new GridLayoutManager(AllCommentsAct.this,2));
                        binding.recycleAllComent.setAdapter( new RestaurantCommentAdapter(AllCommentsAct.this,commentRestList));
                    } else if (data.status.equals("0")) {
                        showToast(AllCommentsAct.this,data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetRestaurantComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}