package com.studentcollab.Models;

public class UserProjectDTO extends User {

    private boolean userAccepted = false;

    private boolean isOwner = false;

    public boolean isUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(boolean userAccepted) {
        this.userAccepted = userAccepted;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public UserProjectDTO() {
        //super();
    }
}
