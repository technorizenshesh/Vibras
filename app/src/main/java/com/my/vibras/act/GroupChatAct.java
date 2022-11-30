package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.GroupOne2OneChatAdapter;
import com.my.vibras.databinding.ActivityCreateGroupBinding;
import com.my.vibras.databinding.ActivityGroupChatBinding;
import com.my.vibras.model.SuccessResGetGroupChat;
import com.my.vibras.model.SuccessResInsertGroupChat;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class GroupChatAct extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    
    private String id="",name="";

    private VibrasInterface apiInterface;

    private String strChatMessage="";

    private ArrayList<SuccessResGetGroupChat.Result> chatList = new ArrayList<>();
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_chat);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        binding.RRFrnd.setOnClickListener(v -> finish());
        binding.txtName.setText(name);
        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strChatMessage = binding.etText.getText().toString();

                strChatMessage = encodeEmoji(strChatMessage);

                if(!strChatMessage.equals(""))
                {
                    uploadImageVideoPost("","text");
                }
            }
        });

        if (NetworkAvailablity.checkNetworkStatus(GroupChatAct.this)) {
            getChat();
        } else {
            Toast.makeText(GroupChatAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat();
                }
            }
        },0,5000);

    }

    public static String encodeEmoji (String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private boolean isLastVisible() {
        if (chatList != null && chatList.size() != 0) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.rvMessageItem.getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int numItems = binding.rvMessageItem.getAdapter().getItemCount();
            return (pos >= numItems - 1);
        }
        return false;
    }

    private void getChat() {
        String userId = SharedPreferenceUtility.getInstance(GroupChatAct.this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_id",id);
        Call<SuccessResGetGroupChat> call = apiInterface.getGroupChat(map);
        call.enqueue(new Callback<SuccessResGetGroupChat>() {
            @Override
            public void onResponse(Call<SuccessResGetGroupChat> call, Response<SuccessResGetGroupChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroupChat data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status == 1) {
                        String dataResponse = new Gson().toJson(response.body());
                        chatList.clear();
                        chatList.addAll(data.getResult());
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(GroupChatAct.this));
                        binding.rvMessageItem.setAdapter(new GroupOne2OneChatAdapter(GroupChatAct.this,chatList,userId));
                        binding.rvMessageItem.scrollToPosition(chatList.size()-1);
                    } else if (data.status == 0) {
                        showToast(GroupChatAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetGroupChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void uploadImageVideoPost(String imageVideoPath,String type1)
    {
        String strUserId = SharedPreferenceUtility.getInstance(GroupChatAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(GroupChatAct.this,getString(R.string.please_wait));
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<SuccessResInsertGroupChat> loginCall = apiInterface.insertGroupImageVideoChat(senderId,receiverId,messageText,type);
        loginCall.enqueue(new Callback<SuccessResInsertGroupChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertGroupChat> call, Response<SuccessResInsertGroupChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertGroupChat data = response.body();
                    Log.e("data",data.status);
                    showToast(GroupChatAct.this, data.result);
                    binding.etText.setText("");
                    getChat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResInsertGroupChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                getChat();
            }
        });
    }
}