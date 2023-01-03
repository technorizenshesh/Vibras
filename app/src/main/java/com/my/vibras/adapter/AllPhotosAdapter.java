package com.my.vibras.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.my.vibras.R;
import com.my.vibras.act.AddCommentAct;
import com.my.vibras.databinding.AllPostItemBinding;
import com.my.vibras.databinding.PostItemBinding;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.utility.PostClickListener;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllPhotosAdapter extends RecyclerView.Adapter<AllPhotosAdapter.StoriesViewHolder> {

    private Context context;
    AllPostItemBinding binding;
    private List<SuccessResGetPosts.Result> postList;
    private PostClickListener postClickListener;
    String From = "";

    public AllPhotosAdapter(Context context, List<SuccessResGetPosts.Result> postList,
                            PostClickListener postClickListener, String from) {
        this.From = from;
        this.context = context;
        this.postList = postList;
        this.postClickListener = postClickListener;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AllPostItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        ImageView ivPOst = holder.itemView.findViewById(R.id.ivPost);
        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName = holder.itemView.findViewById(R.id.tvUserName);
        TextView tvDescription = holder.itemView.findViewById(R.id.tvDescription);
        TextView tvDistance = holder.itemView.findViewById(R.id.tvDistance);

        ImageView ivLike = holder.itemView.findViewById(R.id.ivLike);
        ImageView ivComment = holder.itemView.findViewById(R.id.ivComment);
        ImageView ivMore = holder.itemView.findViewById(R.id.ivMore);


        holder.itemView.setOnClickListener(v->
        {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_NoActionBar);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(context.getResources().getColor(R.color.blue));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.customview);
            dialog.show();
            ImageView imagewshow = (ImageView) dialog.findViewById(R.id.imagewshow);
            RelativeLayout closeimage = (RelativeLayout) dialog.findViewById(R.id.closeimage);
            closeimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            Glide.with(context)
                    .load(postList.get(position).getImage())
                    .into(imagewshow);
        }
       );
        // creating a variable for exoplayerview.
        SimpleExoPlayerView exoPlayerView= holder.itemView.findViewById(R.id.idExoPlayerVIew);
exoPlayerView.setVisibility(View.GONE);
        // creating a variable for exoplayer
        SimpleExoPlayer exoPlayer;
        if (From.equalsIgnoreCase("Mine")) {
            ivMore.setVisibility(View.VISIBLE);
        } else {
            ivMore.setVisibility(View.GONE);

        }
        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        if (postList.get(position).getLikeStatus().equalsIgnoreCase("1")) {
            ivLike.setImageResource(R.drawable.ic_like_filled);
        } else {
            ivLike.setImageResource(R.drawable.like_new);
        }
        ivMore.setOnClickListener(v -> {
                    postClickListener.bottomSheet(v, postList.get(position).getId(), false, position);
                }
        );

        ivComment.setOnClickListener(v ->
                {
                    context.startActivity(new Intent(context, AddCommentAct.class).
                            putExtra("postId", postList.get(position).getId()));
                }
        );
        ivLike.setOnClickListener(v ->
                {
                    postClickListener.selectLike(position, "");
                }
        );
        if (postList.get(position).getTypeStatus().equalsIgnoreCase("image")) {
            Glide.with(context)
                    .load(postList.get(position).getImage())
                    .into(ivPOst);
            exoPlayerView.setVisibility(View.GONE);
            ivPOst.setVisibility(View.VISIBLE);
        } else {
        }

        Glide.with(context)
                .load(postList.get(position).getImageuser())
                .placeholder(R.drawable.ic_user)
                .into(circleImageView);
        tvDescription.setText( StringEscapeUtils.unescapeJava(postList.get(position).getDescription()));
        //    tvDistance.setText(postList.get(position).get());
        tvUserName.setText(    StringEscapeUtils.unescapeJava(postList.get(position).getFirstName()) + " "
                +  StringEscapeUtils.unescapeJava ( postList.get(position).getLastName()));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(AllPostItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
