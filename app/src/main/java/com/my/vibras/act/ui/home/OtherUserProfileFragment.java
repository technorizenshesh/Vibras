package com.my.vibras.act.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.ViewAllEventAct;
import com.my.vibras.act.ViewAllGroupsAct;
import com.my.vibras.adapter.PostsAdapter;
import com.my.vibras.databinding.FragmentOtherUserProfileBinding;
import com.my.vibras.databinding.FragmentProfileBinding;
import com.my.vibras.fragment.PostsFragment;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetProfile;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.PostClickListener;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherUserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OtherUserProfileFragment extends Fragment implements PostClickListener {

    private String TAG= "test";

    private FragmentOtherUserProfileBinding binding;

    private OtherUserProfileFragment.Qr_DetailsAdapter adapter;

    private SuccessResGetProfile.Result userDetail ;
    private ArrayList<SuccessResGetPosts.Result> postList = new ArrayList<>();
    private VibrasInterface apiInterface;

    private String otherUserId = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherUserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherUserProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static OtherUserProfileFragment newInstance(String param1, String param2) {
        OtherUserProfileFragment fragment = new OtherUserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_user_profile,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        
        binding.imgBAck.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            otherUserId = bundle.getString("id");
        }



        binding.btnAddLike.setOnClickListener(v ->
                {
                    addOtherProfileLike(otherUserId,"Like");
                }
                );
        binding.llGroup.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllGroupsAct.class).putExtra("from","other").putExtra("id",otherUserId));
                }
        );
        binding.llViewEvents.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllEventAct.class).putExtra("from","other")
                            .putExtra("id",otherUserId)
                    );
                }
        );

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

            getProfile();
            getPosts();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        setUpUi();
        return binding.getRoot();
    }

    private void addOtherProfileLike(String otherUserId,String type) {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("profile_user", otherUserId);
        map.put("type", type);
        Call<SuccessResAddOtherProfileLike> call = apiInterface.addFireLikeLove(map);
        call.enqueue(new Callback<SuccessResAddOtherProfileLike>() {
            @Override
            public void onResponse(Call<SuccessResAddOtherProfileLike> call, Response<SuccessResAddOtherProfileLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddOtherProfileLike data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status==1) {
                        showToast(getActivity(), data.result);
                        binding.btnIlike.setVisibility(View.VISIBLE);
                        binding.btnAddLike.setVisibility(View.GONE);
                    } else if (data.status==0) {
                        showToast(getActivity(), data.result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResAddOtherProfileLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUserId);
        Call<SuccessResGetProfile> call = apiInterface.getOtherProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProfile data = response.body();
                    userDetail = data.getResult();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        try {
                            setProfileDetails();
                        }catch (NullPointerException e)
                        {
                            Log.d(TAG, "onResponse: "+e);
                        }

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setProfileDetails()
    {

        binding.tvName.setText(userDetail.getFirstName()+" "+userDetail.getLastName());

        Glide
                .with(getActivity())
                .load(userDetail.getCoverImage())
                .into(binding.ivCoverPhoto);

        Glide
                .with(getActivity())
                .load(userDetail.getImage())
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);

        if(userDetail.getLikeStatus().equalsIgnoreCase("False"))
        {
            binding.btnIlike.setVisibility(View.GONE);
            binding.btnAddLike.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.btnIlike.setVisibility(View.VISIBLE);
            binding.btnAddLike.setVisibility(View.GONE);
        }
        binding.tvGivenLike.setText(userDetail.getGivenLike());

        binding.receivedLike.setText(userDetail.getReceiveLike());

    }

    private void setUpUi() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All Photos"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new OtherUserProfileFragment.Qr_DetailsAdapter(getActivity(),getChildFragmentManager(), binding.tabLayout.getTabCount());

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

    @Override
    public void selectLike(int position, String status) {

        addLike(postList.get(position).getId());

    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position) {

    }

    @Override
    public void savePost(View param1, String postID, boolean isUser, int position) {

    }

    private void addLike(String postId) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);
        Call<SuccessResAddLike> call = apiInterface.addLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status==1) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        getPosts();

                    } else if (data.status==0) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
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
                    PostsFragment recents11 = new PostsFragment();
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

    private void getPosts() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUserId);
        map.put("type_status","IMAGE");
        Call<SuccessResGetPosts> call = apiInterface.getOtherUserPost(map);
        call.enqueue(new Callback<SuccessResGetPosts>() {
            @Override
            public void onResponse(Call<SuccessResGetPosts> call, Response<SuccessResGetPosts> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPosts data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        postList.clear();
                        postList.addAll(data.getResult());
                        binding.rvPosts.setHasFixedSize(true);
                        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPosts.setAdapter( new PostsAdapter(getActivity(),postList, OtherUserProfileFragment.this,"Mine"));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetPosts> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}