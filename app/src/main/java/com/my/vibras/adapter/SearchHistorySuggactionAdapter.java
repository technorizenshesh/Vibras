package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.vibras.R;
import com.my.vibras.databinding.ItemSearchSuggactionBinding;
import com.my.vibras.model.SuccessSearchHistoryRes;
import com.my.vibras.utility.SuggactionClick;

import java.util.List;

/**
 * Created by Ravindra Birla on 29,November,2022
 */
public class SearchHistorySuggactionAdapter extends RecyclerView.
        Adapter<SearchHistorySuggactionAdapter.StoriesViewHolder> {
    private Context context;
    ItemSearchSuggactionBinding binding;
    private List<SuccessSearchHistoryRes.Result> postList;
    private SuggactionClick suggactionClick;
    String From = "";

    public SearchHistorySuggactionAdapter(Context context, List<SuccessSearchHistoryRes.Result> postList,
                                          SuggactionClick postClickListener) {
        this.context = context;
        this.postList = postList;
        this.suggactionClick = postClickListener;
    }

    @NonNull
    @Override
    public SearchHistorySuggactionAdapter.StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSearchSuggactionBinding.inflate(LayoutInflater.from(context));
        return new SearchHistorySuggactionAdapter.StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistorySuggactionAdapter.StoriesViewHolder holder, int position) {
        TextView tvDescription = holder.itemView.findViewById(R.id.list_item);
        tvDescription.setText(" "+postList.get(position).getSearch()+" ");
        holder.itemView.setOnClickListener(v -> {
            suggactionClick.suggactionClick(postList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(ItemSearchSuggactionBinding itemView) {
            super(itemView.getRoot());
        }
    }
}