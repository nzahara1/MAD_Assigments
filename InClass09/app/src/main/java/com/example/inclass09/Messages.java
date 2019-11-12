package com.example.inclass09;

import java.io.Serializable;

public class Messages implements Serializable {

    private String senderFName;

    private String senderLName;

    private String message;

    private String subject;

    private String createdAt;

    private String id;

    private String recipient;

    public Messages(String senderFName, String senderLName, String message, String subject, String createdAt, String id) {
        this.senderFName = senderFName;
        this.senderLName = senderLName;
        this.message = message;
        this.subject = subject;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Messages(String message, String subject, String id, String recipient, String createdAt) {
        this.message = message;
        this.subject = subject;
        this.id = id;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    public String getSenderFName() {
        return senderFName;
    }

    public String getSenderLName() {
        return senderLName;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "senderFName='" + senderFName + '\'' +
                ", senderLName='" + senderLName + '\'' +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", id='" + id + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
