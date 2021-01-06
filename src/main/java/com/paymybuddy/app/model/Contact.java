package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Contact {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String firstName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
