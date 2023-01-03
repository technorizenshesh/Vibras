package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.vibras.R;
import com.my.vibras.databinding.SearchItemBinding;
import com.my.vibras.model.SuccessResGetConversation;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class ConversationSearchAdapter extends RecyclerView.Adapter<ConversationSearchAdapter.StoriesViewHolder> {

    private Context context;

    SearchItemBinding binding;

    private List<SuccessResGetConversation.Result> usersList;

    public ConversationSearchAdapter(Context context, List<SuccessResGetConversation.Result> usersList)
    {
      this.context = context;
      this.usersList = usersList;

    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SearchItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName;
        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvUserName.setText((usersList.get(position).getFirstName())+" "+
                (usersList.get(position).getLastName()));

        Glide
                .with(context)
                .load(usersList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        circleImageView.setOnClickListener(v ->
                {

                }
                );


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(SearchItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
