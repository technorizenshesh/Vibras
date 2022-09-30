package com.my.vibras.utility;

import android.view.View;

/**
 * Created by Ravindra Birla on 01,September,2022
 */
public interface PostClickListener {

    public void selectLike(int position,String status);
    public void bottomSheet(View param1, String postID, boolean isUser, int position);

    public void savePost(View param1, String postID, boolean isUser, int position);

}
