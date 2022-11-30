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
import com.my.vibras.adapter.ChatAdapter;
import com.my.vibras.databinding.ActivityChatDetailsScreenBinding;
import com.my.vibras.model.SuccessResGetChat;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

public class ChatDetailsScreen extends AppCompatActivity {

    ActivityChatDetailsScreenBinding binding;

    private VibrasInterface apiInterface;

    private String strChatMessage="";

    private String id="",strUserName="";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat_details_screen);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        id = getIntent().getExtras().getString("id");
        strUserName = getIntent().getExtras().getString("name");

        binding.txtName.setText(strUserName);

        binding.RRFrnd.setOnClickListener(v -> {
            onBackPressed();
        });

        if (NetworkAvailablity.checkNetworkStatus(ChatDetailsScreen.this)) {
            getChat();
        } else {
            Toast.makeText(ChatDetailsScreen.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strChatMessage = binding.etText.getText().toString();

                strChatMessage = encodeEmoji(strChatMessage);

                if(!strChatMessage.equals(""))
                {
                    uploadImageVideoPost("","Text");
                }
            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat();
                }
            }
        },0,5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    public static String encodeEmoji (String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
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

    public void uploadImageVideoPost(String imageVideoPath,String type1)
    {
        String strUserId = SharedPreferenceUtility.getInstance(ChatDetailsScreen.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ChatDetailsScreen.this,getString(R.string.please_wait));
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<SuccessResInsertChat> loginCall = apiInterface.insertImageVideoChat(senderId,receiverId,messageText,type);
        loginCall.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertChat data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(ChatDetailsScreen.this, data.message);
                        binding.etText.setText("");
                        getChat();
                    } else if (data.status.equals("0")) {
                        showToast(ChatDetailsScreen.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResInsertChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                getChat();
            }
        });
    }

    private void getChat() {
        String userId = SharedPreferenceUtility.getInstance(ChatDetailsScreen.this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id",id);
        Call<SuccessResGetChat> call = apiInterface.getChat(map);
        call.enqueue(new Callback<SuccessResGetChat>() {
            @Override
            public void onResponse(Call<SuccessResGetChat> call, Response<SuccessResGetChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetChat data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        chatList.clear();
                        chatList.addAll(data.getResult());
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(ChatDetailsScreen.this));
                        binding.rvMessageItem.setAdapter(new ChatAdapter(ChatDetailsScreen.this,chatList,userId));
                        binding.rvMessageItem.scrollToPosition(chatList.size()-1);
                    } else if (data.status.equals("0")) {
                        showToast(ChatDetailsScreen.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}