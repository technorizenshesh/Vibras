package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.bumptech.glide.Glide;
import com.my.vibras.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StoryDetailAct extends AppCompatActivity implements MomentzCallback {

    private ConstraintLayout container ;

    private TextView tvDateTime;
    ArrayList<MomentzView> storyView = new ArrayList<>();

    private String userName = "";
    private String userImage = "";
    private Momentz momentz ;


    private ArrayList<String> stories= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        container = findViewById(R.id.container);
        tvDateTime = findViewById(R.id.tvTimeAgo);

        stories.add("https://myasp-app.com/vibras//uploads/images/USER_IMG51206.png");
        stories.add("https://myasp-app.com/vibras//uploads/images/USER_IMG70696.png");
        stories.add("https://myasp-app.com/vibras//uploads/images/USER_IMG60834.png");
        stories.add("https://myasp-app.com/vibras//uploads/images/USER_IMG51206.png");

        initializeComponents();
    }

    private void initializeComponents() {
        Bundle extras = getIntent().getExtras();

        if(stories!=null)
        {
            for (String story:stories)
            {
                ImageView internetLoadedImageView = new ImageView(this);
                storyView.add(new MomentzView(internetLoadedImageView,10));
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
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {

        Log.d("TAG", "onNextCalled: "+i);

        tvDateTime.setText("2 min ago");

        if(view instanceof VideoView)
        {
            momentz.pause(true);
            playVideo((VideoView) view,i,momentz);
        }
        else
        {
            momentz.pause(true);
            if(!isFinishing()){
                Glide.with(this).load(stories.get(i)).into((ImageView) view);
                momentz.resume();
            }
        }
    }

    public void playVideo(VideoView videoView,int index,Momentz momentz)
    {

        String str = stories.get(index);
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