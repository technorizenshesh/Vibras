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
import com.my.vibras.model.SuccessResGetEvents;

import java.util.ArrayList;

public class NEarmeEventstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetEvents.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public NEarmeEventstAdapter(Context context, ArrayList<SuccessResGetEvents.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResGetEvents.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_near_me, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetEvents.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            ImageView ivEvent = holder.itemView.findViewById(R.id.ivEvent);
            TextView tvName = holder.itemView.findViewById(R.id.tvName);
            TextView tvLocation = holder.itemView.findViewById(R.id.tvLocation);
            TextView tvDate = holder.itemView.findViewById(R.id.tvDate);

            Glide.with(mContext)
                    .load(modelList.get(position).getImage())
                    .into(ivEvent);
            tvName.setText(model.getEventName());
            tvLocation.setText(model.getAddress());
            tvDate.setText(model.getDateTimeEvent());


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

    private SuccessResGetEvents.Result getItem(int position) {
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



                }
            });
        }
    }

}

