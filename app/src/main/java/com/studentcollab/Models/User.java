package com.studentcollab.Models;

import java.sql.Timestamp;
import java.util.Date;

public class User {

    private String documentId;
    private String userId;
    private String emailAddress;
    private String firstName, lastName;
    private Long joinDate;
    private String universityDocumentId;

    private Boolean initialized;

    public User () {

    }

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

    public Long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Long  joinDate) {
        this.joinDate = joinDate;
    }

    public String getUniversityDocumentId() {
        return universityDocumentId;
    }

    public void setUniversityDocumentId(String universityDocumentId) {
        this.universityDocumentId = universityDocumentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return this.emailAddress;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }
}
