package com.strongest.savingdata.AModels.UserModel;

import java.util.HashMap;

public class User {

    private String name;
    private String email;
    private HashMap<String, Object> dateJoined;
    private String UID;
    private String userToken;

    public User(String name, String email, HashMap<String, Object> dateJoined){

        this.name = name;
        this.email = email;
        this.dateJoined = dateJoined;
    }

    public User(){

    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getDateJoined() {
        return dateJoined;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
