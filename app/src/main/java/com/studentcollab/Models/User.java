package com.studentcollab.Models;

public class User {

    private String documentId;
    private String userId;
    private String userEmail;
    private String firstName, lastName;
    private Long joinDate;
    private String universityDocumentId;
    private int score;
    private int reviews;

    private Boolean initialized;

    public User () {

    }

    public User(String userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
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

    public String getFullName() {
        return firstName + " " + lastName;
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
        return this.userEmail;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public float computeUserRating() {
        if (reviews > 0)
            return (float) (score / reviews);
        else
            return 0;
    }

}
