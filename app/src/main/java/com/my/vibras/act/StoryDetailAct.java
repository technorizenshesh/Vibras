package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.bumptech.glide.Glide;
import com.my.vibras.LoginAct;
import com.my.vibras.MainActivity;
import com.my.vibras.R;
import com.my.vibras.act.ui.home.HomeFragment;
import com.my.vibras.model.SuccessResGetStories;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StoryDetailAct extends AppCompatActivity implements MomentzCallback {

    ArrayList<MomentzView> storyView = new ArrayList<>();

    private ArrayList<SuccessResGetStories.UserStory> stories= new ArrayList<>();
    private String userName = "";
    private String userImage = "";
    private Momentz momentz ;
    private ConstraintLayout container ;
    private TextView tvDateTime;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        container = findViewById(R.id.container);
        tvDateTime = findViewById(R.id.tvTimeAgo);
        editText = findViewById(R.id.etComment);
        initializeComponents();
    }

    private void initializeComponents() {

        Bundle extras = getIntent().getExtras();
        SuccessResGetStories.Result storyObject = HomeFragment.story;
        userImage = extras.getString("UserImage");
        userName = extras.getString("UserName");
        stories.addAll(storyObject.getUserStory());

        if(stories!=null)
        {
            for (SuccessResGetStories.UserStory story:stories)
            {
                if(story.getStoryType().equalsIgnoreCase("image") || story.getStoryType().equalsIgnoreCase("Image"))
                {
                    ImageView internetLoadedImageView = new ImageView(this);
                    storyView.add(new MomentzView(internetLoadedImageView,10));
                }
                else
                {
                    VideoView videoView = new VideoView(this);
                    storyView.add(new MomentzView(videoView,60));
                }
            }
        }
        momentz = new Momentz(this,storyView,container,this,R.drawable.green_lightgrey_drawable);
        momentz.start();
    }

    @Override
    public void done() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("TAG", "onPause: ");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "BackPressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {
        Log.d("TAG", "onNextCalled: "+i);
        tvDateTime.setText(stories.get(i).getTimeAgo());

//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    momentz.pause(false);
//                } else {
//                    momentz.resume();
//                }
//            }
//        });

        if(view instanceof VideoView)
        {
            momentz.pause(true);
            playVideo((VideoView) view,i,momentz);
        }
        else
        {
            momentz.pause(true);
            if(!isFinishing()){
                Glide.with(this).load(stories.get(i).getStoryData()).into((ImageView) view);
                momentz.resume();
            }
        }
    }

    public void playVideo(VideoView videoView,int index,Momentz momentz)
    {

        String str = stories.get(index).getStoryData();
        Uri uri = Uri.parse(str);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    momentz.editDurationAndResume(index, (videoView.getDuration()) / 1000);
                    return true;
                }
                return false;
            }
        });
    }

}