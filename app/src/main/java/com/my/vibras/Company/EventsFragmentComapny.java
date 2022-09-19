package com.my.vibras.Company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.vibras.R;
import com.my.vibras.adapter.StoriesAdapter;
import com.my.vibras.databinding.FragmentEventsCopamnyBinding;
import com.my.vibras.databinding.FragmentPostsBinding;
import com.my.vibras.model.SuccessResGetStories;

import java.util.ArrayList;

public class EventsFragmentComapny extends Fragment{

   private FragmentEventsCopamnyBinding binding;

    private ArrayList<SuccessResGetStories> storyList = new ArrayList<>();
    StoriesAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events_copamny,container, false);

        return binding.getRoot();
    }

}