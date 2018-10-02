package com.example.kubik.chat;

public class Message {
    private String userName;
    private String message;
    private long date;

    public Message(String userName, String message, long date) {
        this.userName = userName;
        this.message = message;
        this.date = date;
    }

    public Message(String userName, String message) {
        this.userName = userName;
        this.message = message;
        this.date = System.currentTimeMillis();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}