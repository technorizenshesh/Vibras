package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class SuccessResGetChat implements Serializable {

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

    public class ReceiverDetail implements Serializable{

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
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("receiver_image")
        @Expose
        public String receiverImage;

        @SerializedName("time_ago")
        @Expose
        public String timeAgo;

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
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

        public String getReceiverImage() {
            return receiverImage;
        }

        public void setReceiverImage(String receiverImage) {
            this.receiverImage = receiverImage;
        }

    }
     public class Result implements Serializable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("sender_id")
        @Expose
        public String senderId;
        @SerializedName("receiver_id")
        @Expose
        public String receiverId;
        @SerializedName("request_id")
        @Expose
        public String requestId;
        @SerializedName("chat_message")
        @Expose
        public String chatMessage;
        @SerializedName("chat_image")
        @Expose
        public String chatImage;
        @SerializedName("chat_audio")
        @Expose
        public String chatAudio;
        @SerializedName("chat_video")
        @Expose
        public String chatVideo;
        @SerializedName("chat_document")
        @Expose
        public String chatDocument;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("contact")
        @Expose
        public String contact;
        @SerializedName("clear_chat")
        @Expose
        public String clearChat;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("pinn_status")
        @Expose
        public String pinnStatus;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("result")
        @Expose
        public String result;
        @SerializedName("sender_detail")
        @Expose
        public SenderDetail senderDetail;
        @SerializedName("receiver_detail")
        @Expose
        public ReceiverDetail receiverDetail;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getChatMessage() {
            return chatMessage;
        }

        public void setChatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
        }

        public String getChatImage() {
            return chatImage;
        }

        public void setChatImage(String chatImage) {
            this.chatImage = chatImage;
        }

        public String getChatAudio() {
            return chatAudio;
        }

        public void setChatAudio(String chatAudio) {
            this.chatAudio = chatAudio;
        }

        public String getChatVideo() {
            return chatVideo;
        }

        public void setChatVideo(String chatVideo) {
            this.chatVideo = chatVideo;
        }

        public String getChatDocument() {
            return chatDocument;
        }

        public void setChatDocument(String chatDocument) {
            this.chatDocument = chatDocument;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getClearChat() {
            return clearChat;
        }

        public void setClearChat(String clearChat) {
            this.clearChat = clearChat;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPinnStatus() {
            return pinnStatus;
        }

        public void setPinnStatus(String pinnStatus) {
            this.pinnStatus = pinnStatus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public SenderDetail getSenderDetail() {
            return senderDetail;
        }

        public void setSenderDetail(SenderDetail senderDetail) {
            this.senderDetail = senderDetail;
        }

        public ReceiverDetail getReceiverDetail() {
            return receiverDetail;
        }

        public void setReceiverDetail(ReceiverDetail receiverDetail) {
            this.receiverDetail = receiverDetail;
        }

    }

    public class SenderDetail implements Serializable {

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
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("sender_image")
        @Expose
        public String senderImage;

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

        public String getSenderImage() {
            return senderImage;
        }

        public void setSenderImage(String senderImage) {
            this.senderImage = senderImage;
        }

    }

}
