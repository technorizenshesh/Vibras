package com.my.vibras.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ravindra Birla on 07,December,2022
 */
  public  class SuccessFollowersRes {
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(SuccessFollowersRes.class.getName()).append('@').
                    append(Integer.toHexString(System.identityHashCode(this))).append('[');
            sb.append("result");
            sb.append('=');
            sb.append(((this.result == null)?"<null>":this.result));
            sb.append(',');
            sb.append("message");
            sb.append('=');
            sb.append(((this.message == null)?"<null>":this.message));
            sb.append(',');
            sb.append("status");
            sb.append('=');
            sb.append(((this.status == null)?"<null>":this.status));
            sb.append(',');
            if (sb.charAt((sb.length()- 1)) == ',') {
                sb.setCharAt((sb.length()- 1), ']');
            } else {
                sb.append(']');
            }
            return sb.toString();
        }


    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_receive_id")
        @Expose
        private String userReceiveId;
        @SerializedName("user_given_id")
        @Expose
        private String userGivenId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("zipcode")
        @Expose
        private String zipcode;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("post_filter")
        @Expose
        private String postFilter;
        @SerializedName("should_not")
        @Expose
        private String shouldNot;
        @SerializedName("want_to")
        @Expose
        private String wantTo;
        @SerializedName("age_range_from")
        @Expose
        private String ageRangeFrom;
        @SerializedName("age_range_to")
        @Expose
        private String ageRangeTo;
        @SerializedName("should")
        @Expose
        private String should;
        @SerializedName("p_language")
        @Expose
        private String pLanguage;
        @SerializedName("f_location")
        @Expose
        private String fLocation;
        @SerializedName("f_lat")
        @Expose
        private String fLat;
        @SerializedName("f_lon")
        @Expose
        private String fLon;
        @SerializedName("filter_gender")
        @Expose
        private String filterGender;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("register_id")
        @Expose
        private String registerId;
        @SerializedName("social_id")
        @Expose
        private String socialId;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("time_zone")
        @Expose
        private String timeZone;
        @SerializedName("notification")
        @Expose
        private String notification;
        @SerializedName("push_notification")
        @Expose
        private String pushNotification;
        @SerializedName("email_notification")
        @Expose
        private String emailNotification;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("identify_image")
        @Expose
        private String identifyImage;
        @SerializedName("admin_approval")
        @Expose
        private String adminApproval;
        @SerializedName("social_type")
        @Expose
        private String socialType;
        @SerializedName("first_login")
        @Expose
        private String firstLogin;
        @SerializedName("online_status")
        @Expose
        private String onlineStatus;
        @SerializedName("user_details")
        @Expose
        private UserDetails userDetails;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserReceiveId() {
            return userReceiveId;
        }

        public void setUserReceiveId(String userReceiveId) {
            this.userReceiveId = userReceiveId;
        }

        public String getUserGivenId() {
            return userGivenId;
        }

        public void setUserGivenId(String userGivenId) {
            this.userGivenId = userGivenId;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getFilterGender() {
            return filterGender;
        }

        public void setFilterGender(String filterGender) {
            this.filterGender = filterGender;
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

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
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

        public String getIdentifyImage() {
            return identifyImage;
        }

        public void setIdentifyImage(String identifyImage) {
            this.identifyImage = identifyImage;
        }

        public String getAdminApproval() {
            return adminApproval;
        }

        public void setAdminApproval(String adminApproval) {
            this.adminApproval = adminApproval;
        }

        public String getSocialType() {
            return socialType;
        }

        public void setSocialType(String socialType) {
            this.socialType = socialType;
        }

        public String getFirstLogin() {
            return firstLogin;
        }

        public void setFirstLogin(String firstLogin) {
            this.firstLogin = firstLogin;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Result.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
            sb.append("id");
            sb.append('=');
            sb.append(((this.id == null)?"<null>":this.id));
            sb.append(',');
            sb.append("userReceiveId");
            sb.append('=');
            sb.append(((this.userReceiveId == null)?"<null>":this.userReceiveId));
            sb.append(',');
            sb.append("userGivenId");
            sb.append('=');
            sb.append(((this.userGivenId == null)?"<null>":this.userGivenId));
            sb.append(',');
            sb.append("status");
            sb.append('=');
            sb.append(((this.status == null)?"<null>":this.status));
            sb.append(',');
            sb.append("dateTime");
            sb.append('=');
            sb.append(((this.dateTime == null)?"<null>":this.dateTime));
            sb.append(',');
            sb.append("firstName");
            sb.append('=');
            sb.append(((this.firstName == null)?"<null>":this.firstName));
            sb.append(',');
            sb.append("lastName");
            sb.append('=');
            sb.append(((this.lastName == null)?"<null>":this.lastName));
            sb.append(',');
            sb.append("email");
            sb.append('=');
            sb.append(((this.email == null)?"<null>":this.email));
            sb.append(',');
            sb.append("dob");
            sb.append('=');
            sb.append(((this.dob == null)?"<null>":this.dob));
            sb.append(',');
            sb.append("age");
            sb.append('=');
            sb.append(((this.age == null)?"<null>":this.age));
            sb.append(',');
            sb.append("image");
            sb.append('=');
            sb.append(((this.image == null)?"<null>":this.image));
            sb.append(',');
            sb.append("mobile");
            sb.append('=');
            sb.append(((this.mobile == null)?"<null>":this.mobile));
            sb.append(',');
            sb.append("about");
            sb.append('=');
            sb.append(((this.about == null)?"<null>":this.about));
            sb.append(',');
            sb.append("password");
            sb.append('=');
            sb.append(((this.password == null)?"<null>":this.password));
            sb.append(',');
            sb.append("gender");
            sb.append('=');
            sb.append(((this.gender == null)?"<null>":this.gender));
            sb.append(',');
            sb.append("zipcode");
            sb.append('=');
            sb.append(((this.zipcode == null)?"<null>":this.zipcode));
            sb.append(',');
            sb.append("bio");
            sb.append('=');
            sb.append(((this.bio == null)?"<null>":this.bio));
            sb.append(',');
            sb.append("postFilter");
            sb.append('=');
            sb.append(((this.postFilter == null)?"<null>":this.postFilter));
            sb.append(',');
            sb.append("shouldNot");
            sb.append('=');
            sb.append(((this.shouldNot == null)?"<null>":this.shouldNot));
            sb.append(',');
            sb.append("wantTo");
            sb.append('=');
            sb.append(((this.wantTo == null)?"<null>":this.wantTo));
            sb.append(',');
            sb.append("ageRangeFrom");
            sb.append('=');
            sb.append(((this.ageRangeFrom == null)?"<null>":this.ageRangeFrom));
            sb.append(',');
            sb.append("ageRangeTo");
            sb.append('=');
            sb.append(((this.ageRangeTo == null)?"<null>":this.ageRangeTo));
            sb.append(',');
            sb.append("should");
            sb.append('=');
            sb.append(((this.should == null)?"<null>":this.should));
            sb.append(',');
            sb.append("pLanguage");
            sb.append('=');
            sb.append(((this.pLanguage == null)?"<null>":this.pLanguage));
            sb.append(',');
            sb.append("fLocation");
            sb.append('=');
            sb.append(((this.fLocation == null)?"<null>":this.fLocation));
            sb.append(',');
            sb.append("fLat");
            sb.append('=');
            sb.append(((this.fLat == null)?"<null>":this.fLat));
            sb.append(',');
            sb.append("fLon");
            sb.append('=');
            sb.append(((this.fLon == null)?"<null>":this.fLon));
            sb.append(',');
            sb.append("filterGender");
            sb.append('=');
            sb.append(((this.filterGender == null)?"<null>":this.filterGender));
            sb.append(',');
            sb.append("distance");
            sb.append('=');
            sb.append(((this.distance == null)?"<null>":this.distance));
            sb.append(',');
            sb.append("registerId");
            sb.append('=');
            sb.append(((this.registerId == null)?"<null>":this.registerId));
            sb.append(',');
            sb.append("socialId");
            sb.append('=');
            sb.append(((this.socialId == null)?"<null>":this.socialId));
            sb.append(',');
            sb.append("otp");
            sb.append('=');
            sb.append(((this.otp == null)?"<null>":this.otp));
            sb.append(',');
            sb.append("lat");
            sb.append('=');
            sb.append(((this.lat == null)?"<null>":this.lat));
            sb.append(',');
            sb.append("lon");
            sb.append('=');
            sb.append(((this.lon == null)?"<null>":this.lon));
            sb.append(',');
            sb.append("timeZone");
            sb.append('=');
            sb.append(((this.timeZone == null)?"<null>":this.timeZone));
            sb.append(',');
            sb.append("notification");
            sb.append('=');
            sb.append(((this.notification == null)?"<null>":this.notification));
            sb.append(',');
            sb.append("pushNotification");
            sb.append('=');
            sb.append(((this.pushNotification == null)?"<null>":this.pushNotification));
            sb.append(',');
            sb.append("emailNotification");
            sb.append('=');
            sb.append(((this.emailNotification == null)?"<null>":this.emailNotification));
            sb.append(',');
            sb.append("type");
            sb.append('=');
            sb.append(((this.type == null)?"<null>":this.type));
            sb.append(',');
            sb.append("identifyImage");
            sb.append('=');
            sb.append(((this.identifyImage == null)?"<null>":this.identifyImage));
            sb.append(',');
            sb.append("adminApproval");
            sb.append('=');
            sb.append(((this.adminApproval == null)?"<null>":this.adminApproval));
            sb.append(',');
            sb.append("socialType");
            sb.append('=');
            sb.append(((this.socialType == null)?"<null>":this.socialType));
            sb.append(',');
            sb.append("firstLogin");
            sb.append('=');
            sb.append(((this.firstLogin == null)?"<null>":this.firstLogin));
            sb.append(',');
            sb.append("onlineStatus");
            sb.append('=');
            sb.append(((this.onlineStatus == null)?"<null>":this.onlineStatus));
            sb.append(',');
            sb.append("userDetails");
            sb.append('=');
            sb.append(((this.userDetails == null)?"<null>":this.userDetails));
            sb.append(',');
            if (sb.charAt((sb.length()- 1)) == ',') {
                sb.setCharAt((sb.length()- 1), ']');
            } else {
                sb.append(']');
            }
            return sb.toString();
        }
        public class UserDetails {

            @SerializedName("follow")
            @Expose
            private String follow;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("last_name")
            @Expose
            private String lastName;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("age")
            @Expose
            private String age;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("about")
            @Expose
            private String about;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("gender")
            @Expose
            private String gender;
            @SerializedName("zipcode")
            @Expose
            private String zipcode;
            @SerializedName("bio")
            @Expose
            private String bio;
            @SerializedName("post_filter")
            @Expose
            private String postFilter;
            @SerializedName("should_not")
            @Expose
            private String shouldNot;
            @SerializedName("want_to")
            @Expose
            private String wantTo;
            @SerializedName("age_range_from")
            @Expose
            private String ageRangeFrom;
            @SerializedName("age_range_to")
            @Expose
            private String ageRangeTo;
            @SerializedName("should")
            @Expose
            private String should;
            @SerializedName("p_language")
            @Expose
            private String pLanguage;
            @SerializedName("f_location")
            @Expose
            private String fLocation;
            @SerializedName("f_lat")
            @Expose
            private String fLat;
            @SerializedName("f_lon")
            @Expose
            private String fLon;
            @SerializedName("filter_gender")
            @Expose
            private String filterGender;
            @SerializedName("distance")
            @Expose
            private String distance;
            @SerializedName("register_id")
            @Expose
            private String registerId;
            @SerializedName("social_id")
            @Expose
            private String socialId;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("lat")
            @Expose
            private String lat;
            @SerializedName("lon")
            @Expose
            private String lon;
            @SerializedName("time_zone")
            @Expose
            private String timeZone;
            @SerializedName("notification")
            @Expose
            private String notification;
            @SerializedName("push_notification")
            @Expose
            private String pushNotification;
            @SerializedName("email_notification")
            @Expose
            private String emailNotification;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("identify_image")
            @Expose
            private String identifyImage;
            @SerializedName("admin_approval")
            @Expose
            private String adminApproval;
            @SerializedName("social_type")
            @Expose
            private String socialType;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("first_login")
            @Expose
            private String firstLogin;
            @SerializedName("online_status")
            @Expose
            private String onlineStatus;
            @SerializedName("date_time")
            @Expose
            private String dateTime;

            public String getFollow() {
                return follow;
            }

            public void setFollow(String follow) {
                this.follow = follow;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
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

            public String getFilterGender() {
                return filterGender;
            }

            public void setFilterGender(String filterGender) {
                this.filterGender = filterGender;
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

            public String getTimeZone() {
                return timeZone;
            }

            public void setTimeZone(String timeZone) {
                this.timeZone = timeZone;
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

            public String getIdentifyImage() {
                return identifyImage;
            }

            public void setIdentifyImage(String identifyImage) {
                this.identifyImage = identifyImage;
            }

            public String getAdminApproval() {
                return adminApproval;
            }

            public void setAdminApproval(String adminApproval) {
                this.adminApproval = adminApproval;
            }

            public String getSocialType() {
                return socialType;
            }

            public void setSocialType(String socialType) {
                this.socialType = socialType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getFirstLogin() {
                return firstLogin;
            }

            public void setFirstLogin(String firstLogin) {
                this.firstLogin = firstLogin;
            }

            public String getOnlineStatus() {
                return onlineStatus;
            }

            public void setOnlineStatus(String onlineStatus) {
                this.onlineStatus = onlineStatus;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(UserDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
                sb.append("follow");
                sb.append('=');
                sb.append(((this.follow == null)?"<null>":this.follow));
                sb.append(',');
                sb.append("image");
                sb.append('=');
                sb.append(((this.image == null)?"<null>":this.image));
                sb.append(',');
                sb.append("id");
                sb.append('=');
                sb.append(((this.id == null)?"<null>":this.id));
                sb.append(',');
                sb.append("firstName");
                sb.append('=');
                sb.append(((this.firstName == null)?"<null>":this.firstName));
                sb.append(',');
                sb.append("lastName");
                sb.append('=');
                sb.append(((this.lastName == null)?"<null>":this.lastName));
                sb.append(',');
                sb.append("email");
                sb.append('=');
                sb.append(((this.email == null)?"<null>":this.email));
                sb.append(',');
                sb.append("dob");
                sb.append('=');
                sb.append(((this.dob == null)?"<null>":this.dob));
                sb.append(',');
                sb.append("age");
                sb.append('=');
                sb.append(((this.age == null)?"<null>":this.age));
                sb.append(',');
                sb.append("mobile");
                sb.append('=');
                sb.append(((this.mobile == null)?"<null>":this.mobile));
                sb.append(',');
                sb.append("about");
                sb.append('=');
                sb.append(((this.about == null)?"<null>":this.about));
                sb.append(',');
                sb.append("password");
                sb.append('=');
                sb.append(((this.password == null)?"<null>":this.password));
                sb.append(',');
                sb.append("gender");
                sb.append('=');
                sb.append(((this.gender == null)?"<null>":this.gender));
                sb.append(',');
                sb.append("zipcode");
                sb.append('=');
                sb.append(((this.zipcode == null)?"<null>":this.zipcode));
                sb.append(',');
                sb.append("bio");
                sb.append('=');
                sb.append(((this.bio == null)?"<null>":this.bio));
                sb.append(',');
                sb.append("postFilter");
                sb.append('=');
                sb.append(((this.postFilter == null)?"<null>":this.postFilter));
                sb.append(',');
                sb.append("shouldNot");
                sb.append('=');
                sb.append(((this.shouldNot == null)?"<null>":this.shouldNot));
                sb.append(',');
                sb.append("wantTo");
                sb.append('=');
                sb.append(((this.wantTo == null)?"<null>":this.wantTo));
                sb.append(',');
                sb.append("ageRangeFrom");
                sb.append('=');
                sb.append(((this.ageRangeFrom == null)?"<null>":this.ageRangeFrom));
                sb.append(',');
                sb.append("ageRangeTo");
                sb.append('=');
                sb.append(((this.ageRangeTo == null)?"<null>":this.ageRangeTo));
                sb.append(',');
                sb.append("should");
                sb.append('=');
                sb.append(((this.should == null)?"<null>":this.should));
                sb.append(',');
                sb.append("pLanguage");
                sb.append('=');
                sb.append(((this.pLanguage == null)?"<null>":this.pLanguage));
                sb.append(',');
                sb.append("fLocation");
                sb.append('=');
                sb.append(((this.fLocation == null)?"<null>":this.fLocation));
                sb.append(',');
                sb.append("fLat");
                sb.append('=');
                sb.append(((this.fLat == null)?"<null>":this.fLat));
                sb.append(',');
                sb.append("fLon");
                sb.append('=');
                sb.append(((this.fLon == null)?"<null>":this.fLon));
                sb.append(',');
                sb.append("filterGender");
                sb.append('=');
                sb.append(((this.filterGender == null)?"<null>":this.filterGender));
                sb.append(',');
                sb.append("distance");
                sb.append('=');
                sb.append(((this.distance == null)?"<null>":this.distance));
                sb.append(',');
                sb.append("registerId");
                sb.append('=');
                sb.append(((this.registerId == null)?"<null>":this.registerId));
                sb.append(',');
                sb.append("socialId");
                sb.append('=');
                sb.append(((this.socialId == null)?"<null>":this.socialId));
                sb.append(',');
                sb.append("otp");
                sb.append('=');
                sb.append(((this.otp == null)?"<null>":this.otp));
                sb.append(',');
                sb.append("lat");
                sb.append('=');
                sb.append(((this.lat == null)?"<null>":this.lat));
                sb.append(',');
                sb.append("lon");
                sb.append('=');
                sb.append(((this.lon == null)?"<null>":this.lon));
                sb.append(',');
                sb.append("timeZone");
                sb.append('=');
                sb.append(((this.timeZone == null)?"<null>":this.timeZone));
                sb.append(',');
                sb.append("notification");
                sb.append('=');
                sb.append(((this.notification == null)?"<null>":this.notification));
                sb.append(',');
                sb.append("pushNotification");
                sb.append('=');
                sb.append(((this.pushNotification == null)?"<null>":this.pushNotification));
                sb.append(',');
                sb.append("emailNotification");
                sb.append('=');
                sb.append(((this.emailNotification == null)?"<null>":this.emailNotification));
                sb.append(',');
                sb.append("type");
                sb.append('=');
                sb.append(((this.type == null)?"<null>":this.type));
                sb.append(',');
                sb.append("identifyImage");
                sb.append('=');
                sb.append(((this.identifyImage == null)?"<null>":this.identifyImage));
                sb.append(',');
                sb.append("adminApproval");
                sb.append('=');
                sb.append(((this.adminApproval == null)?"<null>":this.adminApproval));
                sb.append(',');
                sb.append("socialType");
                sb.append('=');
                sb.append(((this.socialType == null)?"<null>":this.socialType));
                sb.append(',');
                sb.append("status");
                sb.append('=');
                sb.append(((this.status == null)?"<null>":this.status));
                sb.append(',');
                sb.append("firstLogin");
                sb.append('=');
                sb.append(((this.firstLogin == null)?"<null>":this.firstLogin));
                sb.append(',');
                sb.append("onlineStatus");
                sb.append('=');
                sb.append(((this.onlineStatus == null)?"<null>":this.onlineStatus));
                sb.append(',');
                sb.append("dateTime");
                sb.append('=');
                sb.append(((this.dateTime == null)?"<null>":this.dateTime));
                sb.append(',');
                if (sb.charAt((sb.length()- 1)) == ',') {
                    sb.setCharAt((sb.length()- 1), ']');
                } else {
                    sb.append(']');
                }
                return sb.toString();
            }

        }

    }


}
