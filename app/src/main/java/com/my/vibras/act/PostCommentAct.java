package com.my.vibras.act;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityPostCommentBinding;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;
import static com.my.vibras.utility.RandomString.IncodeIntoBase64;

public class PostCommentAct extends AppCompatActivity {
    ActivityPostCommentBinding binding;
    private String fromWhere = "", comment = "", postId = "";
    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_comment);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        fromWhere = getIntent().getExtras().getString("from");
        postId = getIntent().getExtras().getString("id");
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnPost.setOnClickListener(v ->
                {
                    float rating = binding.ratingBar.getRating();
                    comment = binding.etComment.getText().toString();

                    if (!comment.equalsIgnoreCase("")) {
                        if (fromWhere.equalsIgnoreCase("events")) {
                            addEventComment(rating);
                        } else if (fromWhere.equalsIgnoreCase("acc")) {
                            addAccComment(rating);
                        } else {
                            addResComment(rating);

                        }
                    } else {
                        Toast.makeText(PostCommentAct.this, "Please enter comment.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void addAccComment(float rating) {
        String userId = SharedPreferenceUtility.getInstance(PostCommentAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PostCommentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("accommodation_id", postId);
        map.put("comment", IncodeIntoBase64(comment));
        map.put("rating", rating + "");

        Call<ResponseBody> call = apiInterface.addaccommodation_comment(map);
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
                        finish();

                    } else if (data.equals("0")) {
                        showToast(PostCommentAct.this, message);
                    }
                } catch (Exception e) {
                    binding.etComment.setText("");
                    Log.d("TAG", "onResponse: " + e);
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

    private void addResComment(float rating) {
        String userId = SharedPreferenceUtility.getInstance(PostCommentAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PostCommentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("restaurant_id", postId);
        map.put("comment", IncodeIntoBase64(comment));
        map.put("rating", rating + "");

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

                        finish();

                    } else if (data.equals("0")) {
                        showToast(PostCommentAct.this, message);
                    }
                } catch (Exception e) {
                    binding.etComment.setText("");
                    Log.d("TAG", "onResponse: " + e);
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

    private void addEventComment(float rating) {
        String userId = SharedPreferenceUtility.getInstance(PostCommentAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PostCommentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_id", postId);
        map.put("comment", IncodeIntoBase64(comment));
        map.put("rating", rating + "");

        Call<ResponseBody> call = apiInterface.addEventComment(map);
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

                        finish();

                    } else if (data.equals("0")) {
                        showToast(PostCommentAct.this, message);
                    }
                } catch (Exception e) {
                    binding.etComment.setText("");
                    Log.d("TAG", "onResponse: " + e);
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


}