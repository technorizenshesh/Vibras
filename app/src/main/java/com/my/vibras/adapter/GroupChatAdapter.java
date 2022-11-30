package com.my.vibras.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.chat.GroupChat.GroupChatInnerActivity;
import com.my.vibras.model.SuccessResGetGroup;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;

import static com.my.vibras.retrofit.Constant.USER_ID;

public class GroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetGroup.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private OnItemClickListener itemClickListener;

    public GroupChatAdapter(Context context, ArrayList<SuccessResGetGroup.Result> modelList,OnItemClickListener itemClickListener) {
        this.mContext = context;
        this.modelList = modelList;
        this.itemClickListener = itemClickListener;
    }

    public void updateList(ArrayList<SuccessResGetGroup.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_grp_cht, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetGroup.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            ImageView ivCancel = holder.itemView.findViewById(R.id.ivCancel);

            String userId = SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);

            if(userId.equalsIgnoreCase(modelList.get(position).getUserId()))
            {
                ivCancel.setVisibility(View.VISIBLE);
            }
            else
            {
                ivCancel.setVisibility(View.GONE);
            }

            LinearLayout llParent = holder.itemView.findViewById(R.id.llParent);

            llParent.setOnClickListener(v ->
                    {
                        mContext.startActivity(new Intent(mContext, GroupChatInnerActivity.class).
                                putExtra("id",modelList.get(position).getId())
                                .putExtra("name",modelList.get(position).getGroupName())
                        );
                    }
                    );

            ivCancel.setOnClickListener(v ->
                    {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Remove group")
                                .setMessage("Are you sure you want to delete this group?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation

                                        itemClickListener.onItemClick(v,position,model);

                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    );

            ImageView ivProfile  = holder.itemView.findViewById(R.id.ivGroup);
            Glide.with(mContext).load(model.getGroupImage()).into(ivProfile);
            TextView tvGroupName  = holder.itemView.findViewById(R.id.tvGroupName);
            tvGroupName.setText(model.getGroupName());
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

    private SuccessResGetGroup.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetGroup.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

        }
    }

}

