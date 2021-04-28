package com.studentcollab.Models;

public class UserProjectDTO extends User {

    public boolean userAccepted = false;

    public boolean isUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(boolean userAccepted) {
        this.userAccepted = userAccepted;
    }

    public UserProjectDTO() {
        //super();
    }
}
