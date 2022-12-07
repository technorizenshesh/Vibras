package com.my.vibras.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.FriendProfileActivity;
import com.my.vibras.chat.ChatInnerMessagesActivity;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.utility.HomeItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeUsersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder
        > {

    private Context mContext;
    private ArrayList<SuccessResGetUsers.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private HomeItemClickListener homeItemClickListener;
    boolean isClick=false;

    public HomeUsersRecyclerViewAdapter(Context context,ArrayList<SuccessResGetUsers.Result> modelList,HomeItemClickListener homeItemClickListener) {
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
        TextView txtName,tvDistance,tvOnlineStatus;
        ImageView ivFull,ivOtherLike,ivProfileLike,ivChat,send_msg,ivFire;
        CircleImageView smallImage;
        txtName = holder.itemView.findViewById(R.id.tvUserName);
        tvOnlineStatus = holder.itemView.findViewById(R.id.tvOnlineStatus);
        tvDistance = holder.itemView.findViewById(R.id.tvDistance);
        smallImage = holder.itemView.findViewById(R.id.icSmallProfile);
        ivFull = holder.itemView.findViewById(R.id.ivUser);
        ivOtherLike = holder.itemView.findViewById(R.id.ivOtherLike);
        ivProfileLike = holder.itemView.findViewById(R.id.ivLike);
        ivChat = holder.itemView.findViewById(R.id.ivChat);
        ivFire = holder.itemView.findViewById(R.id.ivFire);
        ivProfileLike.setOnClickListener(v ->
                {
                  homeItemClickListener.addLikeToUser(position);
                }
                );

        if(modelList.get(position).getOnlineStatus().equalsIgnoreCase("1"))
        {
            tvOnlineStatus.setText("online");
        } else
        {
            tvOnlineStatus.setText("offline");
        }

        tvDistance.setText(modelList.get(position).getDistance()+" kms away");
        ivFire.setOnClickListener(v ->
                {
                  homeItemClickListener.addCommentToUser(position);
                }
                );

        if(modelList.get(position).getUserMatch().equalsIgnoreCase("Matched"))
        {
            ivChat.setVisibility(View.GONE);
        }
        else
        {
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
                    intent.putExtra("friend_name", modelList.get(position).getFirstName()+
                            " "+modelList.get(position).getLastName());
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
                .into(ivFull);
        Glide.with(mContext)
                .load(modelList.get(position).getImage())
                .into(smallImage);
        txtName.setText(modelList.get(position).getFirstName()+" "+modelList.get(position).getLastName());
        ivOtherLike.setOnClickListener(v ->
                {
                    homeItemClickListener.addUserProfileLike(position);
                }
                );
        smallImage.setOnClickListener(v ->
                {
                /*    Bundle bundle1 = new Bundle();
                    bundle1.putString("id",modelList.get(position).getId());
                    Navigation.findNavController(v)
                            .navigate(R.id.action_navigation_home_to_otherUserProfileFragment,bundle1);
*/

                    Intent intent = new Intent(mContext, FriendProfileActivity.class);
                    intent.putExtra("id", modelList.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                }
        );

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

