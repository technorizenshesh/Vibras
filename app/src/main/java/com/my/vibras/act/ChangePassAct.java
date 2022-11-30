package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityChangePassBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
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

public class ChangePassAct extends AppCompatActivity {

    ActivityChangePassBinding binding;

    String oldPass = "", newConfirmPass = "", newPass = "", pass = "";

    VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_pass);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> {
            finish();
        });

        binding.rPassword.setOnClickListener(v ->
                {
                    oldPass = binding.etPass.getText().toString().trim();
                    newPass = binding.etNewPass.getText().toString().trim();
                    newConfirmPass = binding.etNewConPass.getText().toString().trim();
                    if (isValid()) {
                        if (NetworkAvailablity.checkNetworkStatus(ChangePassAct.this)) {
                            changePassword();
                        } else {
                            Toast.makeText(ChangePassAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                      //  Toast.makeText(ChangePassAct.this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private boolean isValid() {
        if (oldPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_old_pass));
            return false;
        } else if (newPass.equalsIgnoreCase("")) {
            binding.etNewPass.setError(getString(R.string.enter_new_password));
            return false;
        } else if (newConfirmPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_confirm_password));
            return false;
        } else if (!newConfirmPass.equalsIgnoreCase(newPass)) {
            binding.etNewConPass.setError(getString(R.string.password_mismatched));
            return false;
        }
        return true;
    }

    public void changePassword() {

        String userId = SharedPreferenceUtility.getInstance(ChangePassAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ChangePassAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("old_pass", oldPass);
        map.put("new_pass", newPass);

        Call<SuccessResSignup> call = apiInterface.changePassword(map);

        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        showToast(ChangePassAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        binding.etNewPass.setText("");
                        binding.etPass.setText("");
                        binding.etNewConPass.setText("");
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(ChangePassAct.this, data.message);
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