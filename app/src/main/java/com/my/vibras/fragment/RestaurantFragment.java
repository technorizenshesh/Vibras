package com.my.vibras.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.MyEventsAdapter;
import com.my.vibras.adapter.MyRestaurantAdapter;
import com.my.vibras.databinding.FragmentRestaurantBinding;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResMyRestaurantRes;
import com.my.vibras.model.SuccessResMyRestaurantRes;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantFragment extends Fragment implements PostClickListener {

    FragmentRestaurantBinding binding;

    private ArrayList<SuccessResMyRestaurantRes.Result> eventsList = new ArrayList<>();

    private MyRestaurantAdapter myEventsAdapter;

    private VibrasInterface apiInterface;

    public RestaurantFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static RestaurantFragment newInstance(String param1, String param2) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_restaurant, container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        myEventsAdapter = new MyRestaurantAdapter(getActivity(),eventsList,this);

        binding.rvRestaurants.setHasFixedSize(true);
        binding.rvRestaurants.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvRestaurants.setAdapter(myEventsAdapter);

        getMyRestaurantsApi();

        return binding.getRoot();
    }

    public void getMyRestaurantsApi()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResMyRestaurantRes> call = apiInterface.getMyRestaurant(map);
        call.enqueue(new Callback<SuccessResMyRestaurantRes>() {
            @Override
            public void onResponse(Call<SuccessResMyRestaurantRes> call, Response<SuccessResMyRestaurantRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMyRestaurantRes data = response.body();
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
            public void onFailure(Call<SuccessResMyRestaurantRes> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    @Override
    public void selectLike(int position, String status) {

        addLike(eventsList.get(position).getId());

    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position) {

        showDialog(eventsList.get(position).getId(),position);

    }

    @Override
    public void savePost(View param1, String postID, boolean isUser, int position) {

    }

    private void addLike(String postId) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("restaurant_id",postId);
        Call<SuccessResAddLike> call = apiInterface.addRestaurantLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status+"");
                    getMyRestaurantsApi();
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

    public void showDialog(String postId,int position)
    {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView tvDelete = dialog.findViewById(R.id.tvDelete);
        TextView tvShare = dialog.findViewById(R.id.tvShare);
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        if(userId.equalsIgnoreCase(eventsList.get(position).getUserId()))
        {
            tvDelete.setVisibility(View.VISIBLE);
        }else
        {
            tvDelete.setVisibility(View.GONE);
        }

        tvDelete.setOnClickListener(v1 ->
                {
                    dialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.delete_post))
                            .setMessage(getString(R.string.are_you_sure_want_to_delete_post))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePost(postId);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void deletePost(String postId)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("restaurant_id",postId);
        Call<SuccessResAddLike> call = apiInterface.deleteRestaurant(map);
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

                        getMyRestaurantsApi();

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