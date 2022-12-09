package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.ConversationSearchAdapter;
import com.my.vibras.databinding.ActivitySearchConversationUserBinding;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class SearchConversationUserAct extends AppCompatActivity {

    ActivitySearchConversationUserBinding binding;

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetConversation.Result> conversation = new ArrayList<>();
    private ArrayList<SuccessResGetConversation.Result> searhList = new ArrayList<>();

    private ConversationSearchAdapter searchAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_search_conversation_user);
       apiInterface = ApiClient.getClient().create(VibrasInterface.class);

       searchAdapter = new ConversationSearchAdapter(SearchConversationUserAct.this,searhList);

       binding.ivBack.setOnClickListener(v -> finish());
       binding.rvUsers.setHasFixedSize(true);
       binding.rvUsers.setLayoutManager(new LinearLayoutManager(SearchConversationUserAct.this));
       binding.rvUsers.setAdapter(searchAdapter);

       binding.etSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               searhList.clear();
               for (SuccessResGetConversation.Result result:conversation)
               {
                   if(result.getFirstName().contains(s.toString()))
                   {
                       searhList.add(result);
                   }
               }

               searchAdapter.notifyDataSetChanged();

           }
       });
        if (NetworkAvailablity.checkNetworkStatus(SearchConversationUserAct.this)) {
            getConversation();
        } else {
            Toast.makeText(SearchConversationUserAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private void getConversation() {
        String userId = SharedPreferenceUtility.getInstance(SearchConversationUserAct.this).getString(USER_ID);
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
               } else if (data.status.equals("0")) {
                        showToast(SearchConversationUserAct.this, data.message);
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
}