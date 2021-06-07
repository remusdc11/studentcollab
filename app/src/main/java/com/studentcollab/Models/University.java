package com.studentcollab.Models;

public class University {

    private String documentId;

    private String name;

    public University() {}

    public University(String documentId, String name) {
        this.documentId = documentId;
        this.name = name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
