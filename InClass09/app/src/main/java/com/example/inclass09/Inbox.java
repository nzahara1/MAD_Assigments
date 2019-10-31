package com.example.inclass09;

import java.util.List;

public class Inbox {

    private String status;

    private List<Messages> messages;

    public Inbox(String status, List<Messages> messages) {
        this.status = status;
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "Inbox{" +
                "status='" + status + '\'' +
                ", messages=" + messages +
                '}';
    }
}
