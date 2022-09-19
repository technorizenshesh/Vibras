package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.my.vibras.R;
import com.my.vibras.adapter.SearchAdapter;
import com.my.vibras.databinding.ActivitySearchBinding;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
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

public class SearchAct extends AppCompatActivity {

    ActivitySearchBinding binding;

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_search);

        binding.ivBack.setOnClickListener(v -> finish());

        apiInterface  = ApiClient.getClient().create(VibrasInterface.class);

        binding.RRfilter.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(SearchAct.this,FilterAct.class)));
        });

        binding.etSearch.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                getUsers(cs.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }

    private void getUsers(String title) {

        String userId=  SharedPreferenceUtility.getInstance(SearchAct.this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("search",title);

        Call<SuccessResGetUsers> call = apiInterface.searchUser(map);

        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        binding.rvUsers.setHasFixedSize(true);
                        binding.rvUsers.setLayoutManager(new LinearLayoutManager(SearchAct.this));
                        binding.rvUsers.setAdapter(new SearchAdapter(SearchAct.this,usersList,true));
                    } else if (data.status.equals("0")) {
                        showToast(SearchAct.this, data.message);
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

}