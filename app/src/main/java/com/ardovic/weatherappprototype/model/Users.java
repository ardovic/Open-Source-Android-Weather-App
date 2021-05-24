package com.ardovic.weatherappprototype.model;

public class Users {

    String userID, userName, mail, profilePic;

    public Users(String uID, String mail, String userName, String profilePic) {
        this.userID = uID;
        this.userName = userName;
        this.mail = mail;
        this.profilePic = profilePic;
    }

    public Users(){}

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getMail() {
        return mail;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
