package com.my.vibras.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.CommentItemBinding;
import com.my.vibras.model.SuccessResGetComment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.StoriesViewHolder> {

    private Context context;

    CommentItemBinding binding;

    private List<SuccessResGetComment.Result> commentList;

    public CommentAdapter(Context context, List<SuccessResGetComment.Result> commentList)
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
                .load(commentList.get(position).getUserDetial().getImage())
                .placeholder(R.drawable.ic_user)
                .into(imageView);

        tvName.setText(commentList.get(position).getUserDetial().getFirstName());
        tvComment.setText(decodeEmoji(commentList.get(position).getComment()));
        tvTimeAgo.setText(commentList.get(position).getTimeAgo());

    }


    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
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
