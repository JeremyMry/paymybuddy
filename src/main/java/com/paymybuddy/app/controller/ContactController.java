package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/contact/{id}")
    public ResponseEntity<List> getAllContacts(@PathVariable("id") Integer userId) {
        try {
            List<Contact> contactData = contactRepository.findAllByCurrentUser(userId);
            return new ResponseEntity<>(contactData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        try {
            Contact contact1 = contactRepository.save(new Contact(contact.getId(), contact.getEmail(), contact.getFirstName(), contact.getUser()));
            return new ResponseEntity<>(contact1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/contact/put/{id}")
    public ResponseEntity<Contact> updateUser(@PathVariable("id") Integer contactId, @RequestBody Contact contact) {
        Optional<Contact> contactData = contactRepository.findById(contactId);
        if (contactData.isPresent()) {
            Contact contact1 = contactData.get();
            contact1.setFirstName(contact.getFirstName());
            contact1.setEmail(contact.getEmail());
            return new ResponseEntity<>(contactRepository.save(contact1), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/contact/delete/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("id") Integer contactId) {
        try {
            contactRepository.deleteById(contactId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
