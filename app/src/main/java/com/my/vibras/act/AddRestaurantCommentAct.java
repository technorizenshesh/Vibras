package com.my.vibras.act;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.CommentRestaurantAdapter;
import com.my.vibras.databinding.ActivityAddCommentBinding;
import com.my.vibras.model.SuccessResGetRestaurantComment;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
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

public class AddRestaurantCommentAct extends AppCompatActivity {

    ActivityAddCommentBinding binding;

    private String postId;

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetRestaurantComment.Result> commentList = new ArrayList<>();

    private CommentRestaurantAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_comment);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        postId = getIntent().getExtras().getString("postId");

        binding.img.setOnClickListener(v ->
                {
                    if(!binding.etComment.getText().toString().equalsIgnoreCase(""))
                    {
                        addComment();
                    }else
                    {
                        showToast(this,"Please enter comment.");
                    }
                }
                );

        binding.RRFrnd.setOnClickListener(v ->
               {
                   finish();
               }
               );

        commentAdapter = new CommentRestaurantAdapter(AddRestaurantCommentAct.this,commentList);
        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(AddRestaurantCommentAct.this));
        binding.rvComments.setAdapter(commentAdapter);

        if (NetworkAvailablity.checkNetworkStatus(AddRestaurantCommentAct.this)) {
            getComment();
        } else {
            Toast.makeText(AddRestaurantCommentAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private void addComment() {

        String text = binding.etComment.getText().toString();
        String userId = SharedPreferenceUtility.getInstance(AddRestaurantCommentAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(AddRestaurantCommentAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("restaurant_id",postId);
        map.put("comment",text);
        map.put("rating","5");

        Call<ResponseBody> call = apiInterface.addRestaurantComment(map);
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

                        binding.etComment.setText("");

                        getComment();

                    } else if (data.equals("0")) {
                        showToast(AddRestaurantCommentAct.this,message);
                    }
                } catch (Exception e) {
                    binding.etComment.setText("");
                    Log.d("TAG", "onResponse: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                binding.etComment.setText("");
            }
        });
    }

    private void getComment() {

        DataManager.getInstance().showProgressMessage(AddRestaurantCommentAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("restaurant_id",postId);

        Call<SuccessResGetRestaurantComment> call = apiInterface.getRestaurantComments(map);

        call.enqueue(new Callback<SuccessResGetRestaurantComment>() {
            @Override
            public void onResponse(Call<SuccessResGetRestaurantComment> call, Response<SuccessResGetRestaurantComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetRestaurantComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        commentList.clear();
                        commentList.addAll(data.getResult());
                        commentAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        commentList.clear();
                        showToast(AddRestaurantCommentAct.this,data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetRestaurantComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}