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


    public Contact() {}

    public Contact(String email, String firstName, Integer creator) {
        this.email = email;
        this.firstName = firstName;
        this.creator = creator;
    }

    public Integer getId() { return contactId; }
    public void setId(Integer contactId) { this.contactId = contactId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
}
