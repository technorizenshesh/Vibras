package com.my.vibras.Company;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.ChooseLanguage;
import com.my.vibras.R;
import com.my.vibras.SelectViberLoginAct;
import com.my.vibras.act.ChangePassAct;
import com.my.vibras.act.HelpContactActivity;
import com.my.vibras.act.NotificationScreenAct;
import com.my.vibras.act.PrivacyPolicyAct;
import com.my.vibras.act.SavedEventsAct;
import com.my.vibras.act.SavedRestaurantAct;
import com.my.vibras.act.SettingAct;
import com.my.vibras.act.TransactionAct;
import com.my.vibras.databinding.ActivityComapmnySettingBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class ComapmnySettingAct extends AppCompatActivity {

    ActivityComapmnySettingBinding binding;
    private SuccessResSignup.Result userDetail;
    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comapmny_setting);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        getProfile();
        binding.RRpayment.setOnClickListener(v -> {
//            startActivity(new Intent(ComapmnySettingAct.this, PaymentsAct.class));
        });

        binding.RRtrasaction.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, TransactionAct.class));
        });

        binding.RRNotification.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, NotificationScreenAct.class));
        });

        binding.RRpasword.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, ChangePassAct.class));
        });

        binding.RRPP.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, PrivacyPolicyAct.class)
                    .putExtra("from", "company"));
        });
        binding.RRChangel.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, ChooseLanguage.class)
                    .putExtra("from", "company"));
            //  finish();
        });
        binding.RRFAQ.setOnClickListener(v ->
                {
                    startActivity(new Intent(ComapmnySettingAct.this, HelpContactActivity.class));
                }
        );

        binding.RRpasword.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, ChangePassAct.class));
        });

        binding.rlRestaurant.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, SavedRestaurantAct.class));
        });

        binding.rlEvents.setOnClickListener(v -> {
            startActivity(new Intent(ComapmnySettingAct.this, SavedEventsAct.class));
        });

        binding.RRSignOut.setOnClickListener(v -> {
           /* startActivity(new Intent(ComapmnySettingAct.this, SelectViberLoginAct.class));
            finish();*/


            logoutApi();

        });
    }


    private void logoutApi() {
        String userId = SharedPreferenceUtility.getInstance(ComapmnySettingAct.this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<ResponseBody> call = apiInterface.logout(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
//                    SuccessResAddComment data = response.body();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equalsIgnoreCase("1")) {
                        SharedPreferenceUtility.getInstance(getApplicationContext())
                                .putBoolean(Constant.IS_USER_LOGGED_IN, false);
                        Intent intent = new Intent(ComapmnySettingAct.this,
                                SelectViberLoginAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (data.equalsIgnoreCase("0")) {
                        showToast(ComapmnySettingAct.this,message);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "onResponse: "+e);
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

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(ComapmnySettingAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ComapmnySettingAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResSignup> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    userDetail = data.getResult();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();
                    } else if (data.status.equals("0")) {
                        showToast(ComapmnySettingAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setProfileDetails()
    {

     //   binding.tvName.setText(userDetail.getFirstName()+" "+userDetail.getLastName());

        Glide
                .with(getApplicationContext())
                .load(userDetail.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);

    }

}