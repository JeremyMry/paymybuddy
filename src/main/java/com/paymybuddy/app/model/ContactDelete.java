package com.paymybuddy.app.model;

public class ContactDelete {
    private Long id;
    private Long creator;

    public ContactDelete(Long id, Long creator) {
        this.id = id;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreator() {
        return creator;
    }
    public void setCreator(Long creator) {
        this.creator = creator;
    }
}
