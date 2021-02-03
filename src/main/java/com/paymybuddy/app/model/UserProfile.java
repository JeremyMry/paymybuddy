package com.paymybuddy.app.model;

import java.time.Instant;

public class UserProfile {

    private String username;
    private String email;

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
