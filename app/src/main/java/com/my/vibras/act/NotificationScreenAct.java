package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityNotificationScreenBinding;
import com.my.vibras.model.SuccessResUpdateRate;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class NotificationScreenAct extends AppCompatActivity {

    ActivityNotificationScreenBinding binding;
    private String push="",sms="",mail="",selectedPush="",selectedms="",selectedMail;
    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_notification_screen);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        getProfile();

        binding.btnSave.setOnClickListener(v ->
                {
                    updateNotifications();
                }
                );

    }

    public void updateNotifications()
    {

        if(binding.switchPushNotification.isChecked())
        {
            push = "1";
        }
        else
        {
            push = "0";
        }

        if(binding.switchNotification.isChecked())
        {
            sms = "1";
        }
        else
        {
            sms = "0";
        }

        if(binding.switchEmailNotification.isChecked())
        {
            mail = "1";
        }
        else
        {
            mail = "0";
        }

        String userId = SharedPreferenceUtility.getInstance(NotificationScreenAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(NotificationScreenAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("push_notification", push);
        map.put("notification", sms);
        map.put("email_notification", mail);

        Call<SuccessResUpdateRate> call = apiInterface.updateWorkerNoti(map);

        call.enqueue(new Callback<SuccessResUpdateRate>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRate> call, Response<SuccessResUpdateRate> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateRate data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        showToast(NotificationScreenAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        getProfile();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(NotificationScreenAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateRate> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(NotificationScreenAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(NotificationScreenAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResUpdateRate> call = apiInterface.getNotificationStatus(map);
        call.enqueue(new Callback<SuccessResUpdateRate>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRate> call, Response<SuccessResUpdateRate> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateRate data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        selectedPush = data.getResult().getPushNotification();
                        selectedms = data.getResult().getNotification();
                        selectedMail = data.getResult().getEmailNotification();

                        setNotifications();
                    } else if (data.status.equals("0")) {
                        showToast(NotificationScreenAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResUpdateRate> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setNotifications()
    {
        if(selectedPush.equalsIgnoreCase("0"))
        {
            binding.switchPushNotification.setChecked(false);
        }else

        {
            binding.switchPushNotification.setChecked(true);

        }


        if(selectedms.equalsIgnoreCase("0"))
        {
            binding.switchNotification.setChecked(false);
        }else

        {
            binding.switchNotification.setChecked(true);
        }

        if(selectedMail.equalsIgnoreCase("0"))
        {
            binding.switchEmailNotification.setChecked(false);
        }else
        {
            binding.switchEmailNotification.setChecked(true);
        }

    }

}