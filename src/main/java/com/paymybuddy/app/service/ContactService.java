package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.ContactRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Logger logger;

    public Optional<Contact> getContact(Integer contactId) { return contactRepository.findById(contactId); }

    public List<Contact> getAllContacts(Integer userId) { return contactRepository.findAllByCurrentUser(userId); }

    public Contact findByEmail(String email) {return contactRepository.findByEmail(email); }

    @Transactional
    public Boolean createContact(Contact contact) {
        try {
            User user = userService.findByEmail(contact.getEmail());
            User user1 = userService.getProfile(contact.getCreator()).get();
            if(user != null || user1.getId() != null) {
                contact.setCreator(user1.getId());
                contactRepository.save(contact);
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Boolean updateContactFirstName(java.lang.String firstName, Integer contactId) {
        if(contactRepository.findById(contactId).isEmpty()) {
            return false;
        } else {
            Contact updatedContact = contactRepository.findById(contactId).get();
            updatedContact.setFirstName(firstName);
            contactRepository.save(updatedContact);
            return true;
        }
    }

    public Boolean updateEmail(String email, String oldEmail) {
        if(findByEmail(oldEmail) == null) {
            return false;
        } else {
            Contact updatedContact = findByEmail(oldEmail);
            updatedContact.setEmail(email);
            contactRepository.save(updatedContact);
            logger.info("CONTACT UPDATED");
            return true;
        }
    }

    public void deleteContact(Integer contactId) {
        contactRepository.deleteById(contactId);
    }
}
