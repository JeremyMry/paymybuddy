package com.paymybuddy.app.controller;

import org.springframework.web.bind.annotation.*;

@RestController("/paymybuddy/contact")
public class ContactController {

    @GetMapping("")
    public String getContact() {
        return "contact list";
    }

    @PostMapping("/paymybuddy/contact/add")
    public String addContact() {
        return "add contact";
    }

    @PutMapping("/paymybuddy/contact/put")
    public String putContact() {
        return "put contact";
    }

    @DeleteMapping("/paymybuddy/contact/delete")
    public String deleteContact() {
        return "delete contact";
    }
}
