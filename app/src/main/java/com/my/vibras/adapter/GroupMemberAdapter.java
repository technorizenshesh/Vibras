package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetGroupDetails;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<SuccessResGetGroupDetails.GroupMembersDetail> modelList;

    public GroupMemberAdapter(Context context, ArrayList<SuccessResGetGroupDetails.GroupMembersDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_member, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetGroupDetails.GroupMembersDetail model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            TextView tvUserName = holder.itemView.findViewById(R.id.tvUserName);
            CircleImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);

            tvUserName.setText(StringEscapeUtils.unescapeJava(model.getUserName()));

            Glide.with(mContext)
                    .load(model.getImage())
                    .into(ivProfile);


        }
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
    }

    private SuccessResGetGroupDetails.GroupMembersDetail getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetGroupDetails.GroupMembersDetail model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}

