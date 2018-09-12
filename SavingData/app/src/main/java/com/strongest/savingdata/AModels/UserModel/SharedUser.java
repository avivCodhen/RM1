package com.strongest.savingdata.AModels.UserModel;

public class SharedUser {

    private String programUID;
    private String senderUID;
    private String recieverUID;
    private boolean seen;

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
}
