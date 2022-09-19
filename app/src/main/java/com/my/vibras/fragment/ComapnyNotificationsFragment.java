package com.my.vibras.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.vibras.R;
import com.my.vibras.databinding.FragmentNotificationsBinding;
import com.my.vibras.databinding.FragmentNotificationsComapnyBinding;

public class ComapnyNotificationsFragment extends Fragment {

private FragmentNotificationsComapnyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_comapny,container, false);

        return binding.getRoot();
    }


}