package com.paymybuddy.app.model;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String firstName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public Contact() {}

    public Contact(Integer contactId, String email, String firstName, User user) {
        this.contactId = contactId;
        this.email = email;
        this.firstName = firstName;
        this.user = user;
    }

    public Contact(String email, String firstName, User user) {
        this.email = email;
        this.firstName = firstName;
        this.user = user;
    }

    public Integer getId() { return contactId; }
    public void setId(Integer contactId) { this.contactId = contactId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
