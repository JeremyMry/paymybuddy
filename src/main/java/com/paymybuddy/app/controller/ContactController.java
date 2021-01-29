package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.service.ContactService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private Logger logger;

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<Optional> getContact(@PathVariable("contactId") Integer contactId) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(contactService.getContact(contactId), HttpStatus.OK);
    }

    @GetMapping("/contact/all/{userId}")
    public ResponseEntity<List> getAllContacts(@PathVariable("userId") Integer userId) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(contactService.getAllContacts(userId), HttpStatus.OK);
    }

    @PostMapping("/contact")
    public ResponseEntity<HttpStatus> createContact(@RequestBody Contact contact) {
        if(contactService.createContact(contact)) {
            logger.info("CONTACT CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else  {
            logger.error("CANNOT CREATE THE CONTACT");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/contact/put/{contactId}")
    public ResponseEntity<HttpStatus> updateContactFirstName(@PathVariable("contactId") Integer contactId, @RequestBody String firstName) {
        if(contactService.updateContactFirstName(firstName, contactId)) {
            logger.info("CONTACT UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("CANNOT UPDATE THE CONTACT");
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/contact/delete/{contactId}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("contactId") Integer contactId) {
        contactService.deleteContact(contactId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
