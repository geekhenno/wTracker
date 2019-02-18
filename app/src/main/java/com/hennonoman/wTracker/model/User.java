package com.hennonoman.wTracker.model;

/**
 * Created by paulodichone on 4/17/17.
 */

public class User {


    private String profileImg;
    private String userid;
    private String name;
    private String email;
    private double latit;
    private double longi;
    long timestamp ;
    long dayTimestamp ;

    public User() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(long dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    public User(String profileImg, String userid, String name, String email, double latit, double longi , long timestamp, long dayTimestamp) {
        this.profileImg = profileImg;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.latit = latit;
        this.longi = longi;
        this.timestamp=timestamp;
        this.dayTimestamp=dayTimestamp;
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

    public double getLatit() {
        return latit;
    }

    public void setLatit(double latit) {
        this.latit = latit;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}

