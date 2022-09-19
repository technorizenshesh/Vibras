package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.databinding.ActivityHomeCompanyBinding;
import com.my.vibras.databinding.ActivityLoginBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    ActivityLoginBinding binding;
    String LoginType;

    private String strEmail="",strPassword="";

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        if(getIntent()!=null)
        {
             LoginType=getIntent().getStringExtra("loginType").toString();
        }

        binding.llLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginAct.this,SignUpAct.class).putExtra("loginType",LoginType));
        });

        binding.RLogin.setOnClickListener(v -> {
            strEmail = binding.edtEmail.getText().toString().trim();
            strPassword = binding.edtPassword.getText().toString().trim();
            if (isValid()) {
                if (NetworkAvailablity.checkNetworkStatus(this)) {
                    login();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", strEmail);
        map.put("password", strPassword);

        Call<SuccessResSignup> call = apiInterface.login(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID, data.getResult().getId());
                        Toast.makeText(LoginAct.this,""+getResources().getString(R.string.logged_in_success), Toast.LENGTH_SHORT).show();
                        if(LoginType.equalsIgnoreCase("user"))
                        {
                            SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_TYPE,"user");
                            startActivity(new Intent(LoginAct.this,TakeSelfieAct.class).putExtra("loginType",LoginType));
                            finish();
                        }else
                        {
                            SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_TYPE,"company");
                            SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                            startActivity(new Intent(LoginAct.this, HomeComapnyAct.class).putExtra("loginType",LoginType));
                            finish();
                        }

                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
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

    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            binding.edtPassword.setError(getString(R.string.enter_pass));
            return false;
        }
        return true;
    }


}