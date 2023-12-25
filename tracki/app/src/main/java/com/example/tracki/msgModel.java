package com.example.tracki;

public class msgModel {
    String sender;
    String gender;
    String uid;
    String age;
    String msgId;
    String message;
    String imageUrl;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public msgModel(String sender, String gender, String uid, String age, String msgId, String message, String imageUrl){
        this.age=age;
        this.msgId=msgId;
        this.uid=uid;
        this.gender=gender;
        this.sender=sender;
        this.message=message;
        this.imageUrl=imageUrl;

    }

}
