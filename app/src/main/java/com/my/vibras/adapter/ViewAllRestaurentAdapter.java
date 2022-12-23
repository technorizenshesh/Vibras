package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.RestaurantDetailAct;
import com.my.vibras.model.SuccessResMyRestaurantRes;

import java.util.ArrayList;

public class ViewAllRestaurentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SuccessResMyRestaurantRes.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public ViewAllRestaurentAdapter(Context context, ArrayList<SuccessResMyRestaurantRes.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResMyRestaurantRes.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_viewall_rest, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResMyRestaurantRes.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            ImageView ivRestaurants = holder.itemView.findViewById(R.id.ivRestaurant);
            TextView tvName = holder.itemView.findViewById(R.id.tvName);
            TextView tvDistance = holder.itemView.findViewById(R.id.tvDistance);

            ivRestaurants.setOnClickListener(v ->
                    {
                        mContext.startActivity(new Intent(mContext, RestaurantDetailAct.class).putExtra("data", new Gson().toJson(modelList.get(position))));
                    }
            );

            Glide.with(mContext)
                    .load(model.getImage())
                    .into(ivRestaurants);

            tvName.setText(model.getRestaurantName());
            tvDistance.setText(model.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SuccessResMyRestaurantRes.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResMyRestaurantRes.Result model);

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

