package com.paymybuddy.app.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer wallet;

    public User() {}

    public User(String firstName, String lastName, String email, String password, Integer wallet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
    }

    public User(Integer userId, String firstName, String lastName, String email, String password, Integer wallet) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() { return userId; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() { return password; }
    public Integer getWallet() {
        return wallet;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) { this.password = password; }
    public void setWallet(Integer wallet) {
        this.wallet = wallet;
    }
}
