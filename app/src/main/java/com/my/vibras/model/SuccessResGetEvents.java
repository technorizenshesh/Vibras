package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResGetEvents implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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

    public class EventGallery implements Serializable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("image_file")
        @Expose
        public String imageFile;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getEventId() {
            return eventId;
        }
        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }
        public String getDateTime() {
            return dateTime;
        }
        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
        public String getImageFile() {
            return imageFile;
        }
        public void setImageFile(String imageFile) {
            this.imageFile = imageFile;
        }
    }

    public class Result implements Serializable {

        @SerializedName("event_user_mobile")
        @Expose
        public String event_user_mobile;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("event_cat")
        @Expose
        public String eventCat;
        @SerializedName("event_name")
        @Expose
        public String eventName;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("date_time_event")
        @Expose
        public String dateTimeEvent;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("like")
        @Expose
        public String like;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("like_count")
        @Expose
        public String likeCount;
        @SerializedName("booking_amount")
        @Expose
        public String bookingAmount;
        @SerializedName("event_start_time")
        @Expose
        public String eventStartTime;
        @SerializedName("event_end_time")
        @Expose
        public String eventEndTime;
        @SerializedName("event_attend")
        @Expose
        public String eventAttend;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("distance")
        @Expose
        public String distance;
        @SerializedName("estimate_time")
        @Expose
        public Integer estimateTime;
        @SerializedName("total_comments")
        @Expose
        public Integer totalComments;
        @SerializedName("like_status")
        @Expose
        public String likeStatus;
        @SerializedName("save_post")
        @Expose
        public String savePost;
        @SerializedName("event_gallery")
        @Expose
        public List<EventGallery> eventGallery = null;
        @SerializedName("total_like")
        @Expose
        private Integer totalLike;

        public String getIammember() {
            return iammember;
        }

        public void setIammember(String iammember) {
            this.iammember = iammember;
        }

        @SerializedName("Iammember")
        @Expose
        private String iammember;

        public Integer getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(Integer totalLike) {
            this.totalLike = totalLike;
        }

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

        public String getEvent_user_mobile() {
            return event_user_mobile;
        }

        public void setEvent_user_mobile(String event_user_mobile) {
            this.event_user_mobile = event_user_mobile;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public Integer getEstimateTime() {
            return estimateTime;
        }

        public void setEstimateTime(Integer estimateTime) {
            this.estimateTime = estimateTime;
        }

        public Integer getTotalComments() {
            return totalComments;
        }

        public void setTotalComments(Integer totalComments) {
            this.totalComments = totalComments;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }

        public String getSavePost() {
            return savePost;
        }

        public void setSavePost(String savePost) {
            this.savePost = savePost;
        }

        public List<EventGallery> getEventGallery() {
            return eventGallery;
        }

        public void setEventGallery(List<EventGallery> eventGallery) {
            this.eventGallery = eventGallery;
        }

    }

}
