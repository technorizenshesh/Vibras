package com.my.vibras.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.SearchItemBinding;
import com.my.vibras.model.SuccessFollowersRes;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class LikeRecivedAdapter extends RecyclerView.Adapter<LikeRecivedAdapter.StoriesViewHolder> {

    private Context context;

    SearchItemBinding binding;

    private List<SuccessFollowersRes.Result> usersList;

    public LikeRecivedAdapter(Context context, List<SuccessFollowersRes.Result> usersList) {
        this.context = context;
        this.usersList = usersList;

    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName;
        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvUserName.setText(StringEscapeUtils.unescapeJava(
                usersList.get(position).getUserDetails().getFirstName()
                + " " + (usersList.get(position).getUserDetails().getLastName())));
        Log.e("TAG", "onBindViewHolder: " + usersList.get(position).getImage());
        Glide
                .with(context)
                .load(usersList.get(position).getUserDetails().getImage())
                .centerCrop()
                .into(circleImageView);

        circleImageView.setOnClickListener(v ->
                {

                }
        );

    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(SearchItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
