package com.my.vibras.act;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.ui.myprofile.MyProfileFragment;
import com.my.vibras.act.ui.profile.ProfileFragment;
import com.my.vibras.databinding.ActivityFriendProfileBinding;
import com.my.vibras.databinding.FragmentProfileBinding;
import com.my.vibras.fragment.FriendPostsFragment;
import com.my.vibras.fragment.PostsFragment;
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
import static com.my.vibras.utility.Util.hideKeyboard;

public class FriendProfileActivity extends AppCompatActivity {
    private VibrasInterface apiInterface;

    private ActivityFriendProfileBinding binding;
    private SuccessResSignup.Result userDetail;

    private Qr_DetailsAdapter adapter;
    String User_id = "";
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_profile);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        hideKeyboard(FriendProfileActivity.this);
        try {
            User_id = getIntent().getExtras().getString("id");
            bundle.putString("user_id",User_id );
            if (NetworkAvailablity.checkNetworkStatus(getApplicationContext())) {
                getProfile(User_id);
                //  getPosts();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        setUpUi();
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
                    FriendPostsFragment recents = new FriendPostsFragment();
                    recents.setArguments(bundle);
                    return recents;
                case 1:
                    FriendPostsFragment recents1 = new FriendPostsFragment();
                    recents1.setArguments(bundle);
                    return recents1;
              /*  case 2:
                    PostsFragment recents11 = new PostsFragment();
                    return recents11;

                case 3:
                    PostsFragment recents113 = new PostsFragment();
                    return recents113;*/

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    private void getProfile(String userId) {
        DataManager.getInstance().showProgressMessage(FriendProfileActivity.this
                , getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResSignup> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    userDetail = data.getResult();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();
                    } else if (data.status.equals("0")) {
                        showToast(FriendProfileActivity.this, data.message);
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

    private void setProfileDetails() {

        binding.tvName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());

        if (!userDetail.getBio().equalsIgnoreCase("")) {
            binding.tvBio.setVisibility(View.VISIBLE);
            binding.tvBio.setText(userDetail.getBio());
        } else {
            binding.tvBio.setVisibility(View.GONE);
        }
        Log.e("", "setProfileDetailssetProfileDetails: "+userDetail.getCoverImage() );
        Glide
                .with(getApplicationContext())
                .load(userDetail.getCoverImage())
                .into(binding.ivCoverPhoto);
        Glide
                .with(getApplicationContext())
                .load(userDetail.getImage())
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);

        binding.tvLikeGiven.setText(userDetail.getGivenLikes() + "");
        binding.tvLikeReceived.setText(userDetail.getReceviedLikes() + "");

    }
    private void setUpUi() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
      //  binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Apointments"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new Qr_DetailsAdapter(getApplicationContext(),getSupportFragmentManager(), binding.tabLayout.getTabCount());
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

}
