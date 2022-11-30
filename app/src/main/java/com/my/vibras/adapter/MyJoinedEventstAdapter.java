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
import com.my.vibras.act.EventsDetailsScreen;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResMyJoinedEvents;

import java.util.ArrayList;

public class MyJoinedEventstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResMyJoinedEvents.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public MyJoinedEventstAdapter(Context context, ArrayList<SuccessResMyJoinedEvents.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResMyJoinedEvents.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_view_all_event, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResMyJoinedEvents.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            ImageView ivEvent = holder.itemView.findViewById(R.id.ivEvent);
            TextView tvName = holder.itemView.findViewById(R.id.tvName);
            TextView tvLocation = holder.itemView.findViewById(R.id.tvLocation);
            TextView tvDate = holder.itemView.findViewById(R.id.tvDate);

            Glide.with(mContext)
                    .load(modelList.get(position).getEventDetails().getImage())
                    .into(ivEvent);
            tvName.setText(modelList.get(position).getEventDetails().getEventName());
            tvLocation.setText(modelList.get(position).getEventDetails().getAddress());
            tvDate.setText(modelList.get(position).getEventDetails().getDateTime());
            ivEvent.setOnClickListener(v ->
                    {
                        mContext.startActivity(new Intent(mContext, EventsDetailsScreen.class).putExtra("data",new Gson().toJson(modelList.get(position))));
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

    private SuccessResMyJoinedEvents.Result getItem(int position) {
        return modelList.get(position);
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

                    int position = getAbsoluteAdapterPosition();




                }
            });
        }
    }

}

