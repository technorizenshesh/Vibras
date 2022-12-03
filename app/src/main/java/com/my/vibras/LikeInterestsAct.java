package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.act.HomeUserAct;
import com.my.vibras.adapter.PaymentCardRecyclerViewAdapter;
import com.my.vibras.databinding.ActivityLikeInterestsBinding;
import com.my.vibras.model.PaymentModel;
import com.my.vibras.model.SuccessResGetInterest;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class LikeInterestsAct extends AppCompatActivity {

    ActivityLikeInterestsBinding binding;
    PaymentCardRecyclerViewAdapter mAdapter;
    private ArrayList<PaymentModel> modelList = new ArrayList<>();

    private ArrayList<SuccessResGetInterest.Result> interestList = new ArrayList<>();

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_like_interests);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RContinew.setOnClickListener(v -> {
            SharedPreferenceUtility.getInstance(getApplication())
                    .putBoolean(Constant.IS_USER_LOGGED_IN, true);
            startActivity(new Intent(LikeInterestsAct.this,
                    HomeUserAct.class));
        });
        setAdapter();
    }

    private void setAdapter() {

//      modelList.add(new PaymentModel("Photography","",R.drawable.logo));
//      modelList.add(new PaymentModel("Cooking","",R.drawable.logo));
//      modelList.add(new PaymentModel("Video Games","",R.drawable.logo));
//      modelList.add(new PaymentModel("Music","",R.drawable.logo));
//      modelList.add(new PaymentModel("Travelling","",R.drawable.logo));
//      modelList.add(new PaymentModel("Shopping","",R.drawable.logo));
//      modelList.add(new PaymentModel("Speeches","",R.drawable.logo));
//      modelList.add(new PaymentModel("Art & Crafts","",R.drawable.logo));
//      modelList.add(new PaymentModel("Swimming","",R.drawable.logo));
//      modelList.add(new PaymentModel("Drinking","",R.drawable.logo));
//      modelList.add(new PaymentModel("Extreme Sports","",R.drawable.logo));
//      modelList.add(new PaymentModel("Fitness","",R.drawable.logo));

        mAdapter = new PaymentCardRecyclerViewAdapter(LikeInterestsAct.this,interestList);
        binding.recyclelikeintrest.setHasFixedSize(true);
        binding.recyclelikeintrest.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclelikeintrest.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new PaymentCardRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetInterest.Result model) {

            }
        });

        if (NetworkAvailablity.checkNetworkStatus(this)) {
            getInterest();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    private void getInterest() {

        DataManager.getInstance().showProgressMessage(LikeInterestsAct.this, getString(R.string.please_wait));

        Call<SuccessResGetInterest> call = apiInterface.getPassion();

        call.enqueue(new Callback<SuccessResGetInterest>() {
            @Override
            public void onResponse(Call<SuccessResGetInterest> call, Response<SuccessResGetInterest> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInterest data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        interestList.clear();
                        interestList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(LikeInterestsAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetInterest> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }




}