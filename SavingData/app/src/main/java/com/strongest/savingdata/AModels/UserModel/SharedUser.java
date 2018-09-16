package com.strongest.savingdata.AModels.UserModel;

public class SharedUser {

    private String programUID;
    private String senderUID;
    private String recieverUID;
    private boolean seen;
    private String programName;
    private String senderName;
    private String senderToken;

    public SharedUser() {

    }

    public String getProgramUID() {
        return programUID;
    }

    public String getRecieverUID() {
        return recieverUID;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setProgramUID(String programUID) {
        this.programUID = programUID;
    }

    public void setRecieverUID(String recieverUID) {
        this.recieverUID = recieverUID;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }
}
