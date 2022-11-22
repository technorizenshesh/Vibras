package com.my.vibras.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.PostsAdapter;
import com.my.vibras.databinding.FragmentFriendPostsBinding;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetStories;
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

public class FriendVideoFragment extends Fragment implements PostClickListener {

    private FragmentFriendPostsBinding binding;

    private ArrayList<SuccessResGetStories> storyList = new ArrayList<>();

    private ArrayList<SuccessResGetPosts.Result> postList = new ArrayList<>();

    private VibrasInterface apiInterface;

    private PostsAdapter postsAdapter;
    String Userid ="";
    String type ="";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_posts,container, false);
        Userid = getArguments().getString("user_id");
        type = getArguments().getString("type");
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        postsAdapter = new PostsAdapter(getActivity(),postList,FriendVideoFragment.this,"Other");
        binding.rvPosts.setHasFixedSize(true);
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPosts.setAdapter(postsAdapter);
        getPosts();
        return binding.getRoot();
    }

    private void getPosts() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        Map<String,String> map = new HashMap<>();
        map.put("user_id",Userid);
        map.put("viewer_id",userId);
        map.put("type_status","VIDEO");
        Log.e("TAG", "mappppppppppppppppppppp: "+map );
        Call<SuccessResGetPosts> call = apiInterface.getPost(map);
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
                         ArrayList<SuccessResGetPosts.Result> postList1 = new ArrayList<>();
                        postList1.addAll(data.getResult());
                        postList.clear();

                        for (int i = 0; i < postList1.size(); i++) {
                            SuccessResGetPosts.Result  r = postList1.get(i);
                           if (r.getType().equalsIgnoreCase("POST")
                                   &&r.getTypeStatus().equalsIgnoreCase("VIDEO")){
                               postList.add(r);
                            }
                        }
                      //  postList.addAll(data.getResult());
                        postsAdapter.notifyDataSetChanged();
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
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",Userid);
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
//                        setProfileDetails();

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

}