package com.example.dreamsocialclub.model;

public class SignUPModel {
    private String uid;
    private String name;
    private String phone_number;
    private String profile_pic_url;
    private String create_date;
    private String create_time;

    public SignUPModel() {

    }

    public SignUPModel(String uid, String name, String phone_number, String profile_pic_url, String create_date, String create_time) {
        this.uid = uid;
        this.name = name;
        this.phone_number = phone_number;
        this.profile_pic_url = profile_pic_url;
        this.create_date = create_date;
        this.create_time = create_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
