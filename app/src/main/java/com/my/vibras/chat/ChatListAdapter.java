package com.my.vibras.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.my.vibras.R;
import com.my.vibras.utility.Session;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    Context mContext;
    List<ChatMessage> chatList;
    Resources mRes;
    LayoutInflater mInflater;
    String userid;
    Session session;


    public ChatListAdapter(Context context, List<ChatMessage> chatList) {
        this.chatList = chatList;
        this.mContext = context;
        //notifyDataSetChanged();

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allmessagesholayout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ChatMessage chat = chatList.get(position);


        session = new Session(mContext);
        userid=session.getUserId();
        Log.e("adapterID", "onBindViewHolder: "+userid );
        Log.e("adapterID", "onBindViewHolder: "+chat.getReceiveerID() );
        Log.e("adapterID", "onBindViewHolder: "+chat.getSenderID() );
        Log.e("adapterID", "onBindViewHolder: "+chat.getImage() );
        Log.e("adapterID", "onBindViewHolder: "+chat.getVideo() );
        Log.e("adapterID", "onBindViewHolder: "+chat.getTime() );

        if(chat.getSenderID().equalsIgnoreCase(userid)) {


            if (!chat.getLattitude().equalsIgnoreCase("0.0")){
                holder.mycard_locat.setVisibility(View.VISIBLE);
                holder.friendcard_locat.setVisibility(View.GONE);
                holder.usermessagelayout.setVisibility(View.GONE);
                holder.friendcard.setVisibility(View.GONE);
                holder.recievesendimage.setVisibility(View.GONE);
                holder.userTextSHow.setVisibility(View.GONE);
                holder.friendmessagelayout.setVisibility(View.GONE);

            }else
            if (chat.getMessage().equalsIgnoreCase("")){
                holder.usercard.setVisibility(View.VISIBLE);
                holder.friendcard.setVisibility(View.GONE);
                holder.  usersendimage.setVisibility(View.VISIBLE);
                holder.recievesendimage.setVisibility(View.GONE);
                holder.usersendimage.setImageBitmap(base64ToBitmap(chat.getImage()));
                holder.userTextSHow.setVisibility(View.GONE);
                holder.friendmessagelayout.setVisibility(View.GONE);
                holder.usermessagelayout.setVisibility(View.GONE);
            }else {
                holder.usercard.setVisibility(View.GONE);
                holder.friendcard.setVisibility(View.GONE);
                holder.  usersendimage.setVisibility(View.GONE);
                holder.recievesendimage.setVisibility(View.GONE);
                holder.userTextSHow.setText(chat.getMessage());
                holder.friendmessagelayout.setVisibility(View.GONE);
                System.out.println("userid" + chat.getSenderID());
                Glide.with(mContext)
                        .load(  chat.getUserImage())
                        .into(holder.userimage);
                holder. friendtime.setVisibility(View.GONE);
                holder. usertime.setVisibility(View.VISIBLE);
                holder. usertime.setText(chat.getTime());
            }

        }
        else {

            if (!chat.getLattitude().equalsIgnoreCase("0.0")){
                holder.friendcard_locat.setVisibility(View.VISIBLE);
                holder.mycard_locat.setVisibility(View.GONE);
                holder.  usersendimage.setVisibility(View.GONE);
                holder.userTextSHow.setVisibility(View.GONE);
                holder.friendmessagelayout.setVisibility(View.GONE);
                holder.usermessagelayout.setVisibility(View.GONE);
                holder.usercard.setVisibility(View.GONE);
                holder.friendcard.setVisibility(View.GONE);
                holder.  usersendimage.setVisibility(View.GONE);
                holder.recievesendimage.setVisibility(View.GONE);
            }else
            if (chat.getMessage().equalsIgnoreCase("")){
                holder.  recievesendimage.setVisibility(View.VISIBLE);
                holder.usercard.setVisibility(View.GONE);
                holder.friendcard.setVisibility(View.VISIBLE);
                holder.recievesendimage.setImageBitmap(base64ToBitmap(chat.getImage()));
                holder.  usersendimage.setVisibility(View.GONE);
                holder.userTextSHow.setVisibility(View.GONE);
                holder.friendTextShow.setVisibility(View.GONE);
                holder.friendmessagelayout.setVisibility(View.GONE);
                holder.usermessagelayout.setVisibility(View.GONE);
               /* if (chat.getImage().equalsIgnoreCase("")){
                    holder.friendcard_locat.setVisibility(View.VISIBLE);
                }*/



            }else {
                holder.usercard.setVisibility(View.GONE);
                holder.friendcard.setVisibility(View.GONE);
                holder.  usersendimage.setVisibility(View.GONE);
                holder.recievesendimage.setVisibility(View.GONE);
                holder.friendTextShow.setText(chat.getMessage());
                holder.usermessagelayout.setVisibility(View.GONE);
                holder. friendtime.setVisibility(View.VISIBLE);
                holder. usertime.setVisibility(View.GONE);
                holder. friendtime.setText(chat.getTime());
                Log.e("TAG", "GlideGlideGlideGlideGlide: " + chat.getFriendImage());
                Glide.with(mContext)
                        .load(   chat.getFriendImage())
                        .into(holder.friendsenderimage);
                System.out.println("elseuserid" + chat.getReceiveerID());

            }
            // ChatListAdapter222.notifyDataSetChanged();
            try {
                ChatListAdapter.this.notify();

            } catch (Exception e) {
                System.out.println("====" + e.fillInStackTrace());

            }

        }
         holder.friendcard_locat.setOnClickListener(v -> {
             Intent intent = new Intent(Intent.ACTION_VIEW,
                     Uri.parse("geo:"+ chat.getLattitude()+","+ chat.getLongitude()+"?q="+ chat.getLattitude()+","+ chat.getLongitude()));
             mContext. startActivity(intent);
           /*  Log.e("TAG", "onBindViewHolder: "+  chat.getLattitude());
             Log.e("TAG", "onBindViewHolder: "+  chat.getLongitude());
             Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:" +chat.getLattitude()+","+chat.getLongitude()));
             i.setClassName("com.google.android.apps.maps",
                     "com.google.android.maps.MapsActivity");
             mContext.  startActivity(i);*/
         });

        holder.mycard_locat.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:"+ chat.getLattitude()+","+ chat.getLongitude()+"?q="+ chat.getLattitude()+","+ chat.getLongitude()));
           mContext. startActivity(intent);
         });

        holder.usercard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Holo_NoActionBar);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.blue));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.customview);
                dialog.show();
                ImageView  imagewshow = (ImageView) dialog .findViewById(R.id.imagewshow);
                RelativeLayout closeimage = (RelativeLayout) dialog .findViewById(R.id.closeimage);
                closeimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                imagewshow.setImageBitmap(base64ToBitmap(chat.getImage()));
            }
        });
        holder.friendcard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Holo_NoActionBar);
                // dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.blue));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.customview);
           //     dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFF;
                dialog.show();


                ImageView  imagewshow = (ImageView) dialog .findViewById(R.id.imagewshow);
                RelativeLayout closeimage = (RelativeLayout) dialog .findViewById(R.id.closeimage);
                closeimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                imagewshow.setImageBitmap(base64ToBitmap(chat.getImage()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return  chatList.size();
    }

    public void newDialog(final Context c){

        final AlertDialog alertDialog;


    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        }
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userTextSHow,friendTextShow,usertime,friendtime;
        CardView usercard,friendcard,friendcard_locat,mycard_locat;

        LinearLayout usermessagelayout,friendmessagelayout;
        ImageView usersendimage,recievesendimage;
        CircleImageView friendsenderimage,userimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendcard_locat =itemView.findViewById(R.id.friendcard_locat);
            mycard_locat =itemView.findViewById(R.id.mycard_locat);
            usercard =itemView.findViewById(R.id.usercard);
            friendcard =itemView.findViewById(R.id.friendcard);
            userTextSHow =itemView.findViewById(R.id.userTextSHow);
            friendTextShow = itemView.findViewById(R.id.friendTextShow);
            usermessagelayout = itemView.findViewById(R.id.usermessagelayout);
            friendmessagelayout = itemView.findViewById(R.id.friendmessagelayout);
            usersendimage = itemView.findViewById(R.id.usersendimage);
            recievesendimage = itemView.findViewById(R.id.recievesendimage);
            usertime = itemView.findViewById(R.id.usertime);
            friendtime = itemView.findViewById(R.id.friendtime);
            friendsenderimage = itemView.findViewById(R.id.friendsenderimage);
            userimage = itemView.findViewById(R.id.userimage);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


