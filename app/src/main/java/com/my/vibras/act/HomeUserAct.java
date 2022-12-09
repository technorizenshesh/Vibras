package com.my.vibras.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.model.NotificationCount;
import com.my.vibras.model.SocilaLoginResponse;
import com.my.vibras.model.SuccessAddSearchHistory;
import com.my.vibras.model.SuccessFollowersRes;
import com.my.vibras.model.SuccessResAddCard;
import com.my.vibras.model.SuccessResAddEvent;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResDeleteCard;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResFilterData;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.model.SuccessResGetCard;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.model.SuccessResGetChat;
import com.my.vibras.model.SuccessResGetComment;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetEventComment;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetGroup;
import com.my.vibras.model.SuccessResGetGroupChat;
import com.my.vibras.model.SuccessResGetGroupData;
import com.my.vibras.model.SuccessResGetGroupDetails;
import com.my.vibras.model.SuccessResGetGroupRestaurantEventAmount;
import com.my.vibras.model.SuccessResGetHelp;
import com.my.vibras.model.SuccessResGetInterest;
import com.my.vibras.model.SuccessResGetMyStories;
import com.my.vibras.model.SuccessResGetNotification;
import com.my.vibras.model.SuccessResGetPP;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetProfile;
import com.my.vibras.model.SuccessResGetRestaurantComment;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetSubscription;
import com.my.vibras.model.SuccessResGetToken;
import com.my.vibras.model.SuccessResGetTransaction;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.model.SuccessResInsertGroupChat;
import com.my.vibras.model.SuccessResMakeCall;
import com.my.vibras.model.SuccessResMakePayment;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.model.SuccessResMyJoinedEvents;
import com.my.vibras.model.NotificationCount;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.model.SuccessResUpdateRate;
import com.my.vibras.model.SuccessResUploadCoverPhoto;
import com.my.vibras.model.SuccessResUploadPost;
import com.my.vibras.model.SuccessResUploadSelfie;
import com.my.vibras.model.SuccessResUploadStory;
import com.my.vibras.model.SuccessSearchHistoryRes;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class HomeUserAct extends AppCompatActivity {
    static BottomNavigationView navView;
VibrasInterface apiInterface ;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        navView = findViewById(R.id.nav_view);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_notifications,R.id.navigation_chat, R.id.navigation_notifications1)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home_user);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    protected void onResume() {
        getNotification();

        super.onResume();
    }

    public static  void hideBadge() {
 BottomNavigationItemView itemView = navView.findViewById(R.id.navigation_notifications);
    itemView.removeViewAt(2);
    }

    private void getNotification() 

        {
            DataManager.getInstance().showProgressMessage(HomeUserAct.this, getString(R.string.please_wait));
            String userId = SharedPreferenceUtility.getInstance(HomeUserAct.this).getString(USER_ID);
            DataManager.getInstance().showProgressMessage(HomeUserAct.this, getString(R.string.please_wait));
            Map<String,String> map = new HashMap<>();
            map.put("user_id",userId);
            Call<NotificationCount> call = apiInterface.get_total_useen_notification(map);
            call.enqueue(new Callback<NotificationCount>() {
                @Override
                public void onResponse(Call<NotificationCount> call,
                                       Response<NotificationCount> response) {

                    DataManager.getInstance().hideProgressMessage();
                    try {
                        NotificationCount data = response.body();
                        Log.e("data",data.getStatus());
                        if (data.getStatus().equalsIgnoreCase("1")) {
                            String dataResponse = new Gson().toJson(response.body());
                            Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                            //if (data.getResult().getCount()>=1) {
                                showBadge(getApplicationContext(), "" + data.getResult().getCount());
                          // }
                           // hideBadge();

                        } else if (data.getStatus().equalsIgnoreCase("0")) {
                            showToast(HomeUserAct.this, data.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<NotificationCount> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }
            });
        }
    


    public static void showBadge(Context context
                                 , String value) {
       // removeBadge( itemId);
        BottomNavigationItemView itemView = navView.findViewById(R.id.navigation_notifications);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_news_badge,
                navView, false);
    TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

}