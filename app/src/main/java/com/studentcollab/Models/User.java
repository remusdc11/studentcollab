package com.studentcollab.Models;

import java.util.Date;

public class User {

    private String documentId;
    private String userId;
    private String displayName;
    private String emailAddress;
    private String firstName, lastName;
    private Date joinDate;
    private University university;


    public User(String userId, String emailAddress) {
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return this.emailAddress;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
