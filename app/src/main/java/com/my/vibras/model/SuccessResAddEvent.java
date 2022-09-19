package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,August,2022
 */

public class SuccessResAddEvent implements Serializable {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

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

    public class Result implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("event_cat")
        @Expose
        private String eventCat;
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("date_time_event")
        @Expose
        private String dateTimeEvent;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("like")
        @Expose
        private String like;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("like_count")
        @Expose
        private String likeCount;
        @SerializedName("booking_amount")
        @Expose
        private String bookingAmount;
        @SerializedName("event_start_time")
        @Expose
        private String eventStartTime;
        @SerializedName("event_end_time")
        @Expose
        private String eventEndTime;
        @SerializedName("event_attend")
        @Expose
        private String eventAttend;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEventCat() {
            return eventCat;
        }

        public void setEventCat(String eventCat) {
            this.eventCat = eventCat;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDateTimeEvent() {
            return dateTimeEvent;
        }

        public void setDateTimeEvent(String dateTimeEvent) {
            this.dateTimeEvent = dateTimeEvent;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getBookingAmount() {
            return bookingAmount;
        }

        public void setBookingAmount(String bookingAmount) {
            this.bookingAmount = bookingAmount;
        }

        public String getEventStartTime() {
            return eventStartTime;
        }

        public void setEventStartTime(String eventStartTime) {
            this.eventStartTime = eventStartTime;
        }

        public String getEventEndTime() {
            return eventEndTime;
        }

        public void setEventEndTime(String eventEndTime) {
            this.eventEndTime = eventEndTime;
        }

        public String getEventAttend() {
            return eventAttend;
        }

        public void setEventAttend(String eventAttend) {
            this.eventAttend = eventAttend;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}
