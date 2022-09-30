package com.my.vibras.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.EventsDetailsScreen;
import com.my.vibras.act.ui.home.HomeViewModel;
import com.my.vibras.adapter.BruseEventAdapter;
import com.my.vibras.adapter.NEarmeEventstAdapter;
import com.my.vibras.adapter.NEarmeRestaurentAdapter;
import com.my.vibras.adapter.SliderAdapter;
import com.my.vibras.adapter.StoriesAdapter;
import com.my.vibras.databinding.FragmentEventsBinding;
import com.my.vibras.databinding.FragmentPostsBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class EventsFragment extends Fragment{

   private FragmentEventsBinding binding;

    private ArrayList<HomModel> modelListbrouse = new ArrayList<>();
    private ArrayList<SuccessResGetEvents.Result> modelListNearME = new ArrayList<>();
    private ArrayList<SuccessResGetRestaurants.Result> modelListNearMERest = new ArrayList<>();

    private VibrasInterface apiInterface;

    BruseEventAdapter mAdapter;

    NEarmeEventstAdapter mAdapterNEarMe;

    NEarmeRestaurentAdapter mAdapterNEarMeRest;

    private SliderAdapter sliderAdapter;

    private ArrayList<SuccessResGetBanner.Result> bannersList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        Bundle bundle1 = this.getArguments();

        if (bundle1!=null)
        {
            Gson gson = new Gson();
            binding.RRtoolbar.setVisibility(View.VISIBLE);
        }

        binding.imgBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        setAdapter();

        setAdapNearMe();

        setAdapRestoaurent();

        sliderAdapter = new SliderAdapter(getContext(),bannersList);
        binding.imageSlider.setSliderAdapter(sliderAdapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. : WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(3);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();

        getBannerList();
        getEvents();
        getRestaurants();
        return binding.getRoot();
    }

    public void getBannerList()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetBanner> call = apiInterface.getBanner(map);
        call.enqueue(new Callback<SuccessResGetBanner>() {
            @Override
            public void onResponse(Call<SuccessResGetBanner> call, Response<SuccessResGetBanner> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetBanner data = response.body();
                    Log.e("data",data.status);
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

    public void getEvents()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","");
        map.put("lon","");
        Call<SuccessResGetEvents> call = apiInterface.getEvents(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data",data.status);
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

    public void getRestaurants()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","");
        map.put("lon","");
        Call<SuccessResGetRestaurants> call = apiInterface.getRestaurnat(map);
        call.enqueue(new Callback<SuccessResGetRestaurants>() {
            @Override
            public void onResponse(Call<SuccessResGetRestaurants> call, Response<SuccessResGetRestaurants> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetRestaurants data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

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

    private void setAdapter()
    {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

        mAdapter = new BruseEventAdapter(getActivity(),modelListbrouse);
        binding.recycleCategory.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleCategory.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleCategory.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new BruseEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }

    private void setAdapNearMe()
    {

        mAdapterNEarMe = new NEarmeEventstAdapter(getActivity(),modelListNearME);
        binding.recycleNEarme.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleNEarme.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleNEarme.setAdapter(mAdapterNEarMe);
        mAdapterNEarMe.SetOnItemClickListener(new NEarmeEventstAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {


            }
        });
    }

    private void setAdapRestoaurent()
    {

        mAdapterNEarMeRest = new NEarmeRestaurentAdapter(getActivity(),modelListNearMERest);
        binding.recycleRestoaurent.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleRestoaurent.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleRestoaurent.setAdapter(mAdapterNEarMeRest);
    }
}