package com.my.vibras.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessAccList
    {

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
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("userimage")
            @Expose
            private String userimage;
            @SerializedName("user_contact")
            @Expose
            private String userContact;
            @SerializedName("distance")
            @Expose
            private String distance;
            @SerializedName("estimate_time")
            @Expose
            private Integer estimateTime;
            @SerializedName("total_comments")
            @Expose
            private Integer totalComments;
            @SerializedName("total_like")
            @Expose
            private Integer totalLike;
            @SerializedName("like_status")
            @Expose
            private String likeStatus;
            @SerializedName("save_post")
            @Expose
            private String savePost;
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

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getUserimage() {
                return userimage;
            }

            public void setUserimage(String userimage) {
                this.userimage = userimage;
            }

            public String getUserContact() {
                return userContact;
            }

            public void setUserContact(String userContact) {
                this.userContact = userContact;
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

            public Integer getTotalLike() {
                return totalLike;
            }

            public void setTotalLike(Integer totalLike) {
                this.totalLike = totalLike;
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
                @SerializedName("image_file")
                @Expose
                private String imageFile;

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

                public String getImageFile() {
                    return imageFile;
                }

                public void setImageFile(String imageFile) {
                    this.imageFile = imageFile;
                }

            }



        }


    }


