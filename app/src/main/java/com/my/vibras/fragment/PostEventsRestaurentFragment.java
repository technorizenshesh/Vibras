package com.my.vibras.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.my.vibras.R;
import com.my.vibras.databinding.FragmentNotificationsBinding;
import com.my.vibras.databinding.FragmentPostEventsBinding;
import com.my.vibras.databinding.FragmentPostEventsRestaurentBinding;

public class PostEventsRestaurentFragment extends Fragment {

    private Fragment fragment;
    private FragmentPostEventsRestaurentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_events_restaurent,container, false);

        binding.txtEvents.setOnClickListener(v -> {
            binding.txtEvents.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            binding.txtRestaurant.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            binding.txtAccomadtion.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            fragment = new PostEventsFragment();
            loadFragment(fragment);
        });

        binding.txtRestaurant.setOnClickListener(v -> {
            binding.txtEvents.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            binding.txtAccomadtion.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            binding.txtRestaurant.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            fragment = new PostRestaurentFragment();
            loadFragment(fragment);
        });
        binding.txtAccomadtion.setOnClickListener(v -> {
            binding.txtEvents.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            binding.txtAccomadtion.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            binding.txtRestaurant.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            fragment = new PostAccommadationsFragment();
            loadFragment(fragment);
        });
        fragment = new PostEventsFragment();
        loadFragment(fragment);

        return binding.getRoot();
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homeContainer, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }

}