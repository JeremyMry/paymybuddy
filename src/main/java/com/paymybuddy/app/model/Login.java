package com.paymybuddy.app.model;

public class Login {
    private User email;
    private User password;

    public Login() { }

    public Login(User email, User password) {
        this.email = email;
        this.password = password;
    }

    public User getEmail() { return email; }
    public void setEmail(User email) { this.email = email; }

    public User getPassword() { return password; }
    public void setPassword(User password) { this.password = password; }
}
