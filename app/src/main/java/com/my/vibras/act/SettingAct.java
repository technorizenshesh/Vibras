package com.my.vibras.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.SelectViberLoginAct;
import com.my.vibras.databinding.ActivitySettingBinding;
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

public class SettingAct extends AppCompatActivity {

    ActivitySettingBinding binding;

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.RRtrasaction.setOnClickListener(v -> {
           startActivity(new Intent(SettingAct.this,TransactionAct.class));
        });

        binding.RRAbout.setOnClickListener(v -> {
           startActivity(new Intent(SettingAct.this,TermsCondition.class));
        });

        binding.RRprivacyPolicy.setOnClickListener(v -> {
           startActivity(new Intent(SettingAct.this,TermsCondition.class));
        });

        binding.RRcSubsCription.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,SubsCriptionAct.class));
        });

        binding.tvNotification.setOnClickListener(v -> {
            startActivity(new Intent(SettingAct.this,NotificationScreenAct.class));
        });

        binding.RRchangePassword.setOnClickListener(v ->
                {
                    startActivity(new Intent(SettingAct.this,ChangePassAct.class));
                }
                );

        binding.RRHelp.setOnClickListener(v ->
                {
                    startActivity(new Intent(SettingAct.this,HelpAct.class));
                }
        );

        binding.RRprivacyPolicy.setOnClickListener(v ->
                {
                    startActivity(new Intent(SettingAct.this,PrivacyPolicyAct.class));
                }
        );

        binding.RRAbout.setOnClickListener(v ->
                {
                    startActivity(new Intent(SettingAct.this,AboutUsActivity.class));
                }
        );

        binding.tvLogout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent intent = new Intent(SettingAct.this,
                            SelectViberLoginAct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                );

        binding.tvDeleteAccount.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(SettingAct.this)
                            .setTitle("Delete account")
                            .setMessage("Are you sure you want to delete this account?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAccount();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void deleteAccount()
    {
        String userId = SharedPreferenceUtility.getInstance(SettingAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(SettingAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
     
        Call<ResponseBody> call = apiInterface.deleteAccount(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        SharedPreferenceUtility.getInstance(getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                        Intent intent = new Intent(SettingAct.this,
                                SelectViberLoginAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else if (data.equals("0")) {
                        showToast(SettingAct.this,message);
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


}