package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResGetPosts implements Serializable {


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

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("giftImage")
        @Expose
        public String giftImage;
        @SerializedName("video")
        @Expose
        public String video;
        @SerializedName("file")
        @Expose
        public String file;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("type_status")
        @Expose
        public String typeStatus;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("like_count")
        @Expose
        public String likeCount;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("imageuser")
        @Expose
        public String imageuser;
        @SerializedName("like_status")
        @Expose
        public String likeStatus;
        @SerializedName("quantity")
        @Expose
        public Integer quantity;

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

        public String getGiftImage() {
            return giftImage;
        }

        public void setGiftImage(String giftImage) {
            this.giftImage = giftImage;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeStatus() {
            return typeStatus;
        }

        public void setTypeStatus(String typeStatus) {
            this.typeStatus = typeStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getImageuser() {
            return imageuser;
        }

        public void setImageuser(String imageuser) {
            this.imageuser = imageuser;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

    }

}
