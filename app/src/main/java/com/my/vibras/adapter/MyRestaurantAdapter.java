package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.AddRestaurantCommentAct;
import com.my.vibras.databinding.MyeventItemBinding;
import com.my.vibras.model.SuccessResMyRestaurantRes;
import com.my.vibras.utility.PostClickListener;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class MyRestaurantAdapter extends RecyclerView.Adapter<MyRestaurantAdapter.StoriesViewHolder> {

    private Context context;
    MyeventItemBinding binding;
    private List<SuccessResMyRestaurantRes.Result> postList;
    private PostClickListener postClickListener;

    public MyRestaurantAdapter(Context context, List<SuccessResMyRestaurantRes.Result> postList, PostClickListener postClickListener)
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

        TextView tvChat = holder.itemView.findViewById(R.id.tvChat);

        TextView tvDescription = holder.itemView.findViewById(R.id.timeAgo);

        ImageView ivSaved = holder.itemView.findViewById(R.id.ivSaved);

        TextView tvLikeCount = holder.itemView.findViewById(R.id.tvLikeCount);

        LinearLayoutCompat linearLayoutCompat = holder.itemView.findViewById(R.id.llLike);

        ImageView ivLikeUnlike = holder.itemView.findViewById(R.id.ivLikeUnlike);

        ImageView ivOption = holder.itemView.findViewById(R.id.ivOption);

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        String image = postList.get(position).getImage();

        Glide.with(context)
                .load(image)
                .into(ivPOst);

        Glide.with(context)
                .load(postList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(circleImageView);

        tvDescription.setText(postList.get(position).getTimeAgo());

        tvUserName.setText(  (postList.get(position).getRestaurantName()));

        linearLayoutCompat.setOnClickListener(v ->
                {
                    postClickListener.selectLike(position,"");
                }
                );

        if(postList.get(position).getLikeStatus().equalsIgnoreCase("false"))
        {
            ivLikeUnlike.setImageResource(R.drawable.ic_rest_unlike);
        }else
        {
            ivLikeUnlike.setImageResource(R.drawable.ic_rest_like);
        }

        if(postList.get(position).getSavePost().equalsIgnoreCase("No"))
        {
            ivSaved.setImageResource(R.drawable.save_icon);
        }else
        {
            ivSaved.setImageResource(R.drawable.ic_saved_image);
        }

        ivOption.setOnClickListener(v ->
                {
                    postClickListener.bottomSheet(v,"",false,position);
                }
                );

        ivSaved.setOnClickListener(v ->
                {
                    postClickListener.savePost(v,"",false,position);
                }
                );

        tvChat.setOnClickListener(v ->
                {
                    context.startActivity(new Intent(context, AddRestaurantCommentAct.class).putExtra("postId",postList.get(position).getId()));
                }
        );

        tvLikeCount.setText(postList.get(position).getResCountLike());

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
