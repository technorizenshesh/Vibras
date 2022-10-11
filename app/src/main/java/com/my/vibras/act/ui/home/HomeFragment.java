package com.my.vibras.act.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;
import com.mig35.carousellayoutmanager.DefaultChildSelectionListener;
import com.my.vibras.LikeInterestsAct;
import com.my.vibras.R;
import com.my.vibras.ShowStory;
import com.my.vibras.act.ChangePassAct;
import com.my.vibras.act.SearchAct;
import com.my.vibras.adapter.HomeUsersRecyclerViewAdapter;
import com.my.vibras.adapter.StoriesAdapter;
import com.my.vibras.databinding.FragmentHomeBinding;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.CenterZoomLayoutManager;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.HomeItemClickListener;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class HomeFragment extends Fragment implements HomeItemClickListener {

    private FragmentHomeBinding binding;
    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();
    private  StoriesAdapter mAdapter;
    private  HomeUsersRecyclerViewAdapter usersAdapters;
    private VibrasInterface apiInterface;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.RRSearch.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchAct.class));
        });

        binding.RREvents.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("from","home");
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_eventsFragment,bundle);
        });

        setAdapter();

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getInterest();
            getAllUsers();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void setAdapter()
    {
        mAdapter = new StoriesAdapter(getActivity(), this.storyList);
        binding.rvStories.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.rvStories.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new StoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetStories.Result model) {
            }
        });
    }

    private void getInterest() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<SuccessResGetStories> call = apiInterface.getStories();
        call.enqueue(new Callback<SuccessResGetStories>() {
            @Override
            public void onResponse(Call<SuccessResGetStories> call, Response<SuccessResGetStories> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetStories data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        storyList.clear();
                        storyList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetStories> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getAllUsers() {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<SuccessResGetUsers> call = apiInterface.getAllUsers(map);
        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        usersAdapters = new HomeUsersRecyclerViewAdapter(getActivity(),usersList,HomeFragment.this);
                        CenterZoomLayoutManager layoutManager =
                                new CenterZoomLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvhome.setLayoutManager(layoutManager);
                        binding.rvhome.setAdapter(usersAdapters);
                        // Scroll to the position we want to snap to
                        // Wait until the RecyclerView is laid out.
                        binding.rvhome.post(new Runnable() {
                            @Override
                            public void run() {
                                // Shift the view to snap  near the center of the screen.
                                // This does not have to be precise.
                                int dx = (binding.rvhome.getWidth() - binding.rvhome.getChildAt(0).getWidth()) / 2;
                                binding.rvhome.scrollBy(-dx, 0);
                                // Assign the LinearSnapHelper that will initially snap the near-center view.
                                LinearSnapHelper snapHelper = new LinearSnapHelper();
                                binding.rvhome.setOnFlingListener(null);
                                snapHelper.attachToRecyclerView(binding.rvhome);
                            }
                        });
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    @Override
    public void addUserProfileLike(int position) {
        addOtherProfileLike(usersList.get(position).getId(),"Like");
    }

    @Override
    public void addLikeToUser(int position) {

        addOtherProfileLike(usersList.get(position).getId(),"Love");

    }

    @Override
    public void addChatToUser(int position) {

    }

    @Override
    public void addCommentToUser(int position) {

        addOtherProfileLike(usersList.get(position).getId(),"Fire");

    }

    private void addFire(String otherUserID)
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("profile_user", otherUserID);

        Call<SuccessResDeleteConversation> call = apiInterface.addFireToOther(map);

        call.enqueue(new Callback<SuccessResDeleteConversation>() {
            @Override
            public void onResponse(Call<SuccessResDeleteConversation> call, Response<SuccessResDeleteConversation> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteConversation data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        showToast(getActivity(), data.message);
//                        getAllUsers();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResDeleteConversation> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

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

    private void addLikePost(String otherUserId) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", "");
        map.put("post_id", "");

        Call<SuccessResAddOtherProfileLike> call = apiInterface.addLikePost(map);

        call.enqueue(new Callback<SuccessResAddOtherProfileLike>() {
            @Override
            public void onResponse(Call<SuccessResAddOtherProfileLike> call, Response<SuccessResAddOtherProfileLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddOtherProfileLike data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status==1) {

//                        getAllUsers();

                    } else if (data.status==0) {
                        showToast(getActivity(), data.message);
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


}