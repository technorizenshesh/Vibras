package com.my.vibras.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.act.GetEventsByCategoryIdAct;
import com.my.vibras.model.SuccessResGetCategory;

import java.util.ArrayList;

public class BruseEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetCategory.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public BruseEventAdapter(Context context, ArrayList<SuccessResGetCategory.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResGetCategory.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_brouse_events, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetCategory.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            ImageView ivCategory = holder.itemView.findViewById(R.id.ivCategory);

            LinearLayout llParent = holder.itemView.findViewById(R.id.llParent);

            TextView tvCategory = holder.itemView.findViewById(R.id.tvEventName);

            Glide.with(mContext)
                    .load(model.getIcon())
                    .into(ivCategory);

            tvCategory.setText(model.getName());

            llParent.setOnClickListener(v ->
                    {
                        mContext.startActivity(new Intent(mContext, GetEventsByCategoryIdAct.class).putExtra("id",model.getId())
                                .putExtra("name",model.getName())
                        );
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

    private SuccessResGetCategory.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetCategory.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);


        //    this.txtName=itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });
        }
    }

}

