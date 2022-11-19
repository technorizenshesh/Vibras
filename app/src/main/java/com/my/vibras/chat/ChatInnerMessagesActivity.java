package com.my.vibras.chat;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.VideoCalling.VideoCallingAct;
import com.my.vibras.media.RtcTokenBuilder;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.model.SuccessResMakeCall;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;
import static com.my.vibras.utility.RandomString.getAlphaNumericString;

public class ChatInnerMessagesActivity extends AppCompatActivity {
    static final int OPEN_MEDIA_PICKER = 1;
    private static final int SELECT_VIDEO = 1;
    ImageView sendbutton, camerabutton, uploadmultimedia, backbuttonchat;
    Session session;
    String useriddeivce;
    CircleImageView friednimage;
    TextView friend_name;
    RecyclerView chat_messages_list;
    String friend_idlast;
    ArrayList<ChatMessage> allmessagelist = new ArrayList<>();
    ChatListAdapter chatListAdapter;

    String friendnamelast;
    int IMAGE_REQ_CODE = 102;
    RelativeLayout alltimelayout, firsttimelayout;
    //=------firstimelayoutDATA----------
    CircleImageView profileuserfirsttime;
    TextView usernametextfirstime;
    CardView replybuttonfirst;
    String id;
    ImageView backbuttonfirsttime;
    TextView statustextshow;
    String status = "1";
    int messageunreadcount;
    ImageView menu;
    RelativeLayout backbutton;
    String unique_id;
    private DatabaseReference mReference;
    RelativeLayout call_relat;
    EditText chatmessage_edit;
    String friendimage;
    TextView username;
    private VibrasInterface apiInterface;

