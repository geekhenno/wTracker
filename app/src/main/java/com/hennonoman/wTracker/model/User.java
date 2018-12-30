package com.hennonoman.wTracker.model;

/**
 * Created by paulodichone on 4/17/17.
 */

public class User {


    private String profileImg;
    private String userid;
    private String name;
    private String email;

    public User() {
    }

    public User(String name, String email, String userid, String profileImg) {


        this.name = name;
        this.email = email;
        this.userid = userid;
        this.profileImg=profileImg;

    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
