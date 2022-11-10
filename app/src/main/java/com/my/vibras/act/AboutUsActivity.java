package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityAboutUs2Binding;
import com.my.vibras.model.SuccessResGetPP;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class AboutUsActivity extends AppCompatActivity {
    private VibrasInterface apiInterface;
    private String description="";

    private ActivityAboutUs2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_about_us2);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> finish());
        getPrivacyPolicy();
    }

    public void getPrivacyPolicy()
    {

        DataManager.getInstance().showProgressMessage(AboutUsActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResGetPP> call = apiInterface.getAboutUs(map);

        call.enqueue(new Callback<SuccessResGetPP>() {
            @Override
            public void onResponse(Call<SuccessResGetPP> call, Response<SuccessResGetPP> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPP data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        description = data.getResult().getDescription();
                        setWebView();
                    } else if (data.status.equals("0")) {
                        showToast(AboutUsActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPP> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadData(description, "text/html; charset=utf-8", "UTF-8");
    }


}