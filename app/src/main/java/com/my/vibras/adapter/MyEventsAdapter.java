package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.MyeventItemBinding;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.utility.PostClickListener;


import org.apache.commons.lang3.StringEscapeUtils;

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
        TextView tvDescription = holder.itemView.findViewById(R.id.timeAgo);

        TextView tvLikeCount = holder.itemView.findViewById(R.id.tvLikeCount);
        TextView tvChat = holder.itemView.findViewById(R.id.tvChat);

        ImageView ivLike = holder.itemView.findViewById(R.id.ivLikeUnlike);
        ImageView ivComment = holder.itemView.findViewById(R.id.ivComment);
        ImageView ivOption = holder.itemView.findViewById(R.id.ivOption);
        ImageView ivSaved = holder.itemView.findViewById(R.id.ivSaved);

        LinearLayoutCompat llLike = holder.itemView.findViewById(R.id.llLike);

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        Glide.with(context)
                .load(postList.get(position).getImage())
                .into(ivPOst);

        Glide.with(context)
                .load(postList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(circleImageView);

        tvDescription.setText(StringEscapeUtils.unescapeJava(postList.get(position).getDateTime()));

        tvUserName.setText(  StringEscapeUtils.unescapeJava(postList.get(position).getEventName()));

        if(postList.get(position).getLikeStatus().equalsIgnoreCase("false"))
        {
            ivLike.setImageResource(R.drawable.ic_rest_unlike);
        }else
        {
            ivLike.setImageResource(R.drawable.ic_rest_like);
        }

        if(postList.get(position).getSavePost().equalsIgnoreCase("No"))
        {
            ivSaved.setImageResource(R.drawable.save_icon);
        }else
        {
            ivSaved.setImageResource(R.drawable.ic_saved_image);
        }

        llLike.setOnClickListener(v ->
                {
                    postClickListener.selectLike(position,"");
                }
                );

        ivOption.setOnClickListener(v ->
                {
                    postClickListener.bottomSheet(v,"",false,position);
                }
                );

        tvLikeCount.setText(postList.get(position).getLikeCount());

        ivSaved.setOnClickListener(v ->
                {
                    postClickListener.savePost(v,"",false,position);
                }
                );
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
