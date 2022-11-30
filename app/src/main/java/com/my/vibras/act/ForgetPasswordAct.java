package com.my.vibras.act;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityForgetPasswordBinding;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class ForgetPasswordAct extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    VibrasInterface apiInterface;
    String strEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RLogin.setOnClickListener(v -> {
            strEmail = binding.edtEmail.getText().toString().trim();
            if (!strEmail.equalsIgnoreCase("")) {
                if (NetworkAvailablity.checkNetworkStatus(this)) {
                    forgetPasssword(strEmail);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Add Email First", Toast.LENGTH_SHORT).show();
            }
        });
        binding.llLogin.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void forgetPasssword(String strEmail) {
        DataManager.getInstance().showProgressMessage(ForgetPasswordAct.this,
                getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", strEmail);
        Call<ResponseBody> call = apiInterface.resetPassword(map);
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
                        new AlertDialog.Builder(ForgetPasswordAct.this)
                                .setTitle("Password Reset Successful")
                                .setMessage("An Email Contain New Password Has Been Sent To you Email Address")
                                .setPositiveButton("Close", (dialog, which) -> {
                                    onBackPressed();
                                })
                                // .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show().setOnDismissListener(dialog -> onBackPressed());
                    } else if (data.equals("0")) {
                        showToast(ForgetPasswordAct.this, message);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "onResponse: " + e);
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