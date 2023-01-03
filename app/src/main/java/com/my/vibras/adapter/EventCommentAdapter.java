package com.my.vibras.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.RestrauntCommentItemBinding;
import com.my.vibras.model.SuccessResGetEventComment;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

 

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class EventCommentAdapter extends RecyclerView.Adapter<EventCommentAdapter.StoriesViewHolder> {

    private Context context;

    RestrauntCommentItemBinding binding;

    private List<SuccessResGetEventComment.Result> commentList;

    public EventCommentAdapter(Context context, List<SuccessResGetEventComment.Result> commentList)
    {
      this.context = context;
      this.commentList = commentList;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RestrauntCommentItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView imageView;

        TextView tvName,tvComment,tvTimeAgo,tvRating;

        RatingBar ratingBar;

        imageView = holder.itemView.findViewById(R.id.ivProfile);
        tvName = holder.itemView.findViewById(R.id.tvUserName);
        tvComment = holder.itemView.findViewById(R.id.tvComment);
        tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);
        tvRating = holder.itemView.findViewById(R.id.tvRating);
        ratingBar = holder.itemView.findViewById(R.id.ratingBar);

        Glide.with(context)
                .load(commentList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(imageView);

        tvName.setText(  StringEscapeUtils.unescapeJava(commentList.get(position).getFirstName()));
        tvComment.setText(   StringEscapeUtils.unescapeJava(commentList.get(position).getComment()));
        tvRating.setText(commentList.get(position).getRating());

        try {
            ratingBar.setRating(Float.valueOf(commentList.get(position).getRating()));
        }catch (Exception e)
        {
            Log.d("TAG", "onBindViewHolder: "+e);
        }

        tvTimeAgo.setText(commentList.get(position).getTimeAgo());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {
        public StoriesViewHolder(RestrauntCommentItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
