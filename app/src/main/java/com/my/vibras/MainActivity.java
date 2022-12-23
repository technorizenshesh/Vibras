package com.my.vibras;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.act.HomeUserAct;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;

import static com.my.vibras.retrofit.Constant.USER_TYPE;

public class MainActivity extends AppCompatActivity {
    //  BillingClient billingClient;
    Session session;

    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        session = new Session(this);

        isUserLoggedIn = SharedPreferenceUtility.getInstance(MainActivity.this).getBoolean(Constant.IS_USER_LOGGED_IN);

        handlerMethod();
/*
        billingClient = BillingClient.newBuilder(getApplicationContext())
                .setListener((billingResult, list) -> {
                    if (billingResult.getResponseCode() ==
                            BillingClient.BillingResponseCode.OK && list != null) {
                        for (Purchase purchase : list) {
                            //verifyPayment(purchase);
                        }
                    }

                })
                .enablePendingPurchases()
                .build();*/

        // call connectGooglePlayBilling()
        //   connectGooglePlayBilling();
    }

    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isUserLoggedIn) {
                    String type = SharedPreferenceUtility.getInstance(MainActivity.this).getString(USER_TYPE);
                    if (type.equalsIgnoreCase("user")) {
                        startActivity(new Intent(MainActivity.this, HomeUserAct.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, HomeComapnyAct.class));
                        finish();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, SelectViberLoginAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }


 /*   void getProducts() {

        List<String> skuList = new ArrayList<>();

        //replace these with your product IDs from google play console
        skuList.add("remove_ads_id");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {

                            Log.d("remove",skuDetailsList+"");

                            for (SkuDetails skuDetails: skuDetailsList){
                                launchPurchaseFlow(skuDetails);

                                if (skuDetails.getSku().equals("remove_ads_id")){
                                  //  btn_remove_ad.setVisibility(View.VISIBLE);
                                  *//* btn_remove_ad.setOnClickListener(view -> {
                                    });*//*
                                }
                                else {
                                    //btn_remove_ad.setVisibility(View.GONE);
                                }
                            }


                        }

                    }
                });

    }*/

   /* void launchPurchaseFlow(SkuDetails skuDetails) {

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        billingClient.launchBillingFlow(MainActivity.this, billingFlowParams);
    }


    void verifyPayment(Purchase purchase) {


        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        // 1 - True
                        // 0 - False
                       // prefs.setRemoveAd(1);
                        session.set_FormStatus("1");
                    }

                });
            }
        }


    }
*/

/*    void connectGooglePlayBilling() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d("RemovedAd", "Connected " + 0);
                    getProducts();
                }

            }
        });

    }*/
}