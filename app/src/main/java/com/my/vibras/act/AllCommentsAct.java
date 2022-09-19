package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.FriendsListAdapter;
import com.my.vibras.databinding.ActivityAllCommentsBinding;
import com.my.vibras.model.HomModel;

import java.util.ArrayList;

public class AllCommentsAct extends AppCompatActivity {

    ActivityAllCommentsBinding binding;
    AllCpmentAdapter mAdapter;
    private ArrayList<HomModel> modelListbrouse=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_all_comments);

        setAdapter();
    }

    private void setAdapter()
    {
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));
        modelListbrouse.add(new HomModel(""));

        mAdapter = new AllCpmentAdapter(AllCommentsAct.this,modelListbrouse);
        binding.recycleAllComent.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllCommentsAct.this);
        binding.recycleAllComent.setLayoutManager(new GridLayoutManager(this,2));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleAllComent.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AllCpmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomModel model) {

            }
        });
    }
}