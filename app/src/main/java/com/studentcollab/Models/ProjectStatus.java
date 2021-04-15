package com.studentcollab.Models;

public enum ProjectStatus {

    NEW (0),
    READY(1),
    STARTED(2),
    ENDED(3);

    public final int status;
    private ProjectStatus(int status) {
        this.status = status;
    }
}
