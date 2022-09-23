package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.AddCommentAct;
import com.my.vibras.databinding.MyeventItemBinding;
import com.my.vibras.databinding.MyeventItemBinding;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.utility.PostClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.StoriesViewHolder> {

    private Context context;
    MyeventItemBinding binding;
    private List<SuccessResMyEventRes.Result> postList;
    private PostClickListener postClickListener;

    public MyEventsAdapter(Context context, List<SuccessResMyEventRes.Result> postList, PostClickListener postClickListener)
    {
      this.context = context;
      this.postList = postList;
      this.postClickListener = postClickListener;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= MyeventItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        ImageView ivPOst = holder.itemView.findViewById(R.id.imgRestaurant);
        TextView tvUserName = holder.itemView.findViewById(R.id.tvRestaurantName);
        TextView tvDescription = holder.itemView.findViewById(R.id.tvDescription);

        ImageView ivLike = holder.itemView.findViewById(R.id.ivLike);
        ImageView ivComment = holder.itemView.findViewById(R.id.ivComment);
        ImageView ivMore = holder.itemView.findViewById(R.id.ivMore);

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        Glide.with(context)
                .load(postList.get(position).getImage())
                .into(ivPOst);

        Glide.with(context)
                .load(postList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(circleImageView);

        tvDescription.setText(postList.get(position).getDescription());

        tvUserName.setText(postList.get(position).getEventName());

    }


    private void showDialog()
    {

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(MyeventItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
