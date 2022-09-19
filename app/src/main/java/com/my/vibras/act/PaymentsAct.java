package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.PaymentAdapter;
import com.my.vibras.databinding.ActivityPaymentsBinding;
import com.my.vibras.model.HomModel;

import java.util.ArrayList;

public class PaymentsAct extends AppCompatActivity {

    PaymentAdapter mAdapter;
    private ArrayList<HomModel> modelListbrouse=new ArrayList<>();
    ActivityPaymentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_payments);

       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.RRAddPayment.setOnClickListener(v -> {
           startActivity(new Intent(new Intent(PaymentsAct.this,AddPaymentCard.class)));
       });
        setAdapter();
    }

    private void setAdapter()
    {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

        mAdapter = new PaymentAdapter(PaymentsAct.this,modelListbrouse);
        binding.recycleTrasaction.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentsAct.this);
        binding.recycleTrasaction.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleTrasaction.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new PaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }
}