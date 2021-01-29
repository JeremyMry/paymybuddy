package com.paymybuddy.app.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Set;

@Entity
@DynamicInsert
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 64, nullable = false)
    private java.lang.String firstName;

    @Column(length = 64, nullable = false)
    private java.lang.String lastName;

    @Column(length = 64, nullable = false)
    private java.lang.String email;

    @Column(length = 64, nullable = false)
    private java.lang.String password;

    @Column(columnDefinition = "integer default 0")
    private Integer wallet;

    public User() {}

    public User(java.lang.String firstName, java.lang.String lastName, java.lang.String email, java.lang.String password, Integer wallet) {
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
    public java.lang.String getFirstName() {
        return firstName;
    }
    public java.lang.String getLastName() {
        return lastName;
    }
    public java.lang.String getEmail() {
        return email;
    }
    public java.lang.String getPassword() { return password; }
    public Integer getWallet() {
        return wallet;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    public void setPassword(java.lang.String password) { this.password = password; }
    public void setWallet(Integer wallet) {
        this.wallet = wallet;
    }
}
