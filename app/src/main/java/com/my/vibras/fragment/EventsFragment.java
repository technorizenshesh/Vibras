package com.my.vibras.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.CreateGroupAct;
import com.my.vibras.MainActivity;
import com.my.vibras.R;
import com.my.vibras.SearchEventAct;
import com.my.vibras.act.AccAct;
import com.my.vibras.act.RestaurantAct;
import com.my.vibras.act.ViewAllCategoryAct;
import com.my.vibras.act.ViewAllEventAct;
import com.my.vibras.act.ViewAllGroupsAct;
import com.my.vibras.adapter.AllGroupChatAdapter;
import com.my.vibras.adapter.BruseEventAdapter;
import com.my.vibras.adapter.NEarmeAccAdapter;
import com.my.vibras.adapter.NEarmeEventstAdapter;
import com.my.vibras.adapter.NEarmeRestaurentAdapter;
import com.my.vibras.adapter.SliderAdapter;
import com.my.vibras.chat.ChatInnerMessagesActivity;
import com.my.vibras.databinding.FragmentEventsBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessAccList;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetGroup;
import com.my.vibras.model.SuccessResGetGroupData;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.USER_TYPE;
import static com.my.vibras.retrofit.Constant.showToast;
import static io.agora.rtc.gl.VideoFrame.TextureBuffer.TAG;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    private ArrayList<HomModel> modelListbrouse = new ArrayList<>();
    private ArrayList<SuccessResGetEvents.Result> modelListNearME = new ArrayList<>();
    private ArrayList<SuccessResGetRestaurants.Result> modelListNearMERest = new ArrayList<>();
    private ArrayList<SuccessAccList.Result> resultArrayList = new ArrayList<>();

    private boolean addGroup = false;
    private VibrasInterface apiInterface;
    BruseEventAdapter mAdapter;
    AllGroupChatAdapter groupChatAdapter;
    private ArrayList<SuccessResGetCategory.Result> categoryResult = new ArrayList<>();
    NEarmeEventstAdapter mAdapterNEarMe;
    NEarmeRestaurentAdapter mAdapterNEarMeRest;
    NEarmeAccAdapter nEarmeAccAdapter;
    private SliderAdapter sliderAdapter;
    GPSTracker gpsTracker;
    private boolean isUserLoggedIn;

    private ArrayList<SuccessResGetBanner.Result> bannersList = new ArrayList<>();
    Session session;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false);
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        session = new Session(getContext());
        isUserLoggedIn = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.IS_USER_LOGGED_IN);
        Bundle bundle1 = this.getArguments();
        if (bundle1 != null) {
            Gson gson = new Gson();
//            binding.RRtoolbar.setVisibility(View.VISIBLE);
        }
        binding.imgBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        String type = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_TYPE);
         if (type.equalsIgnoreCase("user")){
             binding.btnCreateGroup.setVisibility(View.VISIBLE);
         }else {
             binding.imgBack.setVisibility(View.GONE);
         }
        binding.btnCreateGroup.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), CreateGroupAct.class));
                }
        );
       getProfile();
       getGroup();
       getAllGroups();
        binding.ivSearch.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), SearchEventAct.class));
                }
        );
        setAdapRestoaurent();
        setAdapter();
        setAdapNearMe();
        sliderAdapter = new SliderAdapter(getContext(), bannersList);
        binding.imageSlider.setSliderAdapter(sliderAdapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. : WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(3);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();
        binding.tvViewAllAcc.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), AccAct.class));
                }
        ); binding.tvViewAllRest.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), RestaurantAct.class));
                }
        );

        binding.tvViewAllCat.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllCategoryAct.class));
                }
        );

        binding.tvViewAllEvent.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllEventAct.class)
                            .putExtra("from", "events"));
                }
        );

        binding.tvViewAllGroups.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllGroupsAct.class).putExtra("from", "all"));
                }
        );


        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            if (gpsTracker.canGetLocation()) {
                session.setHOME_LAT(gpsTracker.getLatitude() + "");
                session.setHOME_LONG(gpsTracker.getLongitude() + "");
            } else {

            }
        }
        return binding.getRoot();
    }

    private ArrayList<SuccessResGetGroup.Result> groupList = new ArrayList<>();

    private void getAllGroups() {
        Map<String, String> map = new HashMap<>();
        Call<SuccessResGetGroup> call = apiInterface.getAllGroupApi(map);
        call.enqueue(new Callback<SuccessResGetGroup>() {
            @Override
            public void onResponse(Call<SuccessResGetGroup> call, Response<SuccessResGetGroup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroup data = response.body();
                    String dataResponse = new Gson().toJson(response.body());

                    Log.e("dataResponsedataResponse", dataResponse);
                    if (data.status.equals("1")) {
                        //String dataResponse = new Gson().toJson(response.body());
                        groupList.clear();
                        groupList.addAll(data.getResult());
                        groupChatAdapter = new AllGroupChatAdapter(getActivity(), groupList);
                        binding.rvGrp.setHasFixedSize(true);
                        binding.rvGrp.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        binding.rvGrp.setAdapter(new AllGroupChatAdapter(getActivity(), groupList));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetGroup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private SuccessResSignup.Result userDetail;

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                        binding.txtName.setText(getString(R.string.good_vibes) +   (
                                StringEscapeUtils.unescapeJava( data.getResult().getFirstName())));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    @Override
    public void onResume() {
        super.onResume();
       getBannerList();
       getEventCategory();
       getEvents();
        getRestaurants();
        getAcc();
    }
    private void getAcc()
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
            String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
            Map<String, String> map = new HashMap<>();
            map.put("user_id", userId);
            map.put("lat", session.getHOME_LAT());
            map.put("lon", session.getHOME_LONG());
            Log.e(TAG, "getRestaurants:   getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+map );
            Call<SuccessAccList> call = apiInterface.getAccomadtion(map);
            call.enqueue(new Callback<SuccessAccList>() {
                @Override
                public void onResponse(Call<SuccessAccList> call, Response<SuccessAccList> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        SuccessAccList data = response.body();
                        Log.e("data", data.getStatus());
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+ dataResponse);

                        if (data.getStatus().equals("1")) {
                            resultArrayList.clear();
                            resultArrayList.addAll(data.getResult());
                            nEarmeAccAdapter.notifyDataSetChanged();
                        } else if (data.getStatus().equals("0")) {
                            showToast(getActivity(), data.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SuccessAccList> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }
            });

        }

    private void getEventCategory() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetCategory> call = apiInterface.getEventsCategory(map);
        call.enqueue(new Callback<SuccessResGetCategory>() {
            @Override
            public void onResponse(Call<SuccessResGetCategory> call, Response<SuccessResGetCategory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCategory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        setProfileDetails();
                        categoryResult.clear();
                        categoryResult.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCategory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getBannerList() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetBanner> call = apiInterface.getBanner(map);
        call.enqueue(new Callback<SuccessResGetBanner>() {
            @Override
            public void onResponse(Call<SuccessResGetBanner> call, Response<SuccessResGetBanner> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetBanner data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        bannersList.clear();
                        bannersList.addAll(data.getResult());
                        sliderAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetBanner> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void getEvents() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lat", session.getHOME_LAT());
        map.put("lon", session.getHOME_LONG());
        Call<SuccessResGetEvents> call = apiInterface.getEvents(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        modelListNearME.clear();
                        modelListNearME.addAll(data.getResult());
                        mAdapterNEarMe.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void getRestaurants() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lat", session.getHOME_LAT());
        map.put("lon", session.getHOME_LONG());
        Log.e(TAG, "getRestaurants:   getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+map );
        Call<SuccessResGetRestaurants> call = apiInterface.getRestaurnat(map);
        call.enqueue(new Callback<SuccessResGetRestaurants>() {
            @Override
            public void onResponse(Call<SuccessResGetRestaurants> call, Response<SuccessResGetRestaurants> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetRestaurants data = response.body();
                    Log.e("data", data.status);
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "getRestaurnatgetRestaurnatgetRestaurnatgetRestaurnat"+ dataResponse);

                    if (data.status.equals("1")) {
                       modelListNearMERest.clear();
                        modelListNearMERest.addAll(data.getResult());
                        mAdapterNEarMeRest.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetRestaurants> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void setAdapter() {

        mAdapter = new BruseEventAdapter(getActivity(), categoryResult);
        binding.recycleCategory.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleCategory.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new BruseEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetCategory.Result model) {

            }
        });
    }

    private void setAdapNearMe() {

        mAdapterNEarMe = new NEarmeEventstAdapter(getActivity(), modelListNearME);
        binding.recycleNEarme.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleNEarme.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleNEarme.setAdapter(mAdapterNEarMe);
        mAdapterNEarMe.SetOnItemClickListener(new NEarmeEventstAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }

    private void setAdapRestoaurent() {
        nEarmeAccAdapter = new NEarmeAccAdapter(getActivity(), resultArrayList);
        binding.recycleAcc.setHasFixedSize(true);
        binding.recycleAcc.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false));
        binding.recycleAcc.setAdapter(nEarmeAccAdapter);

        mAdapterNEarMeRest = new NEarmeRestaurentAdapter(getActivity(), modelListNearMERest);
        binding.recycleRestoaurent.setHasFixedSize(true);
        binding.recycleRestoaurent.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false));

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleRestoaurent.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleRestoaurent.setAdapter(mAdapterNEarMeRest);
    }

    private void getGroup() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetGroupData> call = apiInterface.getGroup(map);
        call.enqueue(new Callback<SuccessResGetGroupData>() {
            @Override
            public void onResponse(Call<SuccessResGetGroupData> call, Response<SuccessResGetGroupData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroupData data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        if (data.getResult().get(0).getRemainingGroup().equalsIgnoreCase("0")) {
                            addGroup = false;
                        } else {
                            addGroup = true;
                        }

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetGroupData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}