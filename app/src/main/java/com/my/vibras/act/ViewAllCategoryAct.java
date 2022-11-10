package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.BruseEventAdapter;
import com.my.vibras.databinding.ActivityViewAllCategoryBinding;
import com.my.vibras.model.SuccessResGetCategory;
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

public class ViewAllCategoryAct extends AppCompatActivity {

    ActivityViewAllCategoryBinding binding;
    private ArrayList<SuccessResGetCategory.Result> categoryResult = new ArrayList<>();

    BruseEventAdapter mAdapter;

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_all_category);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        mAdapter = new BruseEventAdapter(ViewAllCategoryAct.this,categoryResult);
        binding.recycleCategory.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllCategoryAct.this);
        binding.recycleCategory.setLayoutManager(new GridLayoutManager(ViewAllCategoryAct.this,3));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleCategory.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new BruseEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetCategory.Result model) {

            }
        });

        binding.RRback.setOnClickListener(v -> finish());

         getEventCategory();
    }

    private void getEventCategory() {
        DataManager.getInstance().showProgressMessage(ViewAllCategoryAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResGetCategory> call = apiInterface.getEventsCategory(map);
        call.enqueue(new Callback<SuccessResGetCategory>() {
            @Override
            public void onResponse(Call<SuccessResGetCategory> call, Response<SuccessResGetCategory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCategory data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        setProfileDetails();
                        categoryResult.clear();
                        categoryResult.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(ViewAllCategoryAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetCategory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}