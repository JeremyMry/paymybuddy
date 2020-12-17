package com.paymybuddy.app.controller;

import org.springframework.web.bind.annotation.*;

@RestController()
public class ContactController {

    @GetMapping("/contact")
    public String getContact() {
        return "contact list";
    }

    @PostMapping("/contact/add")
    public String addContact() {
        return "add contact";
    }

    @PutMapping("/contact/put")
    public String putContact() {
        return "put contact";
    }

    @DeleteMapping("/contact/delete")
    public String deleteContact() {
        return "delete contact";
    }
}
