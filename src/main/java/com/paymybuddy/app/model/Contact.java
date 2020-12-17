package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
@Table(name="contact")
public class Contact {

    @Id
    @GeneratedValue
    @Column(name="Id", nullable = false)
    private Long id;

    @Column(name="email", length = 64, nullable = false)
    private String email;

    @Column(name="firstName", length = 64, nullable = false)
    private String firstName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
