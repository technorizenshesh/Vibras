package com.my.vibras.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.vibras.R;
import com.my.vibras.utility.Session;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class AllChatuserAdapter extends RecyclerView.Adapter<AllChatuserAdapter.ViewHolder>
        implements Filterable {
    ArrayList<AllChatUserModel> filtertranersModels;
    List<AllChatUserModel> alluserchatlist;
    Context context;
    View viewlike;
    ;
    Intent intent = new Intent();
    Session session;
    String user_id, statusget;
    private DatabaseReference mReference;
    OnItemDeleteClickListener onItemClick;

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<AllChatUserModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filtertranersModels);
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AllChatUserModel item : filtertranersModels) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    } else {

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtertranersModels.clear();
            filtertranersModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    public AllChatuserAdapter(ArrayList<AllChatUserModel> alluserchatlist, Context context,OnItemDeleteClickListener onItemClick) {
        this.alluserchatlist = alluserchatlist;
        this.filtertranersModels = alluserchatlist;
        this.onItemClick = onItemClick;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatuser_layout, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        session = new Session(context);
        user_id = session.getUserId();
        final AllChatUserModel model = alluserchatlist.get(position);
        mReference = FirebaseDatabase.getInstance().getReference();
        holder.username.setText(model.getName());
        holder.lastmessage.setText(model.getMessage());
        Glide.with(context)
                .load(model.getImage())
                .into(holder.user_Image);

        Log.e("==>>", "onBindViewHolder: " + model.getReceiver_id());
        Log.e("==>>", "onBindViewHolder: " + model.getSender_id());
        Log.e("==>>", "onBindViewHolder: " + session.getUserId());
        mReference.child(alluserchatlist.get(position).getSender_id()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Log.e("onDataChange->00", "onDataChange: " + snapshot.getValue());
                    Log.e("onDataChange->01", "onDataChange: " + snapshot.getClass());

                    String s = "Android_a_b.pdf";
                    String[] parts = snapshot.getValue().toString().split("=");

                    String part2 = parts[1];
                    Log.e("part2", "onDataChange: " + part2);


                    statusget = part2.replace("}", "");
                    Log.e("statusget", "onDataChange: " + statusget);
                    // updateImage.statuschange(statusget);


                    if (statusget.equalsIgnoreCase("Offline")) {
                        holder.status_image.setVisibility(View.VISIBLE);
                        holder.status_image.setImageResource(R.drawable.red_circel);
                        //  holder.statusshowimage.setImageResource(R.drawable.red_dot);
                    } else if (statusget.equalsIgnoreCase("Online")) {
                        holder.status_image.setVisibility(View.VISIBLE);

                        holder.status_image.setImageResource(R.drawable.online_circle);
                        // holder.statusshowimage.setImageResource(R.drawable.gren_dot);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (session.getUserId().equalsIgnoreCase(alluserchatlist.get(position).getSender_id())) {
            mReference.child("From" + alluserchatlist.get(position).getReceiver_id() + "To"
                    + user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    Log.e("UnReadCount->00", "UnReadCount: " + snapshot.getValue());
                    Log.e("UnReadCount->01", "UnReadCount: " + snapshot.getClass());
                    if (snapshot.getValue() != null) {
                        String[] parts = snapshot.getValue().toString().split("=");

                        String part2 = parts[1];


                        String unreadstatus = part2.replace("}", "");
                        Log.e("statusget", "UnReadCount: " + unreadstatus);

                        if (unreadstatus.equalsIgnoreCase("0")) {
                            holder.unseenmessagecount.setVisibility(View.GONE);
                            //   holder.unssencard.setVisibility(View.GONE);
                            //holder.  unseenmessagecount.setText(unreadstatus);
                        } else {
                            holder.unseenmessagecount.setVisibility(View.VISIBLE);
                            holder.unseenmessagecount.setText(unreadstatus);
                            // notifyItemChanged(position);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            mReference.child("From" + alluserchatlist.get(position).getSender_id() + "To"
                    + user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("UnReadCount->00", "UnReadCount: " + snapshot.getValue());
                    Log.e("UnReadCount->01", "UnReadCount: " + snapshot.getClass());
                    if (snapshot.getValue() != null) {
                        String[] parts = snapshot.getValue().toString().split("=");
                        String part2 = parts[1];
                        String unreadstatus = part2.replace("}", "");
                        Log.e("statusget", "UnReadCount: " + unreadstatus);

                        if (unreadstatus.equalsIgnoreCase("0")) {
                            holder.unseenmessagecount.setVisibility(View.GONE);
                            //   holder.unssencard.setVisibility(View.GONE);
                            //holder.  unseenmessagecount.setText(unreadstatus);
                        } else {
                            holder.unseenmessagecount.setVisibility(View.VISIBLE);
                            holder.unseenmessagecount.setText(unreadstatus);
                            // notifyItemChanged(position);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        Log.e("statusgot-->.", "onBindViewHolder: " + alluserchatlist.get(position).getId());



/*
        Log.e("ID_---------->", "onBindViewHolder: " + model.getFriend_id());
        Log.e("ID_---------->", "onBindViewHolder: " + model.getFriendname());
        Log.e("ID_---------->", "onBindViewHolder: " + model.getFriend_image());
        Log.e("ID_---------->", "onBindViewHolder: " + model.getLastmessage());
*/


      /*  Glide.with(context)
                .load(  + model.getFriend_image())
                .into(holder.user_Image);*/
  holder .ivDelete.setOnClickListener( v -> {
      onItemClick.onItemDeleteClick(holder.itemView,position, model);

  });
        holder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > getItemCount()) {
                } else {
                    Intent intent = new Intent(context, ChatInnerMessagesActivity.class);
                /*    if (session.getUserId().equalsIgnoreCase(alluserchatlist.get(position).getSender_id())) {
                        intent.putExtra("friend_id", alluserchatlist.get(position).getSender_id());
                    } else {*/
                        intent.putExtra("friend_id", alluserchatlist.get(position).getSender_id());
                  //  }
                    intent.putExtra("friendimage", holder.username.getText().toString());
                    intent.putExtra("friend_name", holder.username.getText().toString());
                    intent.putExtra("last_message", holder.lastmessage.getText().toString());
                    intent.putExtra("messagetime", "1");
                    intent.putExtra("status_check", alluserchatlist.get(position).getName());
                    intent.putExtra("id", alluserchatlist.get(position).getId());
                    intent.putExtra("onlinestatus", alluserchatlist.get(position).getImage());
                    intent.putExtra("unique_id", alluserchatlist.get(position).getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return alluserchatlist.size();
    }

    @Override
    public Filter getFilter() {
        return Searched_Filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_Image;
        ImageView    ivDelete , status_image;
        TextView username, lastmessage, unseenmessagecount;
        RelativeLayout rlParent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_Image = itemView.findViewById(R.id.user_image_show);
            status_image = itemView.findViewById(R.id.status_image);
            username = itemView.findViewById(R.id.tvUserName);
            lastmessage = itemView.findViewById(R.id.lastmeeage);
            rlParent = itemView.findViewById(R.id.rlParent);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            unseenmessagecount = itemView.findViewById(R.id.unseenmessagecount);
        }
    }

    public interface OnItemDeleteClickListener {

        void onItemDeleteClick(View view, int position, AllChatUserModel model);

    }
}