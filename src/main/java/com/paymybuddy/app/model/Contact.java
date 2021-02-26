package com.paymybuddy.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@JsonIgnoreProperties({"creator", "debtor", "creditor"})
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @NotNull
    @Size(max = 40)
    private String email;

    @NotNull
    @Size(max = 40)
    private String firstName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator",referencedColumnName="id")
    private User creator;

    public Contact() {}

    public Contact(String email, String firstName, User creator) {
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

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }
}

