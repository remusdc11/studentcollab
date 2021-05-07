package com.studentcollab.Models;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Project {
    private String documentId;
    private String title;
    private String description;
    private int numberOfUsers;
    private long startDate;
    private long endDate;
    private ProjectStatus status;
    private String ownerId;

    private ArrayList<String> tags;

    private ArrayList<String> teamMembers;

    private ArrayList<String> pendingMembers;

    public Project() {
        this.tags = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.pendingMembers = new ArrayList<>();
    }

    public Project (String documentId, String name, String description, int numberOfUsers, long startDate, long endDate, ProjectStatus status, String ownerId, ArrayList<String> tags, ArrayList<String> teamMembers, ArrayList<String> pendingMembers) {
        this.documentId = documentId;
        this.title = name;
        this.description = description;
        this.numberOfUsers = numberOfUsers;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.ownerId = ownerId;
        this.tags = tags;
        this.teamMembers = teamMembers;
        this.pendingMembers = pendingMembers;
    }

    public Project (String name, String description, int numberOfUsers, long startDate, long endDate, ProjectStatus status, String ownerId, ArrayList<String> tags, ArrayList<String> teamMembers, ArrayList<String> pendingMembers) {
        this.title = name;
        this.description = description;
        this.numberOfUsers = numberOfUsers;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.ownerId = ownerId;
        this.tags = tags;
        this.teamMembers = teamMembers;
        this.pendingMembers = pendingMembers;
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
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
        long now = (new Timestamp(System.currentTimeMillis())).getTime();
        if (this.startDate > now)
            this.status = ProjectStatus.NEW;
        else {
            if(now <= this.endDate)
                this.status = ProjectStatus.STARTED;
            else
                this.status = ProjectStatus.ENDED;
        }
        return status;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void addTeamMember(String memberId) {
        this.teamMembers.add(memberId);
    }

    public boolean removeTeamMember(String memberId) {
        return this.teamMembers.remove(memberId);
    }

    public int getNumberOfCurrentUsers() {
        return this.teamMembers.size();
    }

    public int getNumberOfMissingUsers() {
        return this.numberOfUsers - this.teamMembers.size();
    }

    public ArrayList<String> getPendingMembers() {
        return pendingMembers;
    }

    public void setPendingMembers(ArrayList<String> pendingMembers) {
        this.pendingMembers = pendingMembers;
    }

    public void AddPendingMember(String userId) {
        this.pendingMembers.add(userId);
    }

    public boolean RemovePendingMember(String userId) {
        return this.pendingMembers.remove(userId);
    }
}
