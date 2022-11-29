package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ravindra Birla on 24,November,2022
 */

public class SocilaLoginResponse
{
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


 public class Result {

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
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("online_status")
  @Expose
  private String onlineStatus;
  @SerializedName("date_time")
  @Expose
  private String dateTime;

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

  public String getStatus() {
   return status;
  }

  public void setStatus(String status) {
   this.status = status;
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

 }
}
