package com.my.vibras.act.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.SearchAct;
import com.my.vibras.act.SubsCriptionAct;
import com.my.vibras.adapter.HomeUsersRecyclerViewAdapter;
import com.my.vibras.adapter.StoriesAdapter;
import com.my.vibras.chat.ChatMessage;
import com.my.vibras.databinding.DialogUserPurchaseSubscriptionBinding;
import com.my.vibras.databinding.FragmentHomeBinding;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.CenterZoomLayoutManager;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.HomeItemClickListener;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.REGISTER_ID;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class HomeFragment extends Fragment implements HomeItemClickListener {

    private static final String TAG = "HomeFragmentHomeFragment";
    public static SuccessResGetStories.Result story;
    private FragmentHomeBinding binding;
    private DialogUserPurchaseSubscriptionBinding dialogUserPurchaseSubscriptionBinding;
    private ArrayList<SuccessResGetUsers.Result> usersList = new ArrayList<>();
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();
    private StoriesAdapter mAdapter;
    private HomeUsersRecyclerViewAdapter usersAdapters;
    private VibrasInterface apiInterface;

    private SuccessResGetUsers.Result selectedUser;

    private Dialog dialog;

    private String strLat = "", strLng = "";

    private GPSTracker gpsTracker;
    Session session;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        gpsTracker = new GPSTracker(getActivity());
        session = new Session(getActivity());

        String firebasetoken = SharedPreferenceUtility.getInstance(getActivity()).getString(REGISTER_ID);
        Log.e(TAG, "TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKEN: "+firebasetoken );
        session.setUserId(SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID));
        binding.RRSearch.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchAct.class)); });
        getLocation();
        binding.RREvents.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "home");
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_eventsFragment, bundle);
        });
        setAdapter();
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getInterest();
            getAllUsers();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void setAdapter() {
        mAdapter = new StoriesAdapter(getActivity(), this.storyList);
        binding.rvStories.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.rvStories.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new StoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SuccessResGetStories.Result model)
            {
            }
        });
    }

    private void getInterest() {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        Log.e(TAG, "userIduserIduserIduserId: " + userId);
        map.put("user_id", userId);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<SuccessResGetStories> call = apiInterface.getAllStories(map);
        call.enqueue(new Callback<SuccessResGetStories>() {
            @Override
            public void onResponse(Call<SuccessResGetStories> call, Response<SuccessResGetStories> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetStories data = response.body();
                    Log.e("data", data.status);
                    Log.e(TAG, new Gson().toJson(response.body()));

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e(TAG, "getAllStoriesgetAllStoriesgetAllStories: " + dataResponse);
                        storyList.clear();
                        storyList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
//                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetStories> call, Throwable t) {
                call.cancel();
                Log.e(TAG, t.getLocalizedMessage());
                Log.e(TAG, t.getMessage());

                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocation();
        getProfile();
    }

    private SuccessResSignup.Result userDetail;

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResSignup> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.body() != null) {
                        SuccessResSignup data = response.body();

                        userDetail = data.getResult();
                        Log.e("data", data.status);
                        if (data.status.equals("1")) {
                            String dataResponse = new Gson().toJson(response.body());
                            Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                            Session session = new Session(getActivity());
                            session.setUserId(userId);
                            session.setChatImage(data.getResult().getImage());
                            session.setChatName(data.getResult().getFirstName());
                            binding.txtName.setText("Good Vibes, " + data.getResult().getFirstName());

                        } else if (data.status.equals("0")) {
                            showToast(getActivity(), data.message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getAllUsers() {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<SuccessResGetUsers> call = apiInterface.getAllUsers(map);
        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        usersAdapters = new HomeUsersRecyclerViewAdapter(getActivity(), usersList, HomeFragment.this);
                        CenterZoomLayoutManager layoutManager =
                                new CenterZoomLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvhome.setLayoutManager(layoutManager);
                        binding.rvhome.setAdapter(usersAdapters);
                        binding.rvhome.setHasFixedSize(true);
                        // Scroll to the position we want to snap to
                        // Wait until the RecyclerView is laid out.
                        binding.rvhome.post(() -> {
                            // Shift the view to snap  near the center of the screen.
//                                 This does not have to be precise.
                             try{
                            int dx = (binding.rvhome.getWidth()
                                    - binding.rvhome.getChildAt(0).getWidth()) / 2;
                            binding.rvhome.scrollBy(-dx, 0);
                            // Assign the LinearSnapHelper that will initially snap the near-center view.
                            LinearSnapHelper snapHelper = new LinearSnapHelper();
                            binding.rvhome.setOnFlingListener(null);
                            snapHelper.attachToRecyclerView(binding.rvhome);}catch (Exception e){
                                 getActivity().recreate();
                             }
                        });
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void addUserProfileLike(int position) {
        selectedUser = usersList.get(position);
        addOtherProfileLike(position, usersList.get(position).getId(), "Like");
    }

    @Override
    public void addLikeToUser(int position) {
        selectedUser = usersList.get(position);
        addOtherProfileLike(position, usersList.get(position).getId(), "Love");
    }

    @Override
    public void addChatToUser(int position) {
    }

    @Override
    public void addCommentToUser(int position) {
        selectedUser = usersList.get(position);
        addOtherProfileLike(position, usersList.get(position).getId(), "Fire");
    }

    private void updateLocation() {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lat", strLat);
        map.put("lon", strLng);
        Call<SuccessResDeleteConversation> call = apiInterface.updateLocation(map);
        call.enqueue(new Callback<SuccessResDeleteConversation>() {
            @Override
            public void onResponse(Call<SuccessResDeleteConversation> call, Response<SuccessResDeleteConversation> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteConversation data = response.body();
                    Log.e("data", data.status + "");
                    if (data.status.equalsIgnoreCase("1")) {
                    } else if (data.status.equalsIgnoreCase("0")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResDeleteConversation> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void addOtherProfileLike(int position, String otherUserId, String type) {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id"       , userId);
        map.put("profile_user", otherUserId);
        map.put("type"        , type);
        Call<SuccessResAddOtherProfileLike> call = apiInterface.addFireLikeLove(map);

        call.enqueue(new Callback<SuccessResAddOtherProfileLike>() {
            @Override
            public void onResponse(Call<SuccessResAddOtherProfileLike> call, Response<SuccessResAddOtherProfileLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddOtherProfileLike data = response.body();

                    Log.e("data", data.status + "response-----------------"+new Gson().toJson(response.body()));
                    if (data.status == 1) {

                        showToast(getActivity(), data.result);
                        if (!data.getUserMatch().equalsIgnoreCase("Notmatch")) {
                            fullScreenDialog();
                        }
                        usersList.remove(position);
                        usersAdapters.notifyDataSetChanged();
                    } else if (data.status == 0) {

                         if(data.getResult().equalsIgnoreCase
                                 ("You have not a plan please subscribe our plan")){
                             showToast(getActivity(), data.result);
                             BuySubscriptionDialog();
                         }


                        //fullScreenDialog();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddOtherProfileLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void BuySubscriptionDialog() {
//here
        final Dialog dialog1  = new Dialog(getActivity());
        dialog1.setContentView(R.layout.dialog_user_purchase_subscription);
        dialog1.findViewById(R.id.btnPurchase).setOnClickListener(v ->
                {
                    dialog1.dismiss();
                    Intent intent = new Intent(getActivity(), SubsCriptionAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                  //  Toast.makeText(getActivity(), "Please enter message.", Toast.LENGTH_SHORT).show();
                }
        );
        dialog1.findViewById(R.id.btnCancel).setOnClickListener(v ->
                {
                    dialog1.dismiss();
                }
        );
        dialog1.show();
    }

    public void uploadImageVideoPost(String strChatMessage) {
        String strUserId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), selectedUser.getId());
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "Text");
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<SuccessResInsertChat> loginCall = apiInterface.insertImageVideoChat(senderId, receiverId, messageText, type);
        loginCall.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertChat data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("chat")
                                .child(strUserId)
                                .child(selectedUser.getId())
                                .push()
                                .setValue(new ChatMessage(strUserId, selectedUser.getId(), strChatMessage,
                                        selectedUser.getLastName(), "", "",
                                        "", "", selectedUser.getImage(), session.getChatImage()));
                        dialog.dismiss();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResInsertChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                dialog.dismiss();
            }
        });
    }


    private void fullScreenDialog() {
        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.activity_good_vibes);
        AppCompatButton btnAdd = dialog.findViewById(R.id.btnAdd);
        ImageView ivBack, ivSend, ivProfile;
        ivBack = dialog.findViewById(R.id.ivBack);
        ivSend = dialog.findViewById(R.id.ivSend);
        ivProfile = dialog.findViewById(R.id.userImage);
        TextView tvGoodVibes = dialog.findViewById(R.id.tvGoodVibes);
        EditText editText = dialog.findViewById(R.id.etText);
        tvGoodVibes.setText("!Good Vibes \n With \n" + selectedUser.getFirstName());

        Glide.with(getActivity())
                .load(selectedUser.getImage())
                .into(ivProfile);

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        ivSend.setOnClickListener(v ->
                {
                    if (!editText.getText().toString().equalsIgnoreCase("")) {
                        uploadImageVideoPost(editText.getText().toString());

                        //TODO ----here
                    } else {
                        Toast.makeText(getActivity(), "Please enter message.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
/*   Intent intent = new Intent(context, ChatInnerMessagesActivity.class);
        intent.putExtra("friend_id", alluserchatlist.get(position).getSender_id());
        intent.putExtra("friendimage", holder.username.getText().toString());
        intent.putExtra("friend_name", holder.username.getText().toString());
        intent.putExtra("last_message", holder.lastmessage.getText().toString());
        intent.putExtra("messagetime", "1");
        intent.putExtra("status_check", alluserchatlist.get(position).getName());
        intent.putExtra("id", alluserchatlist.get(position).getId());
        intent.putExtra("onlinestatus", alluserchatlist.get(position).getImage());
        intent.putExtra("unique_id", alluserchatlist.get(position).getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        dialog.show();
    }

    private void addLikePost(String otherUserId) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", "");
        map.put("post_id", "");

        Call<SuccessResAddOtherProfileLike> call = apiInterface.addLikePost(map);

        call.enqueue(new Callback<SuccessResAddOtherProfileLike>() {
            @Override
            public void onResponse(Call<SuccessResAddOtherProfileLike> call, Response<SuccessResAddOtherProfileLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddOtherProfileLike data = response.body();
                    Log.e("data", data.status + "");
                    if (data.status == 1) {

//                        getAllUsers();

                    } else if (data.status == 0) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddOtherProfileLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====", gpsTracker.getLatitude() + "");
            strLat = Double.toString(gpsTracker.getLatitude());
            strLng = Double.toString(gpsTracker.getLongitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude());
                    strLng = Double.toString(gpsTracker.getLongitude());
                    updateLocation();

                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

}