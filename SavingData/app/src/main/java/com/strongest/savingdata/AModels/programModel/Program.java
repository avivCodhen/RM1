package com.strongest.savingdata.AModels.programModel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.security.Key;

import io.reactivex.annotations.NonNull;

/**
 * Created by Cohen on 10/18/2017.
 */
@Entity
public class Program implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String key = "";
    private String creatorUID;
    private String programName;
    private String time;
    private String creator;
    private String programDate;
    private String dbName;
    public boolean isSeen;


    public Program(String creatorUID, String creator, String programName, String time, String programDate, String dbName) {
        this.creatorUID = creatorUID;
        this.creator = creator;
        this.programName = programName;
        this.time = time;
        this.dbName = dbName;

        this.programDate = programDate;
    }

    @Ignore
    public Program(){

    }


    public String getDbName() {
        return dbName;
    }


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProgramDate() {
        return programDate;
    }

    public void setProgramDate(String programDate) {
        this.programDate = programDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        Program p = (Program) obj;
        if (this.getKey().equals(p.getKey())){
            return true;
        }
        return false;
    }
}
