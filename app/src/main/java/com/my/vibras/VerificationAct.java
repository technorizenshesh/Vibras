package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.my.vibras.databinding.ActivityVerificationBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class VerificationAct extends AppCompatActivity {

    ActivityVerificationBinding binding;
    String LoginType,userId,finalOtp;

    private VibrasInterface apiInterface;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_verification);

        mContext = VerificationAct.this;

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        if(getIntent()!=null)
        {

            LoginType=getIntent().getStringExtra("loginType").toString();
            userId=getIntent().getStringExtra("user_id").toString();

        }

        binding.RVerify.setOnClickListener(v ->
        {

            if (TextUtils.isEmpty(binding.et1.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et2.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et3.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et4.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else {
                finalOtp =
                        binding.et1.getText().toString().trim() +
                                binding.et2.getText().toString().trim() +
                                binding.et3.getText().toString().trim() +
                                binding.et4.getText().toString().trim();
                ;
                signup();
            }

        });

    }

    public void signup()
    {
        DataManager.getInstance().showProgressMessage(VerificationAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("otp",finalOtp);

        Call<SuccessResSignup> signupCall = apiInterface.verifyOtp(map);
        signupCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    if (data.status.equals("1")) {
                        showToast(VerificationAct.this, data.message);

                        if(LoginType.equalsIgnoreCase("user"))
                        {
                            startActivity(new Intent(VerificationAct.this,LoginAct.class).putExtra("loginType","user"));
                        }else
                        {
                            startActivity(new Intent(VerificationAct.this,LoginAct.class).putExtra("loginType","company"));

                        }

                    } else if (data.status.equals("0")) {
                        showToast(VerificationAct.this, data.message);
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


}