package com.paymybuddy.app.entity;

import javax.persistence.*;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private Long creator;

    public Contact() {}

    public Contact(String email, String firstName, Long creator) {
        this.email = email;
        this.firstName = firstName;
        this.creator = creator;
    }

    public Long getId() { return contactId; }
    public void setId(Long contactId) { this.contactId = contactId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public Long getCreator() { return creator; }
    public void setCreator(Long creator) { this.creator = creator; }
}

