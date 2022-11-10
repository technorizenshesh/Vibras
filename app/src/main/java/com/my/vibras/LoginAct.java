package com.my.vibras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
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

    private static final String TAG = "LoginAct";
    private FirebaseAuth mAuth;
    private String strEmail="",strPassword="",deviceToken="";
    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        getLocation();
        getToken();
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

    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, ""+getString(R.string.fetching_fcm_token_failed), task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    }
                } else {
                    Toast.makeText(LoginAct.this, LoginAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void login() {
        TimeZone tz = TimeZone.getDefault();

        String id = tz.getID();

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", strEmail);
        map.put("password", strPassword);
        map.put("register_id", deviceToken);
        map.put("time_zone",id);
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