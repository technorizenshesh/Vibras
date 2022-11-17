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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.ChatDetailsScreen;
import com.my.vibras.act.FilterAct;
import com.my.vibras.act.FriendListAct;
import com.my.vibras.act.SearchConversationUserAct;
import com.my.vibras.adapter.ChatAdapter;
import com.my.vibras.adapter.GroupChatAdapter;
import com.my.vibras.adapter.SingleChatAdapter;
import com.my.vibras.chat.AllChatUserModel;
import com.my.vibras.chat.AllChatuserAdapter;
import com.my.vibras.databinding.FragmentChatBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetGroup;
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

public class ChatFragment extends Fragment implements AllChatuserAdapter.OnItemDeleteClickListener {

   private FragmentChatBinding binding;

    GroupChatAdapter mAdapter;

    private ArrayList<SuccessResGetGroup.Result> groupList = new ArrayList<>();

    SingleChatAdapter mAdapterNew;

    private ArrayList<SuccessResGetConversation.Result> modelList = new ArrayList<>();

    private ArrayList<HomModel> modelListNew = new ArrayList<>();

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetConversation.Result> conversation = new ArrayList<>();
    ArrayList<AllChatUserModel> chatUSerModelArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.etSearch.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), SearchConversationUserAct.class));
                }
                );

     /*   if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getConversation();
            getMyGroupList();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }*/
        binding.RRFrnd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FriendListAct.class));
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getConversation();
            getMyGroupList();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    private void getConversation() {
        chatUSerModelArrayList.clear();
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                      //  binding.rvSingle.setAdapter(new SingleChatAdapter(getActivity(),conversation,ChatFragment.this::onItemClick));
                        for (int i = 0; i <conversation.size() ; i++) {
                            AllChatUserModel  chatUSerModel= new AllChatUserModel(conversation.get(i).getId()
                                    ,conversation.get(i).getFirstName() +" "+conversation.get(i).getLastName()
                                    ,conversation.get(i).getImage(),conversation.get(i).getLastMessage(),
                                    conversation.get(i).getReceiverId(),conversation.get(i).getReceiverId());
                            chatUSerModelArrayList.add(chatUSerModel);
                        }
                        binding.rvSingle.setAdapter(new AllChatuserAdapter(chatUSerModelArrayList,getActivity(),ChatFragment.this::onItemDeleteClick));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        conversation.clear();
                        binding.rvSingle.setLayoutManager(new LinearLayoutManager(getActivity()));
                      //  binding.rvSingle.setAdapter(new SingleChatAdapter(getActivity(),conversation,ChatFragment.this::onItemClick));
                        for (int i = 0; i <conversation.size() ; i++) {
                            AllChatUserModel  chatUSerModel= new AllChatUserModel(conversation.get(i).getId()
                                    ,conversation.get(i).getFirstName() +" "+conversation.get(i).getLastName()
                                    ,conversation.get(i).getImage(),conversation.get(i).getLastMessage(),
                                    conversation.get(i).getReceiverId(),conversation.get(i).getReceiverId());
                            chatUSerModelArrayList.add(chatUSerModel);
                        }
                        binding.rvSingle.setAdapter(new AllChatuserAdapter(chatUSerModelArrayList,getActivity(),ChatFragment.this::onItemDeleteClick));
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

    private void getMyGroupList() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetGroup> call = apiInterface.getMygroupApi(map);
        call.enqueue(new Callback<SuccessResGetGroup>() {
            @Override
            public void onResponse(Call<SuccessResGetGroup> call, Response<SuccessResGetGroup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroup data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        groupList.clear();

                        groupList.addAll(data.getResult());

                        mAdapter = new GroupChatAdapter(getActivity(), groupList, new GroupChatAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, SuccessResGetGroup.Result model) {

                                deleteGroup(groupList.get(position).getId());

                            }
                        });
                        binding.rvGrp.setHasFixedSize(true);
                        // use a linear layout manager
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        binding.rvGrp.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
                        binding.rvGrp.setAdapter(mAdapter);

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


    public void deleteGroup(String postId)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_id",postId);
        Call<SuccessResAddLike> call = apiInterface.deleteGroup(map);
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

                        getMyGroupList();

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

  /*  @Override
    public void onItemClick(View view, int position, SuccessResGetConversation.Result model) {


    }*/

    private void deleteConversation(String receiverId,String senderId)
    {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("chat").child(SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID));
           mPostReference.removeValue();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("chat").orderByChild("senderID").equalTo(
                SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID));
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
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

    @Override
    public void onItemDeleteClick(View view, int position, AllChatUserModel model) {
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
}