package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetSubscription;

import java.util.ArrayList;

/**
 * Created by Ravindra Birla on 29,November,2022
 */
   public  class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SuccessResGetSubscription.Result> modelList;
    private SubscriptionAdapter.OnItemClickListener mItemClickListener;

    public SubscriptionAdapter(Context context, ArrayList<SuccessResGetSubscription.Result>

            modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SuccessResGetSubscription.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate
                (R.layout.item_plan, viewGroup, false);

        return new SubscriptionAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof SubscriptionAdapter.ViewHolder) {
            final SuccessResGetSubscription.Result model = getItem(position);
            final SubscriptionAdapter.ViewHolder genericViewHolder = (SubscriptionAdapter.ViewHolder) holder;

            TextView PlanName = holder.itemView.findViewById(R.id.PlanName);
            TextView tvMoney = holder.itemView.findViewById(R.id.tvMoney);
            TextView tvService = holder.itemView.findViewById(R.id.tvService);

            PlanName.setText(model.getName());
            tvMoney.setText(model.getMonthlyPrice()+"  /Month");
            tvService.setText(Html.fromHtml(model.getDescription()));

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final SubscriptionAdapter.OnItemClickListener
                                               mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SuccessResGetSubscription.Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,
                         SuccessResGetSubscription.Result model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

            //    this.txtName=itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(),
                            modelList.get(getAdapterPosition()));

                }
            });
        }
    }
}

