package com.afrochic.waste_assistant;

public class User {
    public String username;
    public String email;
    public String message_token;

    public User() {

    }

    public User(String username, String email, String message_token) {
        this.username = username;
        this.email = email;
        this.message_token = message_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage_token() {
        return message_token;
    }

    public void setMessage_token(String message_token) {
        this.message_token = message_token;
    }

}
