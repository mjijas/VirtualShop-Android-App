package com.example.ijasahamed.lezyv2.models;

public class ratingsComments {
    private int RC_id;
    private int ratings;
    private String comments;
    private String date_time;
    private String user_name;

    public ratingsComments() {
    }

    public ratingsComments(int RC_id, int ratings, String comments, String date_time, String user_name) {
        this.RC_id = RC_id;
        this.ratings = ratings;
        this.comments = comments;
        this.date_time = date_time;
        this.user_name = user_name;
    }

    public int getRC_id() {
        return RC_id;
    }

    public int getRatings() {
        return ratings;
    }

    public String getComments() {
        return comments;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setRC_id(int RC_id) {
        this.RC_id = RC_id;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
