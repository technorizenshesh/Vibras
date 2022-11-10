package com.my.vibras.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.my.vibras.R;
import com.my.vibras.model.SuccessResGetHelp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetHelp.Result> faqsList;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;
    private boolean showAns = false;

    public FaqsAdapter(Context context, ArrayList<SuccessResGetHelp.Result> faqsList)
    {
        this.context = context;
        this.faqsList = faqsList;
    }


    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.faqs_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        TextView tvQues = holder.itemView.findViewById(R.id.tvQs);
        TextView tvAnswer = holder.itemView.findViewById(R.id.tvAns);
        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        ImageView ivAdd =  holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus =  holder.itemView.findViewById(R.id.minus);

        tvQues.setText(faqsList.get(position).getTitle());
        tvAnswer.setText(Html.fromHtml(faqsList.get(position).getDescription()));

        rlShiftsNote.setOnClickListener(v ->
                {
                    // showShiftsNotes(postedList.get(position).getShiftNotes());
                    showAns = !showAns;
                    if(showAns)
                    {
                        tvAnswer.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        ivMinus.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvAnswer.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                        ivMinus.setVisibility(View.GONE);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return faqsList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
