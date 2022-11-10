package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.my.vibras.R;
import com.my.vibras.adapter.FaqsAdapter;
import com.my.vibras.databinding.ActivityHelpBinding;
import com.my.vibras.model.SuccessResGetHelp;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class HelpAct extends AppCompatActivity {

    ActivityHelpBinding binding;
    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetHelp.Result> faqsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_help);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        binding.RRback.setOnClickListener(v -> finish());

        getFaqs();

    }

    public void getFaqs()
    {
        DataManager.getInstance().showProgressMessage(HelpAct.this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        Call<SuccessResGetHelp> call = apiInterface.getHelp(map);
        call.enqueue(new Callback<SuccessResGetHelp>() {
            @Override
            public void onResponse(Call<SuccessResGetHelp> call, Response<SuccessResGetHelp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResGetHelp data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {

                        faqsList.clear();
                        faqsList.addAll(data.getResult());

                        binding.rvScheduleTime.setHasFixedSize(true);
                        binding.rvScheduleTime.setLayoutManager(new LinearLayoutManager(HelpAct.this));
                        binding.rvScheduleTime.setAdapter(new FaqsAdapter(HelpAct.this,faqsList));

                    } else {
                        showToast(HelpAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetHelp> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


}