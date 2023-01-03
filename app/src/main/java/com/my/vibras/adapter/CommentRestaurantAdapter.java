package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.CommentItemBinding;
import com.my.vibras.model.SuccessResGetRestaurantComment;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class CommentRestaurantAdapter extends RecyclerView.Adapter<CommentRestaurantAdapter.StoriesViewHolder> {

    private Context context;

    CommentItemBinding binding;

    private List<SuccessResGetRestaurantComment.Result> commentList;

    public CommentRestaurantAdapter(Context context, List<SuccessResGetRestaurantComment.Result> commentList)
    {
      this.context = context;
      this.commentList = commentList;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CommentItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView imageView;
        TextView tvName,tvComment,tvTimeAgo;
        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);
        imageView = holder.itemView.findViewById(R.id.iv_history);
        tvName = holder.itemView.findViewById(R.id.tv_name);
        tvComment = holder.itemView.findViewById(R.id.tv_comment);
        tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);
        Glide.with(context)
                .load(commentList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(imageView);
        tvName.setText(  StringEscapeUtils.unescapeJava(commentList.get(position).getFirstName()));
        tvComment.setText( StringEscapeUtils.unescapeJava (commentList.get(position).getComment()));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {
        public StoriesViewHolder(CommentItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
