package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.TrasactionAdapter;
import com.my.vibras.databinding.ActivityTransactionBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetTransaction;
import com.my.vibras.model.SuccessResGetTransaction;
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

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class TransactionAct extends AppCompatActivity {

    ActivityTransactionBinding binding;
    TrasactionAdapter mAdapter;
    private ArrayList<SuccessResGetTransaction.Result> modelListbrouse=new ArrayList<>();

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transaction);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        setAdapter();
        getTransaction();

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setAdapter()
    {
        mAdapter = new TrasactionAdapter(TransactionAct.this,modelListbrouse);
        binding.recycleTrasaction.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransactionAct.this);
        binding.recycleTrasaction.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleTrasaction.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new TrasactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetTransaction.Result model) {

            }
        });
    }

    public void getTransaction()
    {
        String userId = SharedPreferenceUtility.getInstance(TransactionAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(TransactionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetTransaction> call = apiInterface.getTransaction(map);
        call.enqueue(new Callback<SuccessResGetTransaction>() {
            @Override
            public void onResponse(Call<SuccessResGetTransaction> call, Response<SuccessResGetTransaction> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetTransaction data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        modelListbrouse.clear();
                        modelListbrouse.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showToast(TransactionAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetTransaction> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}