package com.studentcollab.Models;

public class Review {

    private String projectId, userId, title, review;
    private int rating;
    private long date;

    public Review(String projectId, String userId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    public Review(String projectId, String userId, String title, String review, int rating) {
        this.projectId = projectId;
        this.userId = userId;
        this.title = title;
        this.review = review;
        this.rating = rating;
    }

    public Review() { }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
