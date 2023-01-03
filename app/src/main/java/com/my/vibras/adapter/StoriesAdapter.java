package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.StoryDetailAct;
import com.my.vibras.act.ui.home.HomeFragment;
import com.my.vibras.model.SuccessResGetStories;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

 
/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SuccessResGetStories.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public StoriesAdapter(Context context, ArrayList<SuccessResGetStories.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResGetStories.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetStories.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txtName.setText(  StringEscapeUtils.unescapeJava(model.getUserName()));
            Glide.with(mContext)
                    .load(model.getUserImage())
                    .into(genericViewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SuccessResGetStories.Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetStories.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        ImageView image;
        CardView cvParent;

        public ViewHolder(final View itemView) {
            super(itemView);
            cvParent = itemView.findViewById(R.id.cvParent);
            txtName = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HomeFragment.story = modelList.get(getAdapterPosition());
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(),
                            modelList.get(getAdapterPosition()));
                    Intent intent = new Intent(mContext, StoryDetailAct.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

}

