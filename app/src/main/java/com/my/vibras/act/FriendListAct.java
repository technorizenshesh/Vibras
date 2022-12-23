package com.my.vibras.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.FriendsListAdapter;
import com.my.vibras.databinding.ActivityFriendListBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class FriendListAct extends AppCompatActivity {

    ActivityFriendListBinding binding;
    private ArrayList<HomModel> modelListbrouse = new ArrayList<>();
    FriendsListAdapter mAdapter;
    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();
    private VibrasInterface apiInterface;
    GPSTracker gpsTracker;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_list);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        session = new Session(this);
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            session.setHOME_LAT(gpsTracker.getLatitude() + "");
            session.setHOME_LONG(gpsTracker.getLongitude() + "");
        }
        getAllUsers();

        binding.RRFrnd.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void getAllUsers() {

        String userId = SharedPreferenceUtility.getInstance(FriendListAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(FriendListAct.this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lat", session.getHOME_LAT());
        map.put("lon",session.getHOME_LONG());
        Call<SuccessResGetUsers> call = apiInterface.getFriendsList(map);
        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        mAdapter = new FriendsListAdapter(FriendListAct.this, usersList);
                        binding.rvFrnd.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendListAct.this);
                        binding.rvFrnd.setLayoutManager(new GridLayoutManager(FriendListAct.this, 3));
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvFrnd.setAdapter(mAdapter);
                        mAdapter.SetOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, HomModel model) {

                            }
                        });

                    } else if (data.status.equals("0")) {
                        showToast(FriendListAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setAdapter() {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

    }
}