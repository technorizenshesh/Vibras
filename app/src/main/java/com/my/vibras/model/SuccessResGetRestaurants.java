package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,August,2022
 */

public class SuccessResGetRestaurants implements Serializable {

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

    public class RestaurantGallery {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("restaurant_id")
        @Expose
        public String restaurantId;
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
        public String getRestaurantId() {
            return restaurantId;
        }
        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
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

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("restaurant_name")
        @Expose
        public String restaurantName;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("res_count_like")
        @Expose
        public String resCountLike;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("status")
        @Expose
        public String status;
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
        @SerializedName("restaurant_gallery")
        @Expose
        public List<RestaurantGallery> restaurantGallery = null;

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

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getResCountLike() {
            return resCountLike;
        }

        public void setResCountLike(String resCountLike) {
            this.resCountLike = resCountLike;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public List<RestaurantGallery> getRestaurantGallery() {
            return restaurantGallery;
        }

        public void setRestaurantGallery(List<RestaurantGallery> restaurantGallery) {
            this.restaurantGallery = restaurantGallery;
        }

    }

}
