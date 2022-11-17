package com.my.vibras.chat;

public class AllChatUserModel {
    String id;
    String name;
    String image;
    String message;
    String receiver_id;
    String sender_id;
    public AllChatUserModel(String id, String name, String image, String message ,String receiver_id,
            String sender_id  ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.message = message;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



