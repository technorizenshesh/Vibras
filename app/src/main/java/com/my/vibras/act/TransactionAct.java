package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.TrasactionAdapter;
import com.my.vibras.databinding.ActivityTransactionBinding;
import com.my.vibras.model.HomModel;

import java.util.ArrayList;

public class TransactionAct extends AppCompatActivity {

    ActivityTransactionBinding binding;
    TrasactionAdapter mAdapter;
    private ArrayList<HomModel> modelListbrouse=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transaction);

        setAdapter();

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setAdapter()
    {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

        mAdapter = new TrasactionAdapter(TransactionAct.this,modelListbrouse);
        binding.recycleTrasaction.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransactionAct.this);
        binding.recycleTrasaction.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleTrasaction.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new TrasactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }
}