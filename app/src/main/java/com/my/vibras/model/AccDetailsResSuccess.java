package com.my.vibras.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AccDetailsResSuccess {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("accommodation_name")
        @Expose
        private String accommodationName;
        @SerializedName("accommodation_category")
        @Expose
        private String accommodationCategory;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("accommodation_gallery")
        @Expose
        private List<AccommodationGallery> accommodationGallery = null;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAccommodationName() {
            return accommodationName;
        }

        public void setAccommodationName(String accommodationName) {
            this.accommodationName = accommodationName;
        }

        public String getAccommodationCategory() {
            return accommodationCategory;
        }

        public void setAccommodationCategory(String accommodationCategory) {
            this.accommodationCategory = accommodationCategory;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<AccommodationGallery> getAccommodationGallery() {
            return accommodationGallery;
        }

        public void setAccommodationGallery(List<AccommodationGallery> accommodationGallery) {
            this.accommodationGallery = accommodationGallery;
        }

        public class AccommodationGallery {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("accommodation_id")
            @Expose
            private String accommodationId;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("date_time")
            @Expose
            private String dateTime;
            @SerializedName("restaurant_Gallery")
            @Expose
            private String restaurantGallery;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAccommodationId() {
                return accommodationId;
            }

            public void setAccommodationId(String accommodationId) {
                this.accommodationId = accommodationId;
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

            public String getRestaurantGallery() {
                return restaurantGallery;
            }

            public void setRestaurantGallery(String restaurantGallery) {
                this.restaurantGallery = restaurantGallery;
            }

        }

    }
}