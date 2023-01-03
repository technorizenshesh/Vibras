package com.my.vibras.act.ui.profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.SettingAct;
import com.my.vibras.databinding.FragmentHomeBinding;
import com.my.vibras.databinding.FragmentProfileBinding;
import com.my.vibras.fragment.PostsFragment;
import com.my.vibras.fragment.PostsVideoFragment;
import com.my.vibras.model.SuccessResUpdateRate;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class ProfileFragment extends Fragment{

    private FragmentProfileBinding binding;

    private VibrasInterface apiInterface;
    private SuccessResUpdateRate.Result userDetail;
    private ProfileFragment.Qr_DetailsAdapter adapter;
    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResUpdateRate> call = apiInterface.getNotificationStatus(map);
        call.enqueue(new Callback<SuccessResUpdateRate>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRate> call,
                                   Response<SuccessResUpdateRate> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateRate data = response.body();
                    userDetail = data.getResult();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        if (userDetail.getLat()!=null&&!userDetail.getLat().equalsIgnoreCase(""))
                        {
                            binding.cityState.setText(CurrentCity(
                                    Double.parseDouble(userDetail.getLat())
                                    ,Double.parseDouble(userDetail.getLon())));
                        }else {
                            GPSTracker gpsTracker =new GPSTracker(getActivity());
                            if (gpsTracker.getLatitude()>=0){
                            binding.cityState.setText(CurrentCity(
                                  gpsTracker.getLatitude()
                                    ,gpsTracker.getLongitude()));}
                        }
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    public  String CurrentCity(double latitude,double longitude) {
        if (latitude>=0.0) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            //List<Address> addresses =geocoder.getFromLocation(latitude, longitude, 1);

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getSubLocality();
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                // txt_paddress.setText(address);
                return  cityName+" , "+stateName;
                //   txt_state.setText(stateName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  "  ";

    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container, false);
        binding.imgBAck.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        setUpUi();
        getProfile();
        return binding.getRoot();
    }

    private void setUpUi() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
       binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All Photos"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ProfileFragment.Qr_DetailsAdapter(getActivity(),getChildFragmentManager(), binding.tabLayout.getTabCount());

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public class Qr_DetailsAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public Qr_DetailsAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PostsFragment recents = new PostsFragment();
                    return recents;

                case 1:
                    PostsFragment recents1 = new PostsFragment();
                    return recents1;

                case 2:
                    PostsVideoFragment recents11 = new PostsVideoFragment();
                    return recents11;

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}