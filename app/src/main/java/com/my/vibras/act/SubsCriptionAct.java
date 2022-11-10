package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivitySubsCriptionBinding;
import com.my.vibras.model.SuccessResGetSubscription;
import com.my.vibras.model.SuccessResGetSubscription;
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

public class SubsCriptionAct extends AppCompatActivity {

    ActivitySubsCriptionBinding binding;

    private VibrasInterface apiInterface;

    ArrayList<SuccessResGetSubscription.Result> subscriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_subs_cription);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.llBasic.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class));
        });

        binding.btnStandard.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class).putExtra("type",subscriptionList.get(1).getPlanType())
                    .putExtra("planId",subscriptionList.get(1).getId())
                    .putExtra("planPrice",subscriptionList.get(1).getMonthlyPrice())
                    .putExtra("from","user")
            );
        });

        binding.btnPremium.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class).putExtra("type",subscriptionList.get(2).getPlanType())
                    .putExtra("planId",subscriptionList.get(2).getId())
                    .putExtra("planPrice",subscriptionList.get(2).getMonthlyPrice())
                    .putExtra("from","user")
            );
        });
        getSubscription();
    }

    public void getSubscription()
    {

        DataManager.getInstance().showProgressMessage(SubsCriptionAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        Call<SuccessResGetSubscription> call = apiInterface.getSubscription(map);
        call.enqueue(new Callback<SuccessResGetSubscription>() {
            @Override
            public void onResponse(Call<SuccessResGetSubscription> call, Response<SuccessResGetSubscription> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetSubscription data = response.body();

                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        subscriptionList.clear();
                        subscriptionList.addAll(data.getResult());
                        setData();

                    } else if (data.status.equals("0")) {
                        showToast(SubsCriptionAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetSubscription> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setData()
    {

        binding.tvService.setText(Html.fromHtml(subscriptionList.get(0).getDescription()));

        binding.PlanName.setText(subscriptionList.get(0).getName());

        binding.tv2Services.setText(Html.fromHtml(subscriptionList.get(1).getDescription()));
        binding.planName2.setText(subscriptionList.get(1).getName());
        binding.tv2Price.setText("19");

        binding.tv3Services.setText(Html.fromHtml(subscriptionList.get(2).getDescription()));
        binding.planName3.setText(subscriptionList.get(2).getName());
        binding.tv3Price.setText("40");

    }

}