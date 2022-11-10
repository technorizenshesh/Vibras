package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,August,2022
 */

public class SuccessResAddOtherProfileLike implements Serializable {

    @SerializedName("result")
    @Expose
    public String result;
    @SerializedName("user_match")
    @Expose
    public String userMatch;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserMatch() {
        return userMatch;
    }

    public void setUserMatch(String userMatch) {
        this.userMatch = userMatch;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
