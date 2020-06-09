package com.example.essaouiraapp;

public class Message {
    private String Email;
    private String id;
    private String message;
    private String date;

    public Message() {
    }

    public Message(String email, String message ,String dat,String id) {
        Email = email;
        date = dat;
        this.message = message;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
