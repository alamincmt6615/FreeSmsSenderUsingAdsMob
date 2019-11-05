package com.example.dreamsocialclub.model;

public class UploadPostModel {

    public UploadPostModel() {

    }

    private String post_text;
    private String user_uid;
    private String post_time;
    private String post_date;

    public UploadPostModel(String post_text, String user_uid, String post_time, String post_date) {
        this.post_text = post_text;
        this.user_uid = user_uid;
        this.post_time = post_time;
        this.post_date = post_date;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
