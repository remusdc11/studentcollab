package com.studentcollab.Models;

import java.sql.Timestamp;



public class Project {
    private String documentId;
    private String name;
    private String description;
    private int numberOfUsers;
    private Timestamp startDate;
    private Timestamp endDate;
    private ProjectStatus status;
}
