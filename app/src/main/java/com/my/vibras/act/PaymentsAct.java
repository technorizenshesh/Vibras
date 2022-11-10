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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;

import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.R;
import com.my.vibras.adapter.AllCpmentAdapter;
import com.my.vibras.adapter.PaymentAdapter;
import com.my.vibras.databinding.ActivityPaymentsBinding;
import com.my.vibras.model.HomModel;
import com.my.vibras.model.SuccessResAddCard;
import com.my.vibras.model.SuccessResAddEvent;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResDeleteCard;
import com.my.vibras.model.SuccessResGetCard;
import com.my.vibras.model.SuccessResGetGroupRestaurantEventAmount;
import com.my.vibras.model.SuccessResGetToken;
import com.my.vibras.model.SuccessResMakePayment;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class PaymentsAct extends AppCompatActivity implements PaymentAdapter.OnItemClickListener  {

    PaymentAdapter mAdapter;

    ActivityPaymentsBinding binding;

    private Dialog dialog;

    private String restrantName="",strLocation="",strDetails="";

    private ArrayList<SuccessResGetCard.Result> cardList = new ArrayList<>();

    String cardNo ="",expirationMonth="",expirationYear="",cvv = "",holderName="";

    private String eventName="",eventDate="",eventTime="",eventCategory="",eventLocation="",etAmount="",eventDetails="",eventType="",str_image_path;

    private String myLatitude="",myLongitude="";

    private ArrayList<String> imagesList = new ArrayList<>();

    private VibrasInterface apiInterface;

    private int selectedCardPosition = -1;

    private String name="",id="",from="";

    private String memberIds="",groupName = "";

    private String planId,strAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_payments);
       apiInterface = ApiClient.getClient().create(VibrasInterface.class);
       binding.RRback.setOnClickListener(v -> {
          onBackPressed();
       });

        from = getIntent().getExtras().getString("from");

        if(from.equalsIgnoreCase("user"))
        {
            planId = getIntent().getExtras().getString("planId");
            strAmount = getIntent().getExtras().getString("planPrice");
        } else   if(from.equalsIgnoreCase("group"))
        {
            memberIds = getIntent().getExtras().getString("memberIds");
            groupName = getIntent().getExtras().getString("groupName");
            planId = "0";
            getGroupPayPrice();
        }else   if(from.equalsIgnoreCase("event"))
        {
            eventName = getIntent().getExtras().getString("eventName");
            str_image_path = getIntent().getExtras().getString("str_image_path");
            eventDate = getIntent().getExtras().getString("eventDate");
            eventTime = getIntent().getExtras().getString("eventTime");
            eventCategory = getIntent().getExtras().getString("eventCategory");
            eventLocation = getIntent().getExtras().getString("eventLocation");
            etAmount = getIntent().getExtras().getString("etAmount");
            eventDetails = getIntent().getExtras().getString("eventDetails");
            eventType = getIntent().getExtras().getString("eventType");
            myLatitude = getIntent().getExtras().getString("lat");
            myLongitude = getIntent().getExtras().getString("lon");
            imagesList = (ArrayList<String>) getIntent().getSerializableExtra("imagesList");
            planId = "0";
            getGroupPayPrice();
        }else   if(from.equalsIgnoreCase("rest"))
        {
            restrantName = getIntent().getExtras().getString("restrantName");
            str_image_path = getIntent().getExtras().getString("str_image_path");
            strLocation = getIntent().getExtras().getString("strLocation");
            strDetails = getIntent().getExtras().getString("strDetails");
            myLatitude = getIntent().getExtras().getString("lat");
            myLongitude = getIntent().getExtras().getString("lon");
            imagesList = (ArrayList<String>) getIntent().getSerializableExtra("imagesList");
            planId = "0";
            getGroupPayPrice();
        }

        binding.RRAddPayment.setOnClickListener(view ->
                {
                    fullScreenDialog();
                }
        );

        binding.RLogin.setOnClickListener(v ->
                {
                    if(selectedCardPosition != -1)
                    {
                        getCvv();
                    }else
                    {
                        Toast.makeText(PaymentsAct.this, "Please select a card.", Toast.LENGTH_SHORT).show();
                    }
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

    private String token="";
    private String cardNum ="",year = "",month = "",cvc = "";

    private void getCvv()
    {

        final Dialog dialog = new Dialog(PaymentsAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_cvv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);
        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        EditText editTextCvv = dialog.findViewById(R.id.etCvv);

        AppCompatButton appCompatButton = dialog.findViewById(R.id.btnLogin);

        appCompatButton.setOnClickListener(v ->
                {
                    if(editTextCvv.getText().toString().equalsIgnoreCase("") || editTextCvv.getText().toString().length()!=3)
                    {
                        Toast.makeText(PaymentsAct.this,"Please Enter a valid cvv",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        cvv = editTextCvv.getText().toString();
                        getToken(v,editTextCvv.getText().toString());
                        dialog.dismiss();
                    }
                }
        );
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void getGroupPayPrice()
    {

        DataManager.getInstance().showProgressMessage(PaymentsAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetGroupRestaurantEventAmount> call = apiInterface.getGroupPurchasePrice(map);
        call.enqueue(new Callback<SuccessResGetGroupRestaurantEventAmount>() {
            @Override
            public void onResponse(Call<SuccessResGetGroupRestaurantEventAmount> call, Response<SuccessResGetGroupRestaurantEventAmount> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetGroupRestaurantEventAmount data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {

                        if(from.equalsIgnoreCase("group"))
                        {
                            strAmount = data.getResult().getGroupAmount();
                            binding.tvPay.setText(data.getResult().getGroupAmount()+" € "+getString(R.string.pay));

                        } else if(from.equalsIgnoreCase("event"))
                        {
                            strAmount = data.getResult().getEventAmount();
                            binding.tvPay.setText(data.getResult().getEventAmount()+" € "+getString(R.string.pay));
                        }else if(from.equalsIgnoreCase("rest"))
                        {
                            strAmount = data.getResult().getRestaurantAmount();
                            binding.tvPay.setText(data.getResult().getRestaurantAmount()+" € "+getString(R.string.pay));
                        }

                    } else {
                        showToast(PaymentsAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetGroupRestaurantEventAmount> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getToken(View v,String myCvv)
    {

        cardNum = cardList.get(selectedCardPosition).getCardNum();
        year = cardList.get(selectedCardPosition).getCardExp();
        month = cardList.get(selectedCardPosition).getCardMonth();
        DataManager.getInstance().showProgressMessage(PaymentsAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardNum);
        map.put("expiry_year",year);
        map.put("expiry_month",month);
        map.put("cvc_code",myCvv);
        Call<SuccessResGetToken> call = apiInterface.getToken(map);
        call.enqueue(new Callback<SuccessResGetToken>() {
            @Override
            public void onResponse(Call<SuccessResGetToken> call, Response<SuccessResGetToken> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetToken data = response.body();
                    if (data.status == 1) {
                        Log.d(TAG, "onResponse: "+token);
                        token = data.getResult().getId();

                        if(from.equalsIgnoreCase("user"))
                        {
                            strAmount = strAmount.replaceAll("€","");
                            makePayment(token,"Plan");
                        } else if(from.equalsIgnoreCase("group"))
                        {
                            makePayment(token,"Group");
                        }else if(from.equalsIgnoreCase("event"))
                        {
                            makePayment(token,"Event");
                        }else if(from.equalsIgnoreCase("rest"))
                        {
                            makePayment(token,"Restaurant");
                        }

                    } else {
                        showToast(PaymentsAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetToken> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void makePayment(String token,String whichToBuy)
    {

        String userId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("plan_id",planId);
        map.put("amount",strAmount);
        map.put("payment_for",whichToBuy);
        map.put("currency","USD");
        map.put("product_id","0");
        map.put("token",token);
        Call<SuccessResMakePayment> call = apiInterface.makePayment(map);
        call.enqueue(new Callback<SuccessResMakePayment>() {
            @Override
            public void onResponse(Call<SuccessResMakePayment> call, Response<SuccessResMakePayment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMakePayment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        if(from.equalsIgnoreCase("user"))
                        {
                            showToast(PaymentsAct.this, "Plan purchased successfully");
                            finish();
                        } else if(from.equalsIgnoreCase("group"))
                        {
                            createGroupApi();
                        } else if(from.equalsIgnoreCase("event"))
                        {
                            addEvent();
                        }else if(from.equalsIgnoreCase("rest"))
                        {
                            addRestaurant();
                        }

                    } else if (data.status.equals("0")) {
                        showToast(PaymentsAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResMakePayment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void createGroupApi()
    {
        String userId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_name",groupName);
        map.put("members_id",memberIds);
        map.put("group_image","");

        Call<ResponseBody> call = apiInterface.createGroup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        showToast(PaymentsAct.this,message);

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        startActivity(new Intent(PaymentsAct.this,HomeUserAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    } else if (data.equals("0")) {
                        showToast(PaymentsAct.this,message);
                    }
                } catch (Exception e) {

                    Log.d("TAG", "onResponse: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void addCardDetails()
    {

        String userId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentsAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_num",cardNo);
        map.put("card_name",holderName);
//      map.put("exp_date",expirationMonth+"/"+expirationYear);
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
                Toast.makeText(PaymentsAct.this, "Card already exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter()
    {

        mAdapter = new PaymentAdapter(PaymentsAct.this,cardList,PaymentsAct.this);
        binding.recycleTrasaction.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentsAct.this);
        binding.recycleTrasaction.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleTrasaction.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new PaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetCard.Result model) {
                selectedCardPosition = position;
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
                        binding.RLogin.setVisibility(View.VISIBLE);
                        cardList.clear();
                        cardList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();

                    } else {
                        binding.RLogin.setVisibility(View.GONE);
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

    @Override
    public void onItemClick(View view, int position, SuccessResGetCard.Result model) {

        selectedCardPosition = position;
    }

    @Override
    public void deleteCard(View view, int position) {

    }


    public void addEvent()
    {

        String strUserId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(PaymentsAct.this,getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesList.size(); i++) {

            String image = imagesList.get(i);

            if(!imagesList.get(i).contains("https://nobu.es/tasknobu/uploads"))
            {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image_file[]", file.getName(), RequestBody.create(MediaType.parse("image_file[]/*"), file)));
            }
        }

        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody eventNm = RequestBody.create(MediaType.parse("text/plain"), eventName);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), eventLocation);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), myLatitude);
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), myLongitude);
        RequestBody eventDat = RequestBody.create(MediaType.parse("text/plain"), eventDate);
        RequestBody startTime = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody endTIme = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), eventDetails);
        RequestBody bookingAmount = RequestBody.create(MediaType.parse("text/plain"), etAmount);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), eventType);
        RequestBody eventCat = RequestBody.create(MediaType.parse("text/plain"), eventCategory);

        Call<SuccessResAddEvent> loginCall = apiInterface.addEvent(userId,eventNm,address,lat,lon,eventDat,startTime,endTIme,description,bookingAmount,type,eventCat,filePart,filePartList);
        loginCall.enqueue(new Callback<SuccessResAddEvent>() {
            @Override
            public void onResponse(Call<SuccessResAddEvent> call, Response<SuccessResAddEvent> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddEvent data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);

                    startActivity(new Intent(PaymentsAct.this, HomeComapnyAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddEvent> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void addRestaurant()
    {

        String strUserId = SharedPreferenceUtility.getInstance(PaymentsAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(PaymentsAct.this,getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesList.size(); i++) {

            String image = imagesList.get(i);

            if(!imagesList.get(i).contains("https://nobu.es/tasknobu/uploads"))
            {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image_file[]", file.getName(), RequestBody.create(MediaType.parse("image_file[]/*"), file)));
            }
        }

        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody eventName = RequestBody.create(MediaType.parse("text/plain"), restrantName);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), strLocation);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), myLatitude);
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), myLongitude);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), strDetails);

        Call<SuccessResAddRestaurant> loginCall = apiInterface.addRestaurants(userId,eventName,address,lat,lon,description,filePart,filePartList);
        loginCall.enqueue(new Callback<SuccessResAddRestaurant>() {
            @Override
            public void onResponse(Call<SuccessResAddRestaurant> call, Response<SuccessResAddRestaurant> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddRestaurant data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);
                    startActivity(new Intent(PaymentsAct.this, HomeComapnyAct.class));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddRestaurant> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}