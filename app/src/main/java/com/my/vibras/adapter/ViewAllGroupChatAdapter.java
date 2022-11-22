package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.ui.GroupDetailAct;
import com.my.vibras.model.SuccessResGetGroup;
import com.my.vibras.model.SuccessResGetGroup.Result;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllGroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<Result> modelList;
    private OnItemClickListener mItemClickListener;

    public ViewAllGroupChatAdapter(Context context, ArrayList<Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_all_group, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            TextView tvView = holder.itemView.findViewById(R.id.tvView);

            CircleImageView ivProfile  = holder.itemView.findViewById(R.id.ivGroup);
            TextView tvGroupName  = holder.itemView.findViewById(R.id.tvGroupName);
            tvGroupName.setText(model.getGroupName());
            Glide.with(mContext).load(model.getGroupImage()).into(ivProfile);
            RecyclerView recyc_members = holder.itemView.findViewById(R.id.recyc_members);
            List<SuccessResGetGroup.GroupMembersDetail> modelList = new ArrayList<>();
            if ( model.getGroupMembersDetail().size()>=1) {
                modelList = model.getGroupMembersDetail();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                GroupHintedMembersAdapter groupHintedMembersAdapter = new
                        GroupHintedMembersAdapter(mContext, modelList);
                recyc_members.setAdapter(groupHintedMembersAdapter);
                recyc_members.setLayoutManager(linearLayoutManager);
                recyc_members.hasFixedSize();
                recyc_members.hasNestedScrollingParent();
            }
            tvView.setOnClickListener(v ->
                    {
                        mContext.startActivity(new Intent(mContext, GroupDetailAct.class).putExtra("id",model.getId()));
                    }
                    );

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

    private Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}

