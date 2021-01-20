package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class ContactController {

    @Autowired
    ContactService contactService;

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<Optional> getContact(@PathVariable("contactId") Integer contactId) {
        return new ResponseEntity<>(contactService.getContact(contactId), HttpStatus.OK);
    }

    @GetMapping("/contact/all/{userId}")
    public ResponseEntity<List> getAllContacts(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(contactService.getAllContacts(userId), HttpStatus.OK);
    }

    @PostMapping("/contact")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        return new ResponseEntity<>(contactService.createContact(contact), HttpStatus.CREATED);
    }

    @PutMapping("/contact/put/{contactId}")
    public ResponseEntity<Contact> updateContact(@PathVariable("contactId") Integer contactId, @RequestBody Contact contact) {
        return new ResponseEntity<>(contactService.updateContact(contact, contactId), HttpStatus.OK);
    }

    @DeleteMapping("/contact/delete/{contactId}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("contactId") Integer contactId) {
        contactService.deleteContact(contactId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
