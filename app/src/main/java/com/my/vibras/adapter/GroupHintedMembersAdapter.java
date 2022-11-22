package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 23,November,2022
 */
class GroupHintedMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<SuccessResGetGroup.GroupMembersDetail> modelList;

    public GroupHintedMembersAdapter(Context context, List<
            SuccessResGetGroup.GroupMembersDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public GroupHintedMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_member_hint, viewGroup, false);
        return new GroupHintedMembersAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof GroupHintedMembersAdapter.ViewHolder) {
            final SuccessResGetGroup.GroupMembersDetail model = modelList.get(position);
            final GroupHintedMembersAdapter.ViewHolder genericViewHolder =
                    (GroupHintedMembersAdapter.ViewHolder) holder;
            CircleImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile__);
            Glide.with(mContext)
                    .load(model.getImage())
                    .into(ivProfile);


        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    /* private SuccessResGetGroupDetails.GroupMembersDetail getItem(int position) {
         return modelList.get(position);
     }

     public interface OnItemClickListener {

         void onItemClick(View view, int position, SuccessResGetGroupDetails.GroupMembersDetail model);

     }
 */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}
