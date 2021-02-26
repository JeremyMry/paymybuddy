package com.paymybuddy.app.dto;

public class ContactUpdateDto {
    private Long id;
    private String newFirstName;

    public ContactUpdateDto(Long id, String newFirstName) {
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
