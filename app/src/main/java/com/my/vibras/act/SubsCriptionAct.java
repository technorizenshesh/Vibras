package com.my.vibras.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.SubscriptionAdapter;
import com.my.vibras.databinding.ActivitySubsCriptionBinding;
import com.my.vibras.model.SuccessResGetSubscription;
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

public class SubsCriptionAct extends AppCompatActivity implements SubscriptionAdapter.OnItemClickListener {

    ActivitySubsCriptionBinding binding;

    private VibrasInterface apiInterface;

    ArrayList<SuccessResGetSubscription.Result> subscriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subs_cription);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

       /* binding.llBasic.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class)
                    .putExtra("type",subscriptionList.get(0).getPlanType())
                    .putExtra("planId",subscriptionList.get(0).getId())
                    .putExtra("planPrice",subscriptionList.get(0).getMonthlyPrice())
                    .putExtra("from","user")
            );
        });

        binding.btnStandard.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class).putExtra("type",subscriptionList.get(1).getPlanType())
                    .putExtra("planId",subscriptionList.get(1).getId())
                    .putExtra("planPrice",subscriptionList.get(1).getMonthlyPrice())
                    .putExtra("from","user")
            );
        });

        binding.btnPremium.setOnClickListener(v -> {
            startActivity(new Intent(SubsCriptionAct.this,PaymentsAct.class).putExtra("type",subscriptionList.get(1).getPlanType())
                    .putExtra("planId",subscriptionList.get(1).getId())
                    .putExtra("planPrice",subscriptionList.get(1).getMonthlyPrice())
                    .putExtra("from","user")
            );
        });*/
        getSubscription();
    }

    public void getSubscription() {

        DataManager.getInstance().showProgressMessage(SubsCriptionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        Call<SuccessResGetSubscription> call = apiInterface.getSubscription(map);
        call.enqueue(new Callback<SuccessResGetSubscription>() {
            @Override
            public void onResponse(Call<SuccessResGetSubscription> call, Response<SuccessResGetSubscription> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetSubscription data = response.body();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        subscriptionList.clear();
                        subscriptionList.addAll(data.getResult());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(getApplicationContext(), subscriptionList);
                        binding.recycelSubsc.setAdapter(subscriptionAdapter);
                        binding.recycelSubsc.setLayoutManager(layoutManager);
                        subscriptionAdapter.SetOnItemClickListener(new SubscriptionAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position,
                                                    SuccessResGetSubscription.Result model) {
                              /*  Toast.makeText(SubsCriptionAct.this, "clicked",
                                        Toast.LENGTH_SHORT).show();*/
                                startActivity(new Intent(SubsCriptionAct.this,
                                        PaymentsAct.class).putExtra("type",
                                        model.getPlanType())
                                        .putExtra("planId",model.getId())
                                        .putExtra("planPrice",model.getMonthlyPrice())
                                        .putExtra("from","user")
                                );
                            }
                        });

                        //  setData();

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

    @Override
    public void onItemClick(View view, int position, SuccessResGetSubscription.Result model) {

    }


  /*  public void setData()
    {

        binding.tvService.setText(Html.fromHtml(subscriptionList.get(0).getDescription()));

        binding.PlanName.setText(subscriptionList.get(0).getName());
        binding.tv2Services.setText(Html.fromHtml(subscriptionList.get(1).getDescription()));
        binding.planName2.setText(subscriptionList.get(1).getName());
        binding.tv2Price.setText("19");

       // binding.tv3Services.setText(Html.fromHtml(subscriptionList.get(2).getDescription()));
      //  binding.planName3.setText(subscriptionList.get(1).getName());
      //  binding.tv3Price.setText("40");

    }*/

}