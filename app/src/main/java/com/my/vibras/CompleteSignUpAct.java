package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.databinding.ActivityCompleteSignUpBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class CompleteSignUpAct extends AppCompatActivity {

    private String strEmail="",strPass="",strDob="",strFname="",strLname="",strGender="",strMobile="";
    public VibrasInterface apiInterface;
    String LoginType;
    String[] gender = { "male", "female"};
    ActivityCompleteSignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_complete_sign_up);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        strEmail = getIntent().getExtras().getString("email");
        strPass = getIntent().getExtras().getString("pass");
        strDob = getIntent().getExtras().getString("dob");
        LoginType=getIntent().getStringExtra("loginType").toString();

        binding.RLogin.setOnClickListener(v ->
                {
                    strFname = binding.edtFname.getText().toString().trim();
                    strLname = binding.edtLname.getText().toString().trim();
                    strGender = binding.spinnerGender.getSelectedItem().toString();
                    strMobile = binding.ccp.getSelectedCountryCode()+""+ binding.etPhone.getText().toString().trim();
                    
                    if (isValid()) {
                        if (NetworkAvailablity.checkNetworkStatus(this)) {
                            signup();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
                );

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnerGender.setAdapter(aa);
    }

    private boolean isValid() {
        if (strFname.equalsIgnoreCase("")) {
            binding.edtFname.setError(getString(R.string.enter_first));
            return false;
        } else if (strLname.equalsIgnoreCase("")) {
            binding.edtLname.setError(getString(R.string.enter_last));
            return false;
        }else if (strMobile.equalsIgnoreCase("")) {
            binding.etPhone.setError(getString(R.string.enter_mobile));
            return false;
        }
        return true;
    }

    public void signup()
    {

        String userTYpe="";
        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        if(LoginType.equalsIgnoreCase("user"))
        {
            userTYpe = "USER";
        }
        else
        {
            userTYpe = "COMPANY";
        }

        DataManager.getInstance().showProgressMessage(CompleteSignUpAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("first_name", StringEscapeUtils.escapeJava(strFname));
        map.put("last_name",StringEscapeUtils.escapeJava(strLname));
        map.put("email",strEmail);
        map.put("mobile",strMobile);
        map.put("password",strPass);
        map.put("dob",strDob);
        map.put("type",userTYpe);
        map.put("gender",strGender);
        map.put("time_zone",id);
        Log.e("HashMapHashMap", "signup: "+map );
        Call<SuccessResSignup> signupCall = apiInterface.signup(map);
        signupCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("onResponse", "signupsignupsignup" + dataResponse);

                    if (data.status.equals("1")) {
                        showToast(CompleteSignUpAct.this, data.message);

                        startActivity(new Intent(CompleteSignUpAct.this,
                                VerificationAct.class)
                                .putExtra("otp",data.getResult().getOtp())
                                .putExtra("user_id",data.getResult().getId())
                                .putExtra("type",LoginType)
                        );


                    } else if (data.status.equals("0")) {
                        showToast(CompleteSignUpAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                Log.e("onFailure", "signupsignupsignup" + t.getMessage());
                Log.e("onFailure", "signupsignupsignup" + t.getLocalizedMessage());
                Log.e("onFailure", "signupsignupsignup" + t.getCause());


                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}