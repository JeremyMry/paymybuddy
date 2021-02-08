package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.ContactDelete;
import com.paymybuddy.app.model.ContactSummary;
import com.paymybuddy.app.model.ContactUpdate;
import com.paymybuddy.app.security.CurrentUser;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.ContactServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    ContactServiceImpl contactServiceImpl;

    @Autowired
    private Logger logger;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List> getAllContacts(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(contactServiceImpl.getAllContacts(currentUser), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createContact(@CurrentUser UserPrincipal currentUser, @RequestBody ContactSummary contactSummary) {
        if(contactServiceImpl.createContact(currentUser, contactSummary)) {
            logger.info("CONTACT CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else  {
            logger.error("CANNOT CREATE THE CONTACT");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/put")
    public ResponseEntity<HttpStatus> updateContactFirstName(@CurrentUser UserPrincipal currentUser, @RequestBody ContactUpdate contactUpdate) {
        contactServiceImpl.updateContactFirstName(currentUser, contactUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteContact(@CurrentUser UserPrincipal currentUser, @RequestBody ContactDelete contactDelete) {
        contactServiceImpl.deleteContact(currentUser, contactDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
