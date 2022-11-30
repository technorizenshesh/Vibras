package com.my.vibras.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetCard;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetCard.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private int selectedPosition = -1;

    public PaymentAdapter(Context context, ArrayList<SuccessResGetCard.Result> modelList,OnItemClickListener mItemClickListener) {
        this.mContext = context;
        this.modelList = modelList;
        this.mItemClickListener = mItemClickListener;
    }

    public void updateList(ArrayList<SuccessResGetCard.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_payments, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view

        if (holder instanceof ViewHolder) {
            final SuccessResGetCard.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            TextView tvCardHolderName = holder.itemView.findViewById(R.id.tvCardHolderName);

            RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

            TextView tvCadNumber = holder.itemView.findViewById(R.id.tvCadNumber);

            TextView tvExpiry = holder.itemView.findViewById(R.id.tvExpiry);

            ImageView ivDelete = holder.itemView.findViewById(R.id.ivDelete);

            if(position == selectedPosition)
            {
                rlParent.setBackgroundResource(R.drawable.light_blue_fill);
            }
            else
            {
                rlParent.setBackgroundResource(R.drawable.light_gray_fill);
            }

            rlParent.setOnClickListener(v ->
                    {
                        selectedPosition = position;
                        mItemClickListener.onItemClick(v,position,model);
                        notifyDataSetChanged();
                    }
                    );

            ivDelete.setOnClickListener(v ->
                    {
                        mItemClickListener.deleteCard(v,position);
                    }
                    );

            tvCardHolderName.setText(model.getCardName());
            tvCadNumber.setText(modelList.get(position).getCardNum());
            tvExpiry.setText("Expiry :"+model.getCardMonth()+"/"+model.getCardExp());
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

    private SuccessResGetCard.Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, SuccessResGetCard.Result model);
        void deleteCard(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ViewHolder(final View itemView) {
          super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }
}