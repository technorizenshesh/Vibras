package com.my.vibras.act.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.my.vibras.R;
import com.my.vibras.adapter.GroupMemberAdapter;
import com.my.vibras.databinding.ActivityGroupDetailBinding;
import com.my.vibras.model.SuccessResGetGroupDetails;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class GroupDetailAct extends AppCompatActivity {

    ActivityGroupDetailBinding binding;

    private String groupId = "";

    private String memberIds="";

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetGroupDetails.GroupMembersDetail> membersDetailArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_detail);
        binding.RRback.setOnClickListener(v -> finish());
        groupId = getIntent().getExtras().getString("id");
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.rlJoin.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(GroupDetailAct.this)
                            .setTitle("Join Group")
                            .setMessage("Are you sure you want to join this group?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                     joinGroup();
                                    
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                );

        getGroup();
    }

    public void joinGroup()
    {
        String userId = SharedPreferenceUtility.getInstance(GroupDetailAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(GroupDetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("group_id",groupId);
        map.put("members_id",userId);

        Call<ResponseBody> call = apiInterface.joinGroup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equalsIgnoreCase("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        finish();

                    } else if (data.equalsIgnoreCase("0")) {
                        showToast(GroupDetailAct.this,message);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "onResponse: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getGroup()
    {
        String userId = SharedPreferenceUtility.getInstance(GroupDetailAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(GroupDetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_id",groupId);
        Call<SuccessResGetGroupDetails> call = apiInterface.getGroupDetails(map);
        call.enqueue(new Callback<SuccessResGetGroupDetails>() {
            @Override
            public void onResponse(Call<SuccessResGetGroupDetails> call, Response<SuccessResGetGroupDetails> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroupDetails data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        membersDetailArrayList.clear();
                        membersDetailArrayList.addAll(data.getResult().getGroupMembersDetail());
                        Glide.with(GroupDetailAct.this)
                                .load(data.getResult().getGroupImage())
                                .into(binding.ivGroupImage);
                        if(data.getResult().getIammember().equalsIgnoreCase("Yes"))
                        {
                            binding.rlJoin.setVisibility(View.GONE);
                        } else
                        {
                            binding.rlJoin.setVisibility(View.VISIBLE);
                        }

                        memberIds = data.getResult().getMembersId();

                        binding.tvGroupName.setText(data.getResult().getGroupName());
                        binding.tvGroupParticipants.setText("Group : Participants "+membersDetailArrayList.size());
                        binding.rvParticipants.setHasFixedSize(true);
                        binding.rvParticipants.setLayoutManager(new LinearLayoutManager(GroupDetailAct.this));
                        binding.rvParticipants.setAdapter(new GroupMemberAdapter(GroupDetailAct.this,membersDetailArrayList));
                    } else if (data.status.equals("0")) {
                        showToast(GroupDetailAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetGroupDetails> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}