    static String appId = "1362d5af232340c39f521565d01ca1e9";
    static String appCertificate = "0333ebf80dac40e2a2a657fa9b7ea5f8";
    static String channelName = "Vibras";
    static String userAccount = "833504";
    static int uid = 0;
    static int expirationTimeInSeconds = 43200;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inner_messages);
        chat_messages_list = findViewById(R.id.chat_messages_list);
        call_relat = findViewById(R.id.call_relat);
        sendbutton = findViewById(R.id.img);
        camerabutton = findViewById(R.id.camerabutton);
        backbutton = findViewById(R.id.backbutton);
        chatmessage_edit = findViewById(R.id.etText);
        username = findViewById(R.id.username);
        session = new Session(ChatInnerMessagesActivity.this);
        useriddeivce = session.getUserId();
        mReference = FirebaseDatabase.getInstance().getReference();
        if (getIntent() != null) {

            friend_idlast = getIntent().getStringExtra("id");
            friendimage = getIntent().getStringExtra("friendimage");
            friendnamelast = getIntent().getStringExtra("friend_name");
            String status_check = getIntent().getStringExtra("status_check");
            unique_id = getIntent().getStringExtra("unique_id");
            Log.e("unique_id", "unique_id: " + unique_id);
            id = getIntent().getStringExtra("id");
            Log.e("->>", "onCreate: friend_idlastfriend_idlastfriend_idlastfriend_idlast  " + friend_idlast + status_check);
            Log.e("->>", "onCreate: friend_idlastfriend_idlastfriend_idlastfriend_idlast    idd " + id + "----" + status_check);
            username.setText(friendnamelast);
        }
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        mReference.child(friend_idlast).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot chatt : snapshot.getChildren()) {
                    Log.e("Status", "onDataChange: " + chatt.getValue(String.class));

                    // statustextshow.setText(chatt.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        channelName = getAlphaNumericString(10);
        token = getToken();
        call_relat.setOnClickListener(v -> {
            Log.e(TAG, "onCreate: channelName ----" + channelName);
            Log.e(TAG, "onCreate: token----------" + token);
            if (NetworkAvailablity.checkNetworkStatus(ChatInnerMessagesActivity.this)) {
                addNotification();
              /*  startActivity(new Intent(ChatInnerMessagesActivity.this, VideoCallingAct.class).putExtra(session.getUserId(),friend_idlast)
                        .putExtra("channel_name",channelName) .putExtra("token",token)
                        .putExtra("from","user"));*/
            } else {
                Toast.makeText(ChatInnerMessagesActivity.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }

        });

        if (unique_id != null) {
            mReference.child("chat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        allmessagelist.clear();
                        for (DataSnapshot chatt : snapshot.getChildren()) {
                            if (status.equalsIgnoreCase("1")) {
                                ChangeUNSEENCOUNTSTATUS();
                            }
                            String message = chatt.child("message").getValue(String.class);
                            String senderID = chatt.child("senderID").getValue(String.class);
                            String receiveerID = chatt.child("receiveerID").getValue(String.class);
                            String username = chatt.child("username").getValue(String.class);
                            String image = chatt.child("image").getValue(String.class);
                            String video = chatt.child("video").getValue(String.class);
                            String time = chatt.child("time").getValue(String.class);
                            String status = chatt.child("status").getValue(String.class);

                            // FireCons.currentuser=currentuser;
                            Log.e("useriddeivce", "onDataChange: " + useriddeivce);


                            Log.e("-->>message", "onDataChange: " + image);
                            Log.e("-->>message", "onDataChange: " + message);
                            Log.e("-->>kisko", "onDataChange: " + receiveerID);
                            Log.e("-->>kisne", "onDataChange: " + senderID);


                            if (useriddeivce.equalsIgnoreCase(senderID) &&
                                    friend_idlast.equalsIgnoreCase(receiveerID)) {
                                ChatMessage chatMessage = new ChatMessage(senderID,
                                        receiveerID, message, username, image, video, time,
                                        "", friendimage, session.getChatImage());
                                Log.e("insertID", "onDataChange: " + message);

                                allmessagelist.add(chatMessage);

                                System.out.println("conditiionif");

                            } else if (useriddeivce.equals(receiveerID) && friend_idlast.equals(senderID)) {
                                ChatMessage chatMessage = new ChatMessage(senderID,
                                        receiveerID, message, username, image, video, time,
                                        "", friendimage, session.getChatImage());
                                allmessagelist.add(chatMessage);
                                System.out.println("conditiionelseif");
                            } else {
                                System.out.println("else<><><><userid");

                            }
                            chatListAdapter = new ChatListAdapter(ChatInnerMessagesActivity.this, allmessagelist);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ChatInnerMessagesActivity.this);
                            chat_messages_list.setLayoutManager(mLayoutManger);
                            chat_messages_list.setLayoutManager(new LinearLayoutManager(ChatInnerMessagesActivity.this, RecyclerView.VERTICAL, false));
                            chat_messages_list.setItemAnimator(new DefaultItemAnimator());
                            chat_messages_list.setAdapter(chatListAdapter);
                            chat_messages_list.setItemViewCacheSize(allmessagelist.size());
                            chatListAdapter.notifyDataSetChanged();
                            chat_messages_list.scrollToPosition(chat_messages_list.getAdapter().getItemCount() - 1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });
        }


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "2";
                onBackPressed();
            }
        });
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("UserID", "onClick: " + useriddeivce);
                Log.e("friendID", "onClick: " + friend_idlast);

                if (!useriddeivce.equalsIgnoreCase(friend_idlast)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                    Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                    String time = dateFormat.format(new Date());
                    String messagesend = chatmessage_edit.getText().toString();
                    if (!messagesend.equalsIgnoreCase("")) {
                        Log.e("sendmessage", "onClick: ");
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("chat")
                                .push()
                                .setValue(new ChatMessage(useriddeivce, friend_idlast, messagesend, friendnamelast, "", "", time, "", friendimage, session.getChatImage()));
                        chatmessage_edit.setText("");
                        sendmessage(useriddeivce, messagesend, friend_idlast);


                        SETUNSEENCOUNTSTATUS();
                    }
                } else {
                    Toast.makeText(ChatInnerMessagesActivity.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatInnerMessagesActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            startActivityForResult(intent, 1);
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 1000);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1) {


                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                Log.e("BITMAAP", "onActivityResult: " + scaled);

                String base64String = bitmapToBase64(scaled);
                Log.e("base64String", "onActivityResult: " + base64String);

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chat")
                        .push()
                        .setValue(new ChatMessage(useriddeivce, friend_idlast, "", session.getChatName()
                                , base64String, "", "", "", friendimage, session.getChatImage()));
                chatmessage_edit.setText("");

            } else if (requestCode == 1000) {

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(ChatInnerMessagesActivity.this.getContentResolver(), data.getData());
                    int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                    Log.e("BITMAAP", "onActivityResult: " + bitmap);

                    String base64String = bitmapToBase64(bitmap);
                    Log.e("base64String", "onActivityResult: " + base64String);

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat")
                            .push()
                            .setValue(new ChatMessage(useriddeivce, friend_idlast, "", friendnamelast, base64String, "", "", "", friendimage, session.getChatImage()));
                    chatmessage_edit.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception exception) {
            Log.e("-->>", "onActivityResult: " + exception.getMessage());
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void SETUNSEENCOUNTSTATUS() {
        mReference.child("From" + useriddeivce + "To" + friend_idlast).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Log.e("UnReadCount->00", "UnReadCount: " + snapshot.getValue());
                Log.e("UnReadCount->01", "UnReadCount: " + snapshot.getClass());
                if (snapshot.getValue() != null) {
                    String[] parts = snapshot.getValue().toString().split("=");

                    String part2 = parts[1];


                    messageunreadcount = Integer.parseInt(part2.replace("}", ""));
                    Log.e("onDataChange-0", "onDataChange: " + messageunreadcount);

                    HashMap<String, Object> HashMap = new HashMap<String, Object>();
                    HashMap.put("UnReadMessageStatus", messageunreadcount + 1);
                    // mReference.child("From" + useriddeivce + "To" + friend_idlast).removeValue();
                    mReference.child("From" + useriddeivce + "To" + friend_idlast).setValue(HashMap);

                } else {
                    HashMap<String, Object> HashMap = new HashMap<String, Object>();
                    HashMap.put("UnReadMessageStatus", 1);
                    // mReference.child("From" + useriddeivce + "To" + friend_idlast).removeValue();
                    mReference.child("From" + useriddeivce + "To" + friend_idlast).setValue(HashMap);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.e("SETUNSEENCOUNTSTATUS", "SETUNSEENCOUNTSTATUS: " + useriddeivce);


        //  mReference.child("From" + useriddeivce + "To" + friend_idlast).update({'dateOfBirth': moment(value.dateOfBirth).toDate().getTime()})
    }

    private void ChangeUNSEENCOUNTSTATUS() {
        Log.e("manageconnection", "manageconnection: " + useriddeivce);
        HashMap<String, Object> HashMap = new HashMap<String, Object>();
        HashMap.put("UnReadMessageStatus", 0);
        mReference.child("From" + friend_idlast + "To" + useriddeivce).setValue(HashMap);
    }

    private void sendmessage(String UserId, String message, String FriednId) {
        //  DataManager.getInstance().showProgressMessage(ChatInnerMessagesActivity.this,getString(R.string.please_wait));
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), UserId);
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), FriednId);
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "Text");
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<SuccessResInsertChat> loginCall = apiInterface.insertImageVideoChat(senderId, receiverId, messageText, type);
        loginCall.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {
                //   DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertChat data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        // showToast(ChatInnerMessagesActivity.this, data.message);

                        // dialog.dismiss();

                    } else if (data.status.equals("0")) {
                        //   showToast(ChatInnerMessagesActivity.this, data.message);
                        //  dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResInsertChat> call, Throwable t) {
                call.cancel();
                // DataManager.getInstance().hideProgressMessage();
                // dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        status = "2";

        super.onBackPressed();
    }

    public String getToken() {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,
                channelName, userAccount, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);

        Log.d("TAG", "onCreate: My Token1" + result);

        result = token.buildTokenWithUid(appId, appCertificate,
                channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);

        Log.d("TAG", "onCreate: My Token2" + result);

        return result;
    }

    public void addNotification() {
        String userId = SharedPreferenceUtility.getInstance(ChatInnerMessagesActivity.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ChatInnerMessagesActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("sender_id", userId);
        map.put("receiver_id", friend_idlast);
        map.put("username", "userName");
        map.put("token", token);
        map.put("channel", channelName);

        //  sender_id=1&receiver_id=3&username=testuser&token=this123ve&channel=testchannel
        Call<SuccessResMakeCall> call = apiInterface.addNotification(map);
        call.enqueue(new Callback<SuccessResMakeCall>() {
            @Override
            public void onResponse(Call<SuccessResMakeCall> call, Response<SuccessResMakeCall> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResMakeCall data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {

                        startActivity(new Intent(ChatInnerMessagesActivity.this, VideoCallingAct.class).putExtra("id"
                                , friend_idlast)
                                .putExtra("channel_name", channelName).putExtra("token", token)
                                .putExtra("from", "user")
                        );
                        finish();

                    } else {
                        showToast(ChatInnerMessagesActivity.this, data.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResMakeCall> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }
}