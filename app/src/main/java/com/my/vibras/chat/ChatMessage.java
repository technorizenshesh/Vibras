package com.my.vibras.chat;

public class ChatMessage {
    String senderID;
    String receiveerID;
    String message;
    String username;
    String image;
    String video;
    String time;
    String status;
    String FriendImage;
    String UserImage;

    public ChatMessage(String senderID, String receiveerID, String message, String username, String image, String video, String time, String status, String friendImage, String userImage) {
        this.senderID = senderID;
        this.receiveerID = receiveerID;
        this.message = message;
        this.username = username;
        this.image = image;
        this.video = video;
        this.time = time;
        this.status = status;
        FriendImage = friendImage;
        UserImage = userImage;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiveerID() {
        return receiveerID;
    }

    public void setReceiveerID(String receiveerID) {
        this.receiveerID = receiveerID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFriendImage() {
        return FriendImage;
    }

    public void setFriendImage(String friendImage) {
        FriendImage = friendImage;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
