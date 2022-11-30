package com.my.vibras.act.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.my.vibras.R;
import com.my.vibras.databinding.FragmentHomeBinding;
import com.my.vibras.databinding.FragmentProfileBinding;
import com.my.vibras.fragment.PostsFragment;

public class ProfileFragment extends Fragment{

    private FragmentProfileBinding binding;

    private ProfileFragment.Qr_DetailsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container, false);
        binding.imgBAck.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        setUpUi();
        return binding.getRoot();
    }

    private void setUpUi() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All Photos"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ProfileFragment.Qr_DetailsAdapter(getActivity(),getChildFragmentManager(), binding.tabLayout.getTabCount());

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public class Qr_DetailsAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public Qr_DetailsAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PostsFragment recents = new PostsFragment();
                    return recents;

                case 1:
                    PostsFragment recents1 = new PostsFragment();
                    return recents1;

                case 2:
                    PostsFragment recents11 = new PostsFragment();
                    return recents11;

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}