package com.hennonoman.wTracker.model;

/**
 * Created by paulodichone on 4/17/17.
 */

public class Group {



    private String groupid;
    private String groupName;
    private String groupImg;
    private String groupAdmin;

    public Group()
    {

    }

    public Group(String groupid, String groupName, String groupImg, String groupAdmin) {
        this.groupid = groupid;
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.groupAdmin = groupAdmin;
    }



    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
