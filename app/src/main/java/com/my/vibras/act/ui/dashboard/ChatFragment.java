package com.my.vibras.act.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.ChatDetailsScreen;
import com.my.vibras.act.FriendListAct;
import com.my.vibras.adapter.ChatAdapter;
import com.my.vibras.adapter.GroupChatAdapter;
import com.my.vibras.adapter.SingleChatAdapter;
import com.my.vibras.databinding.FragmentChatBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class ChatFragment extends Fragment implements SingleChatAdapter.OnItemClickListener {

   private FragmentChatBinding binding;

    GroupChatAdapter mAdapter;

    SingleChatAdapter mAdapterNew;

    private ArrayList<SuccessResGetConversation.Result> modelList = new ArrayList<>();

    private ArrayList<HomModel> modelListNew = new ArrayList<>();

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetConversation.Result> conversation = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getConversation();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.RRFrnd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FriendListAct.class));
        });

        return binding.getRoot();
    }

    private void setAdapter()
    {

//        mAdapter = new GroupChatAdapter(getActivity(),modelList);
//        binding.rvGrp.setHasFixedSize(true);
//        // use a linear layout manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        binding.rvGrp.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
//        binding.rvGrp.setAdapter(mAdapter);
//        mAdapter.SetOnItemClickListener(new GroupChatAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, HomModel model) {
//            }
//        });
    }

    private void setAdapterSingle()
    {

//        modelListNew.add(new HomModel(""));
//        modelListNew.add(new HomModel(""));
//        modelListNew.add(new HomModel(""));
//        modelListNew.add(new HomModel(""));
//        modelListNew.add(new HomModel(""));
//        mAdapterNew = new SingleChatAdapter(getActivity(),modelList);

//        binding.rvSingle.setHasFixedSize(true);
//        // use a linear layout manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        binding.rvSingle.setLayoutManager(linearLayoutManager);
//        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
//        binding.rvSingle.setAdapter(mAdapterNew);
    }

    private void getConversation() {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("receiver_id",userId);
        Call<SuccessResGetConversation> call = apiInterface.getConversation(map);
        call.enqueue(new Callback<SuccessResGetConversation>() {
            @Override
            public void onResponse(Call<SuccessResGetConversation> call, Response<SuccessResGetConversation> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetConversation data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        conversation.clear();
                        conversation.addAll(data.getResult());
                        binding.rvSingle.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvSingle.setAdapter(new SingleChatAdapter(getActivity(),conversation,ChatFragment.this::onItemClick));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        conversation.clear();
                        binding.rvSingle.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvSingle.setAdapter(new SingleChatAdapter(getActivity(),conversation,ChatFragment.this::onItemClick));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetConversation> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, SuccessResGetConversation.Result model) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.delete_post))
                .setMessage(getString(R.string.are_you_sure_want_to_delete_post))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteConversation(conversation.get(position).getReceiverId(),conversation.get(position).getSenderId());
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void deleteConversation(String receiverId,String senderId)
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("receiver_id", receiverId);
        map.put("sender_id", senderId);

        Call<SuccessResDeleteConversation> call = apiInterface.deleteConversation(map);

        call.enqueue(new Callback<SuccessResDeleteConversation>() {
            @Override
            public void onResponse(Call<SuccessResDeleteConversation> call, Response<SuccessResDeleteConversation> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteConversation data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        getConversation();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);
                        getConversation();
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

}