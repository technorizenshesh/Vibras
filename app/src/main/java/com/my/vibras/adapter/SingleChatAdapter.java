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
import com.my.vibras.model.SuccessResGetChat;
import com.my.vibras.model.SuccessResGetConversation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private ArrayList<SuccessResGetConversation.Result> modelList;

    private OnItemClickListener mItemClickListener;

    public SingleChatAdapter(Context context, ArrayList<SuccessResGetConversation.Result> modelList,OnItemClickListener mItemClickListener) {
        this.mContext = context;
        this.modelList = modelList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_cht, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SuccessResGetConversation.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            TextView tvUserName = holder.itemView.findViewById(R.id.tvUserName);
            TextView tvLastMessage = holder.itemView.findViewById(R.id.tvLastMessage);
            ImageView ivDelete = holder.itemView.findViewById(R.id.ivDelete);
            CircleImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);

            tvUserName.setText(model.getFirstName()+" "+model.getLastName());

            tvLastMessage.setText(model.getLastMessage());

            Glide.with(mContext)
                    .load(model.getImage())
                    .into(ivProfile);

            ivDelete.setOnClickListener(v ->
                    {
                        mItemClickListener.onItemClick(v,position,model);
                    }
                    );
        }
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
    }

    private SuccessResGetConversation.Result getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, SuccessResGetConversation.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

        //    this.txtName=itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });
        }
    }

}

