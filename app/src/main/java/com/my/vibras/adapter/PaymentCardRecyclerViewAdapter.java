package com.my.vibras.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetInterest;

import java.util.ArrayList;


public class PaymentCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetInterest.Result> modelList;
    private OnItemClickListener mItemClickListener;

    boolean isClick=false;

    public PaymentCardRecyclerViewAdapter(Context context, ArrayList<SuccessResGetInterest.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_like_intrested, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetInterest.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            Glide.with(mContext)
                    .load(model.getImage())
                    .into(genericViewHolder.ivInterest);

            genericViewHolder.txtName.setText(model.getName());
            genericViewHolder.txtNameOne.setText(model.getName());

            genericViewHolder.cardView.setBackgroundColor(model.isSelected() ? Color.BLACK : Color.GRAY);

            if(model.isSelected())
            {

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    genericViewHolder.cardView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.black_cornors_5) );
                } else {
                    genericViewHolder.cardView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.black_cornors_5));
                }

//                genericViewHolder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.black));
//                genericViewHolder.cardView.setBackgroundColor(R.color.black);
                genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.white));

//                genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            }else
            {

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    genericViewHolder.cardView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.gray_cornors_5) );
                } else {
                    genericViewHolder.cardView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.gray_cornors_5));
                }
//                genericViewHolder.cardView.setBackgroundResource(R.color.lightgray);
                genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.black));

//                genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.black));

//                genericViewHolder.txtName.getCompoundDrawables()[0].setTint(Color.BLACK);
            }

            genericViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setSelected(!model.isSelected());
                    if(model.isSelected())
                    {

                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            genericViewHolder.cardView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.black_cornors_5) );
                        } else {
                            genericViewHolder.cardView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.black_cornors_5));
                        }

//                        genericViewHolder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.black));
                        genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//                        genericViewHolder.txtName.getCompoundDrawables()[0].setTint(Color.WHITE);

//                        genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                        genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.white));


                    }else
                    {

                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            genericViewHolder.cardView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.gray_cornors_5) );
                        } else {
                            genericViewHolder.cardView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.gray_cornors_5));
                        }

//                        genericViewHolder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.lightgray));
                        genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//                        genericViewHolder.txtName.getCompoundDrawables()[0].setTint(Color.BLACK);

                        genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.black));

//                        genericViewHolder.ivInterest.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                    }
                   // genericViewHolder.cardView.setBackgroundColor(model.isSelected() ? Color.BLACK : Color.GRAY);
                }
            });
        }
    }
    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SuccessResGetInterest.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetInterest.Result model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtNameOne;
        ImageView ivInterest;
        RelativeLayout cardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtNameOne=itemView.findViewById(R.id.txtNameOne);
            cardView=itemView.findViewById(R.id.cardView);
            ivInterest = itemView.findViewById(R.id.ivInterest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });
        }
    }


}

