package com.paymybuddy.app.DTO;

public class ContactUpdate {
    private Long id;
    private String newFirstName;

    public ContactUpdate(Long id, String newFirstName) {
        this.id = id;
        this.newFirstName = newFirstName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNewFirstName() {
        return newFirstName;
    }
    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }
}
