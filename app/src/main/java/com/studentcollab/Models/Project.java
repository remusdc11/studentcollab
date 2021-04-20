package com.studentcollab.Models;

public class Project {
    private String documentId;
    private String title;
    private String description;
    private int numberOfUsers;
    private long startDate;
    private long endDate;
    private ProjectStatus status;

    public Project(String documentId, String name, String description, int numberOfUsers, long startDate, long endDate, ProjectStatus status) {
        this.documentId = documentId;
        this.title = name;
        this.description = description;
        this.numberOfUsers = numberOfUsers;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Project(String name, String description, int numberOfUsers, long startDate, long endDate, ProjectStatus status) {
        this.title = name;
        this.description = description;
        this.numberOfUsers = numberOfUsers;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
