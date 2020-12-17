package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "firstName", length = 64, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 64, nullable = false)
    private String lastName;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Column(name= "password", length = 64, nullable = false)
    private String password;

    @Column(name = "wallet", nullable = false)
    private Integer wallet;

    public Long getId() { return id; }
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

    public void setId(Long id) {
        this.id = id;
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
