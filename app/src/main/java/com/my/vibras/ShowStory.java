package com.my.vibras;

import android.view.View;

import com.my.vibras.model.SuccessResGetStories;

/**
 * Created by Ravindra Birla on 14,July,2021
 */
public interface ShowStory {

    public void showStory(View v,int pos,String userName, String userImage, SuccessResGetStories story);

}
