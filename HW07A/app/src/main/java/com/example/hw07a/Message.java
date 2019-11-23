package com.example.hw07a;

import java.util.Date;

public class Message {

    public String sender;

    public String receiver;


    public String message;
    public String chatroomname;
    public String sendtime;

    public String getChatroomname() {
        return chatroomname;
    }

    public String getDate() {
        return sendtime;
    }

    public Message(String sender, String receiver, String message, String chatroomname, String sendtime) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatroomname = chatroomname;
        this.sendtime = sendtime;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
