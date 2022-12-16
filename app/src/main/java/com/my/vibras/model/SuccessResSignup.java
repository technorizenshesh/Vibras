package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResSignup implements Serializable {

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
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("about")
        @Expose
        public String about;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("zipcode")
        @Expose
        public String zipcode;
        @SerializedName("bio")
        @Expose
        public String bio;
        @SerializedName("post_filter")
        @Expose
        public String postFilter;
        @SerializedName("should_not")
        @Expose
        public String shouldNot;
        @SerializedName("want_to")
        @Expose
        public String wantTo;
        @SerializedName("age_range_from")
        @Expose
        public String ageRangeFrom;
        @SerializedName("age_range_to")
        @Expose
        public String ageRangeTo;
        @SerializedName("should")
        @Expose
        public String should;
        @SerializedName("p_language")
        @Expose
        public String pLanguage;
        @SerializedName("f_location")
        @Expose
        public String fLocation;
        @SerializedName("f_lat")
        @Expose

        public String fLat;
        @SerializedName("f_lon")
        @Expose
        public String fLon;
        @SerializedName("distance")
        @Expose
        public String distance;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("notification")
        @Expose
        public String notification;
        @SerializedName("push_notification")
        @Expose
        public String pushNotification;
        @SerializedName("email_notification")
        @Expose
        public String emailNotification;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("cover_image")
        @Expose
        public String coverImage;
        @SerializedName("recevied_likes")
        @Expose
        public Integer receviedLikes;
        @SerializedName("subscription")
        @Expose
        public String subscription;
        @SerializedName("first_login")
        @Expose
        public String first_login;
        @SerializedName("admin_approval")
        @Expose
        public String admin_approval;

        public String getAdmin_approval() {
            return admin_approval;
        }

        public void setAdmin_approval(String admin_approval) {
            this.admin_approval = admin_approval;
        }

        public String getFirst_login() {
            return first_login;
        }

        public void setFirst_login(String first_login) {
            this.first_login = first_login;
        }

        public String getRestaurantsPlan() {
            return restaurantsPlan;
        }

        public void setRestaurantsPlan(String restaurantsPlan) {
            this.restaurantsPlan = restaurantsPlan;
        }

        @SerializedName("given_likes")
        @Expose
        public Integer givenLikes;
        @SerializedName("restaurants_plan")
        @Expose
        private String restaurantsPlan;
        @SerializedName("like_status")
        @Expose
        private String like_status;
        @SerializedName("match_status")
        @Expose
        private String match_status;

        public String getMatch_status() {
            return match_status;
        }

        public void setMatch_status(String match_status) {
            this.match_status = match_status;
        }

        public String getLike_status() {
            return like_status;
        }

        public void setLike_status(String like_status) {
            this.like_status = like_status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getPostFilter() {
            return postFilter;
        }

        public void setPostFilter(String postFilter) {
            this.postFilter = postFilter;
        }

        public String getShouldNot() {
            return shouldNot;
        }

        public void setShouldNot(String shouldNot) {
            this.shouldNot = shouldNot;
        }

        public String getWantTo() {
            return wantTo;
        }

        public void setWantTo(String wantTo) {
            this.wantTo = wantTo;
        }

        public String getAgeRangeFrom() {
            return ageRangeFrom;
        }

        public void setAgeRangeFrom(String ageRangeFrom) {
            this.ageRangeFrom = ageRangeFrom;
        }

        public String getAgeRangeTo() {
            return ageRangeTo;
        }

        public void setAgeRangeTo(String ageRangeTo) {
            this.ageRangeTo = ageRangeTo;
        }

        public String getShould() {
            return should;
        }

        public void setShould(String should) {
            this.should = should;
        }

        public String getpLanguage() {
            return pLanguage;
        }

        public void setpLanguage(String pLanguage) {
            this.pLanguage = pLanguage;
        }

        public String getfLocation() {
            return fLocation;
        }

        public void setfLocation(String fLocation) {
            this.fLocation = fLocation;
        }

        public String getfLat() {
            return fLat;
        }

        public void setfLat(String fLat) {
            this.fLat = fLat;
        }

        public String getfLon() {
            return fLon;
        }

        public void setfLon(String fLon) {
            this.fLon = fLon;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
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

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getPushNotification() {
            return pushNotification;
        }

        public void setPushNotification(String pushNotification) {
            this.pushNotification = pushNotification;
        }

        public String getEmailNotification() {
            return emailNotification;
        }

        public void setEmailNotification(String emailNotification) {
            this.emailNotification = emailNotification;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public Integer getReceviedLikes() {
            return receviedLikes;
        }

        public void setReceviedLikes(Integer receviedLikes) {
            this.receviedLikes = receviedLikes;
        }

        public String getSubscription() {
            return subscription;
        }

        public void setSubscription(String subscription) {
            this.subscription = subscription;
        }

        public Integer getGivenLikes() {
            return givenLikes;
        }

        public void setGivenLikes(Integer givenLikes) {
            this.givenLikes = givenLikes;
        }

    }
}
