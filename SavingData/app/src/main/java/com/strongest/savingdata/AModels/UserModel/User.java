package com.strongest.savingdata.AModels.UserModel;

import java.util.HashMap;

public class User {

    private String name;
    private String email;
    private HashMap<String, Object> dateJoined;

    public User(String name, String email, HashMap<String, Object> dateJoined){

        this.name = name;
        this.email = email;
        this.dateJoined = dateJoined;
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
}
