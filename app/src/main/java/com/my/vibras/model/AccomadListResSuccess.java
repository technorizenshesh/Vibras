package com.my.vibras.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class AccomadListResSuccess  implements Serializable {

@SerializedName("result")
@Expose
private List<Result> result = null;
@SerializedName("message")
@Expose
private String message;
@SerializedName("status")
@Expose
private String status;

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
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("accommodation_id")
        @Expose
        private String accommodationId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("imageuser")
        @Expose
        private String imageuser;
        @SerializedName("time_ago")
        @Expose
        private String timeAgo;

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

        public String getAccommodationId() {
            return accommodationId;
        }

        public void setAccommodationId(String accommodationId) {
            this.accommodationId = accommodationId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
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

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getImageuser() {
            return imageuser;
        }

        public void setImageuser(String imageuser) {
            this.imageuser = imageuser;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }

    }

}