package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetUsers;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private OnItemClickListener mItemClickListener;

    private ArrayList<SuccessResGetUsers.Result> usersList ;
    public FriendsListAdapter(Context context,ArrayList<SuccessResGetUsers.Result> usersList) {
        this.mContext = context;
        this.usersList = usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frndlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            ImageView ivUserProfile = holder.itemView.findViewById(R.id.ivUserProfile);
            TextView tvUsername = holder.itemView.findViewById(R.id.tvUsername);
            TextView tvViewProfile = holder.itemView.findViewById(R.id.tvViewProfile);

            Log.d("TAG", "onBindViewHolder: "+usersList.get(position).getImage());

            Glide.with(mContext)
                    .load(usersList.get(position).getImage())
                    .into(ivUserProfile);

            tvUsername.setText(usersList.get(position).getFirstName()+" "+usersList.get(position).getFirstName());

        }
    }

    @Override
    public int getItemCount()
    {
        return usersList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, HomModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

        //    this.txtName=itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

}
