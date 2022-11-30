package com.my.vibras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.vibras.R;
import com.my.vibras.databinding.ItemSearchSuggaction2Binding;
import com.my.vibras.databinding.ItemSearchSuggactionBinding;
import com.my.vibras.model.SuccessSearchHistoryRes;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;
import com.my.vibras.utility.SuggactionClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 29,November,2022
 */
public class SearchHistorySuggactionAdapter2 extends RecyclerView.
        Adapter<SearchHistorySuggactionAdapter2.StoriesViewHolder> {
    private Context context;
    ItemSearchSuggaction2Binding binding;
    private List<SuccessSearchHistoryRes.Result> postList;
    private SuggactionClick suggactionClick;
    String From = "";
    private VibrasInterface apiInterface;

    public SearchHistorySuggactionAdapter2(Context context, List<SuccessSearchHistoryRes.Result> postList,
                                          SuggactionClick postClickListener) {
        this.context = context;
        this.postList = postList;
        this.suggactionClick = postClickListener;
    }

    @NonNull
    @Override
    public SearchHistorySuggactionAdapter2.StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSearchSuggaction2Binding.inflate(LayoutInflater.from(context));
        return new SearchHistorySuggactionAdapter2.StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistorySuggactionAdapter2.StoriesViewHolder holder, int position) {
        TextView tvDescription = holder.itemView.findViewById(R.id.item_txt);
        apiInterface  = ApiClient.getClient().create(VibrasInterface.class);
        TextView clear_btn = holder.itemView.findViewById(R.id.clear_btn);
        tvDescription.setText(" "+postList.get(position).getSearch()+" ");
        clear_btn.setOnClickListener(v -> {

            removeItem(postList.get(position).getId(),position);
          //  suggactionClick.suggactionClick(postList.get(position));
        });
    }

    private void removeItem(String id,int g) {
        String userId=  SharedPreferenceUtility.getInstance(context).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("id",id);
        Call<ResponseBody> call = apiInterface.delete_search_history(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                postList.remove(g);
                notifyDataSetChanged();
            }
                @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(ItemSearchSuggaction2Binding itemView) {
            super(itemView.getRoot());
        }
    }
}