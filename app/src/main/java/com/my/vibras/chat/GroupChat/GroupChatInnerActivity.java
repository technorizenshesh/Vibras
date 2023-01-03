package com.my.vibras.chat.GroupChat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.SearchLocationMapAct;
import com.my.vibras.act.ui.GroupDetailAct;
import com.my.vibras.adapter.GroupOne2OneChatAdapter;
import com.my.vibras.chat.ChatMessage;
import com.my.vibras.databinding.ActivityGroupChatBinding;
import com.my.vibras.model.SuccessResGetGroupChat;
import com.my.vibras.model.SuccessResInsertGroupChat;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class GroupChatInnerActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    private DatabaseReference mReference;

    private String id = "", name = "";

    private VibrasInterface apiInterface;

    RecyclerView chat_messages_list;
    GroupChatAdaapter chatListAdapter;
    ImageView live_location;
    GPSTracker gpsTracker;
    private ArrayList<SuccessResGetGroupChat.Result> chatList = new ArrayList<>();
    ArrayList<ChatMessage> allmessagelist = new ArrayList<>();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_chat);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        binding.RRFrnd.setOnClickListener(v -> finish());
        chat_messages_list = findViewById(R.id.rvMessageItem);
        binding.txtName.setText(name);
        session = new Session(this);
        mReference = FirebaseDatabase.getInstance().getReference();
        gpsTracker = new GPSTracker(this);
        live_location = findViewById(R.id.live_location);
        binding.txtName.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GroupDetailAct.class)
                    .putExtra("id", id));
        });
        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        live_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GroupChatInnerActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constant.LOCATION_REQUEST);
                } else {

                    final CharSequence[] options = {"Share Current Location", "Pick on Map", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupChatInnerActivity.this);
                    builder.setTitle("Share Location!");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Share Current Location")) {
                                if (gpsTracker.canGetLocation()) {
                                    Log.e("sendmessage", "onClick: ");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                                    Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                                    String time = dateFormat.format(new Date());
                                    LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLatitude());
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("group_chat").child(id)
                                            .push()
                                            .setValue(new ChatMessage(session.getUserId(), "group",
                                                    "", session.getChatName()
                                                    , "", "", time, "", session.getChatImage(),
                                                    session.getChatImage(), "" + latLng.latitude,
                                                    "" + latLng.longitude));
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.turn_on_gps, Toast.LENGTH_SHORT).show();
                                }


                            } else if (options[item].equals("Pick on Map")) {

                                startActivity(new Intent(GroupChatInnerActivity.this,
                                        SearchLocationMapAct.class)
                                        .putExtra("from", "group")
                                        .putExtra("id", id));

                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        if (id != null) {
            mReference.child("group_chat").child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                allmessagelist.clear();
                                for (DataSnapshot chatt : snapshot.getChildren()) {
                                    String message = chatt.child("message").getValue(String.class);
                                    String senderID = chatt.child("senderID").getValue(String.class);
                                    String receiveerID = chatt.child("receiveerID").getValue(String.class);
                                    String username = chatt.child("username").getValue(String.class);
                                    String image = chatt.child("image").getValue(String.class);
                                    String video = chatt.child("video").getValue(String.class);
                                    String time = chatt.child("time").getValue(String.class);
                                    String status = chatt.child("status").getValue(String.class);
                                    String friendImage = chatt.child("friendImage").getValue(String.class);
                                    String lattitude = chatt.child("lattitude").getValue(String.class);
                                    String longitude = chatt.child("longitude").getValue(String.class);
                                    // FireCons.currentuser=currentuser;
                                    Log.e("useriddeivce", "onDataChange: " + session.getUserId());
                                    Log.e("-->>message", "onDataChange: " + image);
                                    Log.e("-->>message", "onDataChange: " + message);
                                    Log.e("-->>kisko", "onDataChange: " + receiveerID);
                                    Log.e("-->>kisne", "onDataChange: " + senderID);
                                    ChatMessage chatMessage = new ChatMessage(senderID,
                                            receiveerID, message, username, image, video, time,
                                            "", friendImage, session.getChatImage(), lattitude, longitude);
                                    Log.e("insertID", "onDataChange: " + message);

                                    allmessagelist.add(chatMessage);

                                    System.out.println("conditiionif");

                                    chatListAdapter = new GroupChatAdaapter(GroupChatInnerActivity
                                            .this, allmessagelist);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(
                                            GroupChatInnerActivity.this);
                                    chat_messages_list.setLayoutManager(mLayoutManger);
                                    chat_messages_list.setLayoutManager(new LinearLayoutManager(
                                            GroupChatInnerActivity.this, RecyclerView.VERTICAL, false));
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


        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                String time = dateFormat.format(new Date());
                String messagesend = (binding.etText.getText().toString());
                if (!messagesend.equalsIgnoreCase("")) {
                    Log.e("sendmessage", "onClick: ");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("group_chat").child(id)
                            .push()
                            .setValue(new ChatMessage
                                    (session.getUserId(), "group",
                                            messagesend, session.getChatName(),
                                            "", "", time, ""
                                            , session.getChatImage(), session.getChatImage(), "0.0", "0.0"));
                    //  sendmessage(useriddeivce, messagesend, friend_idlast);
                    //   uploadImageVideoPost(messagesend);
                    binding.etText.setText("");

                }
            }
        });
        binding.llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupChatInnerActivity.this);
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

    public static String encodeEmoji(String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
                        .child("group_chat").child(id)
                        .push()
                        .setValue(new ChatMessage(session.getUserId(), "group",
                                "", session.getChatName()
                                , base64String, "", "", "", session.getChatImage(),
                                session.getChatImage(), "0.0", "0.0"));

            } else if (requestCode == 1000) {

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(GroupChatInnerActivity
                            .this.getContentResolver(), data.getData());
                    int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                    Log.e("BITMAAP", "onActivityResult: " + bitmap);

                    String base64String = bitmapToBase64(bitmap);
                    Log.e("base64String", "onActivityResult: " + base64String);

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("group_chat").child(id)
                            .push()
                            .setValue(new ChatMessage(session.getUserId(), "group",
                                    "", session.getChatName()
                                    , base64String, "", "", "", session.getChatImage(),
                                    session.getChatImage(), "0.0", "0.0"));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception exception) {
            Log.e("-->>", "onActivityResult: " + exception.getMessage());
        }

    }

    private boolean isLastVisible() {
        if (chatList != null && chatList.size() != 0) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.rvMessageItem.getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int numItems = binding.rvMessageItem.getAdapter().getItemCount();
            return (pos >= numItems - 1);
        }
        return false;
    }

    private void getChat() {
        String userId = SharedPreferenceUtility.getInstance(GroupChatInnerActivity.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("group_id", id);
        Call<SuccessResGetGroupChat> call = apiInterface.getGroupChat(map);
        call.enqueue(new Callback<SuccessResGetGroupChat>() {
            @Override
            public void onResponse(Call<SuccessResGetGroupChat> call, Response<SuccessResGetGroupChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetGroupChat data = response.body();
                    Log.e("data", data.status + "");
                    if (data.status == 1) {
                        String dataResponse = new Gson().toJson(response.body());
                        chatList.clear();
                        chatList.addAll(data.getResult());
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(GroupChatInnerActivity.this));
                        binding.rvMessageItem.setAdapter(new GroupOne2OneChatAdapter(GroupChatInnerActivity.this, chatList, userId));
                        binding.rvMessageItem.scrollToPosition(chatList.size() - 1);
                    } else if (data.status == 0) {
                        showToast(GroupChatInnerActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetGroupChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void uploadImageVideoPost(String txt) {
        String strUserId = SharedPreferenceUtility.getInstance(GroupChatInnerActivity.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(GroupChatInnerActivity.this, getString(R.string.please_wait));
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), session.getUserId());
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), txt);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "text");
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");
        Call<SuccessResInsertGroupChat> loginCall = apiInterface.insertGroupImageVideoChat
                (senderId, receiverId, messageText, type);
        loginCall.enqueue(new Callback<SuccessResInsertGroupChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertGroupChat> call, Response<SuccessResInsertGroupChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertGroupChat data = response.body();
                    Log.e("data", data.status);
                    //   showToast(GroupChatInnerActivity.this, data.result);
                    //  binding.etText.setText("");
                    // getChat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResInsertGroupChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                //  getChat();
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}