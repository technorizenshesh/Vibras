package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import com.my.vibras.act.EditProfileAct;
import com.my.vibras.act.PaymentsAct;
import com.my.vibras.act.SettingAct;
import com.my.vibras.act.SubsCriptionAct;
import com.my.vibras.adapter.AddFriendAdapter;
import com.my.vibras.adapter.FriendsListAdapter;
import com.my.vibras.adapter.SelectedUserAdapter;
import com.my.vibras.databinding.ActivityCreateGroupBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
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

public class CreateGroupAct extends AppCompatActivity implements AddFriendAdapter.OnItemClickListener {

    ActivityCreateGroupBinding binding;
    private VibrasInterface apiInterface;
    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();
    private ArrayList<SuccessResGetUsers.Result> selectedUserList = new ArrayList<>();
    private AddFriendAdapter mAdapter;
    private SelectedUserAdapter selectedUserAdapter;
    String usersIds="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_create_group);
       apiInterface = ApiClient.getClient().create(VibrasInterface.class);
       getAllUsers();
       binding.RRFrnd.setOnClickListener(v -> finish());
       binding.ivReferesh.setOnClickListener(v ->
               {
                 getAllUsers();
               }
               );
       selectedUserAdapter = new SelectedUserAdapter(CreateGroupAct.this, selectedUserList, new SelectedUserAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position, SuccessResGetUsers.Result model) {
               selectedUserList.remove(position);
               selectedUserAdapter.notifyDataSetChanged();
           }
       });
       binding.btnCreate.setOnClickListener(v ->
               {
                   if(selectedUserList.size()>0)
                   {
                       createGroup();
                   }
               }
               );
       binding.rvSelectedUser.setHasFixedSize(true);
       binding.rvSelectedUser.setLayoutManager(new LinearLayoutManager(CreateGroupAct.this,LinearLayoutManager.HORIZONTAL,false));
       binding.rvSelectedUser.setAdapter(selectedUserAdapter);
    }

    private void createGroup()
    {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
         usersIds = userId+",";
        for (SuccessResGetUsers.Result result:selectedUserList)
        {
            usersIds = usersIds + result.getId()+",";
        }
        usersIds = usersIds.substring(0, usersIds.length() -1);
        final Dialog dialog = new Dialog(CreateGroupAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_group_name);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        AppCompatButton btnCreate = dialog.findViewById(R.id.btnCreate);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        btnCreate.setOnClickListener(v ->
                {
                    if(edtEmail.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(CreateGroupAct.this, "Please enter group name.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        dialog.dismiss();
                        startActivity(new Intent(CreateGroupAct.this,PaymentsAct.class)
                                .putExtra("memberIds",usersIds)
                                .putExtra("groupName",edtEmail.getText().toString())
                                .putExtra("from","group")
                        );
//                        createGroupApi(usersIds,edtEmail.getText().toString());
                    }
                }
                );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void createGroupApi(String userIds,String groupName)
    {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreateGroupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_name",groupName);
        map.put("members_id",userIds);
        map.put("group_image","");

        Call<ResponseBody> call = apiInterface.createGroup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        finish();

                    } else if (data.equals("0")) {
                        showToast(CreateGroupAct.this,message);
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

    private void getAllUsers() {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreateGroupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","22.7196");
        map.put("lon","75.8577");
        Call<SuccessResGetUsers> call = apiInterface.getFriendsList(map);
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
                        mAdapter = new AddFriendAdapter(CreateGroupAct.this,usersList,CreateGroupAct.this);
                        binding.rvFrnd.setHasFixedSize(true);
                        binding.rvFrnd.setLayoutManager(new LinearLayoutManager(CreateGroupAct.this));
                        binding.rvFrnd.setAdapter(mAdapter);
                    } else if (data.status.equals("0")) {
                        showToast(CreateGroupAct.this, data.message);
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
    public void onItemClick(View view, int position, SuccessResGetUsers.Result model) {
        boolean found = false;
        if(selectedUserList.size()==0)
        {
            selectedUserList.add(usersList.get(position));
            selectedUserAdapter.notifyDataSetChanged();
        } else
        {
            for (SuccessResGetUsers.Result result:selectedUserList)
            {
                String id1 = result.getId();
                String id2 = usersList.get(position).getId();
                if(id1.equalsIgnoreCase(id2))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                selectedUserList.add(usersList.get(position));
                selectedUserAdapter.notifyDataSetChanged();
            } else
            {
                Toast.makeText(CreateGroupAct.this, "User already added.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}