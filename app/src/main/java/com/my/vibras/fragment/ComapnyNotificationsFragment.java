package com.my.vibras.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.CompanyNotificationAdapter;
import com.my.vibras.databinding.FragmentNotificationsBinding;
import com.my.vibras.databinding.FragmentNotificationsComapnyBinding;
import com.my.vibras.model.SuccessResGetNotification;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class ComapnyNotificationsFragment extends Fragment {

private FragmentNotificationsComapnyBinding binding;

    private ArrayList<SuccessResGetNotification.Result> notificationList = new ArrayList<>();
    private VibrasInterface apiInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_comapny,container, false);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getNotification();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void getNotification()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id","3");
        Call<SuccessResGetNotification> call = apiInterface.getNotification(map);
        call.enqueue(new Callback<SuccessResGetNotification>() {
            @Override
            public void onResponse(Call<SuccessResGetNotification> call, Response<SuccessResGetNotification> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetNotification data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        notificationList.clear();
                        notificationList.addAll(data.getResult());
                        binding.rvNotification.setHasFixedSize(true);
                        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvNotification.setAdapter(new CompanyNotificationAdapter(getActivity(),notificationList));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(),""+data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetNotification> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}