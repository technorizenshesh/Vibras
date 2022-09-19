package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.utility.ImageCancelClick;

import java.util.ArrayList;

/**
 * Created by Ravindra Birla on 04,August,2021
 */
public class MultipleImagesAdapter extends RecyclerView.Adapter<MultipleImagesAdapter.OfferViewHolder> {

    private Context context;

    private ArrayList<String> imagesList;

    private ImageCancelClick imageCancelClick;

    public MultipleImagesAdapter(Context context, ArrayList<String> imagesList, ImageCancelClick imageCancelClick)
    {
        this.context = context;
        this.imagesList = imagesList;
        this.imageCancelClick = imageCancelClick;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.images_item, parent, false);
        OfferViewHolder viewHolder = new OfferViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        ImageView ivAnswer = holder.itemView.findViewById(R.id.ivAnswer);

        ImageView ivCancel = holder.itemView.findViewById(R.id.ivCancel);

        Glide.with(context)
                .load(imagesList.get(position))
                .into(ivAnswer);

        ivCancel.setOnClickListener(v ->
                {

                    imageCancelClick.imageCancel(position);

                }
                );

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public OfferViewHolder(View itemView) {
            super(itemView);
        }
    }

}
