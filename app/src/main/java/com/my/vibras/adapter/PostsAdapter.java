package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.my.vibras.R;
import com.my.vibras.act.AddCommentAct;
import com.my.vibras.databinding.PostItemBinding;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.utility.PostClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.StoriesViewHolder> {

    private Context context;
    PostItemBinding binding;
    private List<SuccessResGetPosts.Result> postList;
    private PostClickListener postClickListener;
    String From = "";

    public PostsAdapter(Context context, List<SuccessResGetPosts.Result> postList,
                        PostClickListener postClickListener, String from) {
        this.From = from;
        this.context = context;
        this.postList = postList;
        this.postClickListener = postClickListener;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PostItemBinding.inflate(LayoutInflater.from(context));
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
                    context.startActivity(new Intent(context, AddCommentAct.class).putExtra("postId", postList.get(position).getId()));
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
        tvDescription.setText(postList.get(position).getDescription());
        //    tvDistance.setText(postList.get(position).get());
        tvUserName.setText(postList.get(position).getFirstName() + " "
                + postList.get(position).getLastName());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(PostItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
