package com.paymybuddy.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @NotBlank
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(max = 25)
    private String firstName;

    @NotBlank
    @NotNull
    private Long creator;

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

