package com.my.vibras.AudioCalling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.vibras.MainActivity;
import com.my.vibras.R;
import com.my.vibras.VideoCalling.VideoCallingAct;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallWaitingActivity extends AppCompatActivity {
    private String channelName="",token="",plumberId="",from="";
    int Profile=1;
    String name="",callType="Video",userimage="";
     ImageView end , pickup;
    CircleImageView circularimage;
    TextView  txtexpname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_waiting);
        name       = getIntent().getStringExtra("name");
        userimage    = getIntent().getStringExtra("userimage");
        channelName    =  getIntent().getStringExtra("channel_name");
        token      =  getIntent().getStringExtra("token");
        callType =  getIntent().getStringExtra("call_type");
        Log.e("TAG", "getIntentgetIntent: "+" name===  " +name+
                "    userimage===  "+userimage+
                "    channelName===  "+ channelName+
                "    token===  "+ token  +
                "    callType===  "+ callType);

        end = findViewById(R.id.end );
        pickup = findViewById(R.id.pickup );
        circularimage = findViewById(R.id.circularimage );
        txtexpname = findViewById(R.id.txtexpname );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);



        txtexpname.setText(name);
        Glide.with(getApplicationContext()).load(userimage).into(circularimage);
         end.setOnClickListener(v -> {
             Intent intentCallReceive = new Intent(getApplicationContext(), MainActivity.class);
             intentCallReceive.putExtra("Call", "incoming");
             intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
             getApplicationContext().startActivity(intentCallReceive);
             finish();
         });
        pickup.setOnClickListener(v -> {
            if (callType.equalsIgnoreCase("Video")){
                //  boolean checkPer = checkAppPermissions();
                Intent intentCallReceive = new Intent(getApplicationContext(), VideoCallingAct.class).putExtra
                        ("id",plumberId)
                        .putExtra("channel_name",channelName)
                        .putExtra("token",token)
                        .putExtra("from","plumber")
                        .putExtra("call_type",callType)
                        .putExtra("name",name)
                        .putExtra("userimage",userimage);
                intentCallReceive.putExtra("Call", "incoming");
                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(intentCallReceive);
                finish();

            }else {

                Intent intentCallReceive = new Intent(getApplicationContext(), VoiceChatViewActivity.class)
                        .putExtra("id",plumberId)
                        .putExtra("channel_name",channelName)
                        .putExtra("token",token)
                        .putExtra("Profile",1)
                        .putExtra("call_type",callType)
                        .putExtra("name",name)
                        .putExtra("userimage",userimage)
                        .putExtra("from","plumber");
                intentCallReceive.putExtra("Call", "incoming");
                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(intentCallReceive);
                finish();

            }
        });

    }
}