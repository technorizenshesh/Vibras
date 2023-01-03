package com.my.vibras.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.FriendProfileActivity;
import com.my.vibras.databinding.SearchItemBinding;
import com.my.vibras.model.SuccessAddSearchHistory;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;
import com.my.vibras.utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.firebase.messaging.Constants.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.StoriesViewHolder> {

    private Context context;

    SearchItemBinding binding;

    private List<SuccessResGetUsers.Result> usersList;
    private VibrasInterface apiInterface;

    private boolean from;
String title;

    public SearchAdapter(Context context, List<SuccessResGetUsers.Result> usersList, boolean from,String title) {
        this.context = context;
        this.usersList = usersList;
        this.from = from;
        this.title = title;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        apiInterface  = ApiClient.getClient().create(VibrasInterface.class);

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName;
        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);


        tvUserName.setText(StringEscapeUtils.unescapeJava(usersList.get(position).getFirstName() + " " + usersList.get(position).getLastName()));

        Glide
                .with(context)
                .load(usersList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        holder.itemView.setOnClickListener(v ->
                {
                    addSearchHistory(title);
                    Util.hideKeyboard((Activity)context );
                    context.startActivity(new Intent(context,
                            FriendProfileActivity.class).putExtra("id",
                            usersList.get(position).getId()));
                }
        );


    }
    private void addSearchHistory(String toString) {
        String userId=  SharedPreferenceUtility.getInstance(context).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("search",toString);
        Call<SuccessAddSearchHistory> call = apiInterface.add_search_history(map);
        call.enqueue(new Callback<SuccessAddSearchHistory>() {
            @Override
            public void onResponse(Call<SuccessAddSearchHistory> call,
                                   Response<SuccessAddSearchHistory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String dataResponse = new Gson().toJson(response.body());

                    Log.e(TAG, "dataResponsedataResponsedataResponse: "+ dataResponse);
                    //  getSearchHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessAddSearchHistory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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
