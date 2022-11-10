package com.my.vibras.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jackandphantom.blurimage.BlurImage;
import com.my.vibras.R;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResGetNotification;
import com.my.vibras.utility.BlurDrawable;
import com.my.vibras.utility.DeletePost;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetNotification.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private DeletePost deletePost;

    public NotificationAdapter(Context context, ArrayList<SuccessResGetNotification.Result> modelList,DeletePost deletePost) {
        this.mContext = context;
        this.modelList = modelList;
        this.deletePost = deletePost;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            try {
                LinearLayout llJoinGroup = holder.itemView.findViewById(R.id.llJoinGroup);
                TextView tvMessage =   holder.itemView.findViewById(R.id.tvNotiMessage);
                TextView tvAccept =   holder.itemView.findViewById(R.id.tvAccept);
                TextView tvReject =   holder.itemView.findViewById(R.id.tvReject);
                TextView tvtimeAgo =   holder.itemView.findViewById(R.id.tvTimeAgo);
                CircleImageView ivProfile =   holder.itemView.findViewById(R.id.ivProfile);
                ImageView ivPost =   holder.itemView.findViewById(R.id.ivPost);
                tvMessage.setText(modelList.get(position).getMessage());
                Glide.with(mContext)
                        .load(modelList.get(position).getPostImage())
                        .into(ivPost);

                tvReject.setOnClickListener(v ->
                        {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Reject Request")
                                    .setMessage("Are you sure you want to reject this request?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deletePost.bottomSheet(v,"Rejected",false,position);
                                            // Continue with delete operation
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                );


                tvAccept.setOnClickListener(v ->
                        {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Accept Request")
                                    .setMessage("Are you sure you want to accept this request?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deletePost.bottomSheet(v,"Accepted",false,position);
                                            // Continue with delete operation
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        );

                if(modelList.get(position).getStatus().equalsIgnoreCase("Unseen") && !modelList.get(position).getType().equalsIgnoreCase("JoinGroup"))
                {
                    if (Build.VERSION.SDK_INT >= 11) {
                        tvMessage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                    float radius = tvMessage.getTextSize() / 3;
                    BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                    tvMessage.getPaint().setMaskFilter(filter);
                    BlurImage.with(mContext).load(R.drawable.profile_banner).intensity(80).Async(true).into(ivProfile);
                }
                else
                {
                    Glide.with(mContext)
                            .load(modelList.get(position).getUserImage())
                            .placeholder(mContext.getDrawable(R.drawable.circle_gray))
                            .into(ivProfile);
                }
                if(modelList.get(position).getType().equalsIgnoreCase("Like"))
                {
                    ivPost.setVisibility(View.VISIBLE);
                    llJoinGroup.setVisibility(View.GONE);
                } else  if(modelList.get(position).getType().equalsIgnoreCase("JoinGroup"))
                {
                    ivPost.setVisibility(View.GONE);
                    llJoinGroup.setVisibility(View.VISIBLE);
                }else
                {
                    ivPost.setVisibility(View.GONE);
                    llJoinGroup.setVisibility(View.GONE);
                }

            }catch (NullPointerException e)
            {

            }catch (Exception e)
            {

            }
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

    public interface OnItemClickListener {

        void onItemClick(View view, int position, HomModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

        }
    }

}

