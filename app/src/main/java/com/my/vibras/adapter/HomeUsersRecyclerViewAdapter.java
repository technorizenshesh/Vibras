package com.my.vibras.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.my.vibras.R;
import com.my.vibras.act.FriendProfileActivity;
import com.my.vibras.chat.ChatInnerMessagesActivity;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.utility.HomeItemClickListener;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

 
public class HomeUsersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder
        > {

    private Context mContext;
    private ArrayList<SuccessResGetUsers.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private HomeItemClickListener homeItemClickListener;
    boolean isClick = false;

    public HomeUsersRecyclerViewAdapter(Context context, ArrayList<SuccessResGetUsers.Result> modelList, HomeItemClickListener homeItemClickListener) {
        this.mContext = context;
        this.modelList = modelList;
        this.homeItemClickListener = homeItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        TextView txtName, tvDistance, tvOnlineStatus;
        ImageView ivFull, ivOtherLike, ivProfileLike, ivChat, send_msg, ivFire;
        CircleImageView smallImage;
        ShimmerFrameLayout shimmer_view_container = holder.itemView.findViewById(R.id.shimmer_view_container);
        txtName = holder.itemView.findViewById(R.id.tvUserName);
        tvOnlineStatus = holder.itemView.findViewById(R.id.tvOnlineStatus);
        tvDistance = holder.itemView.findViewById(R.id.tvDistance);
        smallImage = holder.itemView.findViewById(R.id.icSmallProfile);
        ivFull = holder.itemView.findViewById(R.id.ivUser);
        ivOtherLike = holder.itemView.findViewById(R.id.ivOtherLike);
        ivProfileLike = holder.itemView.findViewById(R.id.ivLike);
        ivChat = holder.itemView.findViewById(R.id.ivChat);
        ivFire = holder.itemView.findViewById(R.id.ivFire);
        shimmer_view_container.startShimmer();
        ivProfileLike.setOnClickListener(v ->
                {
                    homeItemClickListener.addLikeToUser(position);
                }
        );

        if (modelList.get(position).getOnlineStatus().equalsIgnoreCase("1")) {
            tvOnlineStatus.setText("online");
        } else {
            tvOnlineStatus.setText("offline");
        }

        tvDistance.setText(modelList.get(position).getDistance() + " kms away");
        ivFire.setOnClickListener(v ->
                {
                    homeItemClickListener.addCommentToUser(position);
                }
        );

        if (modelList.get(position).getUserMatch().equalsIgnoreCase("Matched")) {
            ivChat.setVisibility(View.GONE);
        } else {
            ivChat.setVisibility(View.GONE);
        }

        ivChat.setOnClickListener(v ->
                {
               /*     mContext.startActivity(new Intent(mContext, ChatDetailsScreen.class)
                            .putExtra("id",modelList.get(position).getId())
                            .putExtra("name",modelList.get(position).getFirstName()+
                                    " "+modelList.get(position).getLastName()));*/

                    Intent intent = new Intent(mContext, ChatInnerMessagesActivity.class);
                    intent.putExtra("friend_id", modelList.get(position).getId());
                    intent.putExtra("friendimage", modelList.get(position).getImage());
                    intent.putExtra("friend_name", modelList.get(position).getFirstName() +
                            " " + modelList.get(position).getLastName());
                    intent.putExtra("last_message", "hii");
                    intent.putExtra("messagetime", "1");
                    intent.putExtra("status_check", modelList.get(position).getId());
                    intent.putExtra("id", modelList.get(position).getId());
                    intent.putExtra("onlinestatus", modelList.get(position).getImage());
                    intent.putExtra("unique_id", modelList.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);


                }
        );
        Glide.with(mContext)
                .load(modelList.get(position).getImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(
                        Glide
                                .with(mContext)
                                .load(modelList.get(position).getIdentify_image())).placeholder(R.color.gray1)
                .error(R.drawable.border_dark_gray)
                .into(ivFull);
        Glide.with(mContext)
                .load(modelList.get(position).getImage()).placeholder(R.color.gray1).error(R.drawable.border_dark_gray)
                .into(smallImage);

        if (modelList.get(position).getAdmin_approval().equalsIgnoreCase("Approved")) {
            txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_verified_white, 0);
        } else {
            txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

       /* txtName.setText(  (modelList.get(position).getFirstName())
                + " " +   (modelList.get(position).getLastName()));
     */
        try {
            txtName.setText(StringEscapeUtils.unescapeJava(modelList.get(position).getFirstName())
                    + " " + StringEscapeUtils.unescapeJava(modelList.get(position).getLastName()));
        }catch (Exception e){
            e.printStackTrace();
        }
        ivOtherLike.setOnClickListener(v ->
                {
                    homeItemClickListener.addUserProfileLike(position);
                }
        );
        smallImage.setOnClickListener(v ->
                {
                    Intent intent = new Intent(mContext, FriendProfileActivity.class);
                    intent.putExtra("id", modelList.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                }
        );
        shimmer_view_container.hideShimmer();
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SuccessResGetUsers.Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, SuccessResGetUsers.Result model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}

