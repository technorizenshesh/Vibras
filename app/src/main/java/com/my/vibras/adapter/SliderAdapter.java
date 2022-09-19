package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetBanner;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 19,June,2021
 */
public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {


    private final Context context;

    private List<SuccessResGetBanner.Result> myProductModeList = new ArrayList<>();

    public SliderAdapter(Context context, List<SuccessResGetBanner.Result> myProductModeListls) {
        this.context = context;
        this.myProductModeList = myProductModeListls;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Glide.with(viewHolder.itemView)
                .load(myProductModeList.get(position).getImage())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return myProductModeList.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {

            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;

        }
    }

}
