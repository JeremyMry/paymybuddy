package com.paymybuddy.app.DTO;

public class ContactUpdate {
    private Long id;
    private String oldFirstName;
    private String newFirstName;

    public ContactUpdate(Long id, String oldFirstName, String newFirstName) {
        this.id = id;
        this.oldFirstName = oldFirstName;
        this.newFirstName = newFirstName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOldFirstName() { return oldFirstName; }
    public void setOldFirstName(String oldFirstName) {
        this.oldFirstName = oldFirstName;
    }

    public String getNewFirstName() {
        return newFirstName;
    }
    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }
}
