package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
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
import com.my.vibras.model.SuccessResGetNotification;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetNotification.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public NotificationAdapter(Context context, ArrayList<SuccessResGetNotification.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            try {

                TextView tvMessage =   holder.itemView.findViewById(R.id.tvNotiMessage);
                CircleImageView ivProfile =   holder.itemView.findViewById(R.id.ivProfile);
                ImageView ivPost =   holder.itemView.findViewById(R.id.ivPost);

                tvMessage.setText(modelList.get(position).getMessage());

                Glide.with(mContext)
                        .load(modelList.get(position).getUserImage())
                        .placeholder(mContext.getDrawable(R.drawable.circle_gray))
                        .into(ivProfile);

                Glide.with(mContext)
                        .load(modelList.get(position).getPostImage())
                        .into(ivPost);

                if(modelList.get(position).getType().equalsIgnoreCase("Like"))
                {

                    ivPost.setVisibility(View.VISIBLE);

                } else
                {
                    ivPost.setVisibility(View.GONE);
                }

            }catch (NullPointerException e)
            {

            }catch (Exception e)
            {

            }

        }
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
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

        }
    }

}

