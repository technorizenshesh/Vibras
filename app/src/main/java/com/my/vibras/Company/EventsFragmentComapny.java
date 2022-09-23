package com.my.vibras.Company;

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
import com.my.vibras.adapter.MyEventsAdapter;
import com.my.vibras.adapter.StoriesAdapter;
import com.my.vibras.databinding.FragmentEventsCopamnyBinding;
import com.my.vibras.databinding.FragmentPostsBinding;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.retrofit.ApiClient;
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

public class EventsFragmentComapny extends Fragment implements PostClickListener {

    private FragmentEventsCopamnyBinding binding;

    private ArrayList<SuccessResGetStories> storyList = new ArrayList<>();

    private ArrayList<SuccessResMyEventRes.Result> eventsList = new ArrayList<>();

    private MyEventsAdapter myEventsAdapter;

    private VibrasInterface apiInterface;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events_copamny,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

   /*     myEventsAdapter = new MyEventsAdapter(getActivity(),eventsList,this);

        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvRestaurants.setAdapter(myEventsAdapter);

        getMyRestaurantsApi();
*/
        return binding.getRoot();
    }

  /*  public void getMyRestaurantsApi()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResMyEventRes> call = apiInterface.getMyEvents(map);
        call.enqueue(new Callback<SuccessResMyEventRes>() {
            @Override
            public void onResponse(Call<SuccessResMyEventRes> call, Response<SuccessResMyEventRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMyEventRes data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        myEventsAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResMyEventRes> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }*/

    @Override
    public void selectLike(int position, String status) {

    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position) {

    }
}