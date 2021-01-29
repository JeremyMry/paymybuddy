package com.paymybuddy.app.model;

import javax.persistence.*;


@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    @Column(length = 64, nullable = false)
    private java.lang.String email;

    @Column(length = 64, nullable = false)
    private java.lang.String firstName;

    @Column
    private Integer creator;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public Contact() {}

    public Contact(Integer contactId, java.lang.String email, java.lang.String firstName, Integer creator) {
        this.contactId = contactId;
        this.email = email;
        this.firstName = firstName;
        this.creator = creator;
    }

    public Contact(java.lang.String email, java.lang.String firstName, User user) {
        this.email = email;
        this.firstName = firstName;
        this.user = user;
    }

    public Integer getId() { return contactId; }
    public void setId(Integer contactId) { this.contactId = contactId; }
    public java.lang.String getEmail() { return email; }
    public void setEmail(java.lang.String email) { this.email = email; }
    public java.lang.String getFirstName() { return firstName; }
    public void setFirstName(java.lang.String firstName) { this.firstName = firstName; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
}
