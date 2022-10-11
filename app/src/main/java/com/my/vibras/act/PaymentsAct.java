package com.my.vibras.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.PaymentAdapter;
import com.my.vibras.databinding.ActivityPaymentsBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResAddCard;
import com.my.vibras.model.SuccessResDeleteCard;
import com.my.vibras.model.SuccessResGetCard;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class PaymentsAct extends AppCompatActivity {

    PaymentAdapter mAdapter;
    ActivityPaymentsBinding binding;
    private Dialog dialog;
    private ArrayList<SuccessResGetCard.Result> cardList = new ArrayList<>();
    String cardNo ="",expirationMonth="",expirationYear="",cvv = "",holderName="";

    private VibrasInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

       binding= DataBindingUtil.setContentView(this,R.layout.activity_payments);

       apiInterface = ApiClient.getClient().create(VibrasInterface.class);

       binding.RRback.setOnClickListener(v -> {
          onBackPressed();
       });

       binding.RRAddPayment.setOnClickListener(v -> {
          startActivity(new Intent(new Intent(PaymentsAct.this,AddPaymentCard.class)));
       });

        binding.RRAddPayment.setOnClickListener(view ->
                {
                    fullScreenDialog();
                }
        );
        setAdapter();
        getCards();
    }

    private void fullScreenDialog() {
        dialog = new Dialog(PaymentsAct.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);
        CardForm cardForm = dialog.findViewById(R.id.card_form);

        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity)PaymentsAct.this);

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                //cardForm.getAutofillType();
                Toast.makeText(PaymentsAct.this, ""+ cardForm.getLayerType(), Toast.LENGTH_SHORT).show();

                cardForm.getLabelFor();

                cardNo = cardForm.getCardNumber();
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
                cvv = cardForm.getCvv();
                holderName = cardForm.getCardholderName();

                if(cardForm.isValid())
                {
                    addCardDetails();
                }else
                {
                    cardForm.validate();
                }
            }
        });

        btnAdd.setOnClickListener(v ->
                {

                    cardNo = cardForm.getCardNumber();
                    expirationMonth = cardForm.getExpirationMonth();
                    expirationYear = cardForm.getExpirationYear();
                    cvv = cardForm.getCvv();
                    holderName = cardForm.getCardholderName();
                    if(cardForm.isValid())
                    {

                        addCardDetails();

                    }else
                    {
                        cardForm.validate();
                    }
                }
        );
        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    private void addCardDetails()
    {

        String userId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this,getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_num",cardNo);
        map.put("card_name",holderName);
//        map.put("exp_date",expirationMonth+"/"+expirationYear);
        map.put("card_month",expirationMonth);
        map.put("card_exp",expirationYear);
        Call<SuccessResAddCard> loginCall = apiInterface.addCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCard>() {
            @Override
            public void onResponse(Call<SuccessResAddCard> call, Response<SuccessResAddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddCard data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    showToast(PaymentsAct.this,data.message);
                    dialog.dismiss();
                    getCards();
                    Log.e(TAG,"Test Response :"+responseString);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCard> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void setAdapter()
    {

        mAdapter = new PaymentAdapter(PaymentsAct.this,cardList);
        binding.recycleTrasaction.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentsAct.this);
        binding.recycleTrasaction.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleTrasaction.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new PaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetCard.Result model) {

            }

            @Override
            public void deleteCard(View view, int position) {

                new AlertDialog.Builder(PaymentsAct.this)
                        .setTitle("Delete Card")
                        .setMessage("Are you sure you want to delete this card?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                removeCard(cardList.get(position).getId());
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    public void getCards()
    {

        String userId =  SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCard> call = apiInterface.getCards(map);
        call.enqueue(new Callback<SuccessResGetCard>() {
            @Override
            public void onResponse(Call<SuccessResGetCard> call, Response<SuccessResGetCard> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCard data = response.body();

                    if (data.status.equals("1")) {
                        cardList.clear();
                        cardList.addAll(data.getResult());

                        mAdapter.notifyDataSetChanged();

                    } else {

                        showToast(PaymentsAct.this, data.message);
                        cardList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCard> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    public void removeCard(String cardId)
    {

        String userId =  SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("id",cardId);

        Call<SuccessResDeleteCard> call = apiInterface.deleteCard(map);
        call.enqueue(new Callback<SuccessResDeleteCard>() {
            @Override
            public void onResponse(Call<SuccessResDeleteCard> call, Response<SuccessResDeleteCard> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResDeleteCard data = response.body();

                    if (data.status.equals("1")) {

                        getCards();

                    } else {

                        showToast(PaymentsAct.this, data.message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResDeleteCard> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

}