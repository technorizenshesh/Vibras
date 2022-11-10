package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResGetGroupRestaurantEventAmount implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("group_amount")
        @Expose
        public String groupAmount;
        @SerializedName("event_amount")
        @Expose
        public String eventAmount;
        @SerializedName("restaurant_amount")
        @Expose
        public String restaurantAmount;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupAmount() {
            return groupAmount;
        }

        public void setGroupAmount(String groupAmount) {
            this.groupAmount = groupAmount;
        }

        public String getEventAmount() {
            return eventAmount;
        }

        public void setEventAmount(String eventAmount) {
            this.eventAmount = eventAmount;
        }

        public String getRestaurantAmount() {
            return restaurantAmount;
        }

        public void setRestaurantAmount(String restaurantAmount) {
            this.restaurantAmount = restaurantAmount;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }
    
}