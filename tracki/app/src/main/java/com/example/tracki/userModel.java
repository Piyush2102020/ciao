package com.example.tracki;

public class userModel {
    String gender;
    String age;
    String username;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public userModel(String gender, String age, String username){
        this.age=age;
        this.gender=gender;
        this.username=username;

    }
}
