package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.entity.Contact;
import com.paymybuddy.app.model.ContactDelete;
import com.paymybuddy.app.model.ContactSummary;
import com.paymybuddy.app.model.ContactUpdate;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.IContactService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Logger logger;

    @Override
    public Optional<Contact> getContact(Long contactId) { return contactRepository.findById(contactId); }

    @Override
    public List<ContactSummary> getAllContacts(UserPrincipal currentUser) {
        List<Contact> contactList = contactRepository.findAllByCurrentUser(currentUser.getId());
        List<ContactSummary> contactSummaryList = new ArrayList<>();
        contactList.forEach(contact -> {
            ContactSummary contactSummary = new ContactSummary(contact.getEmail(), contact.getFirstName());
            contactSummaryList.add(contactSummary);
        });
        return contactSummaryList;
    }

    @Override
    public Boolean createContact(UserPrincipal currentUser, ContactSummary contactSummary) {
        if(userRepository.existsByEmail(contactSummary.getEmail())) {
            contactRepository.save(new Contact(contactSummary.getEmail(), contactSummary.getFirstName(), currentUser.getId()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateContactFirstName(UserPrincipal currentUser, ContactUpdate contactUpdate) {
        Contact updatedContact = contactRepository.findById(contactUpdate.getId())
                                                  .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactUpdate.getId()));

        List<Contact> contactList = contactRepository.findAllByCurrentUser(currentUser.getId());
        contactList.forEach(contact -> {
            if(contact.getEmail().equals(updatedContact.getEmail())) {
                updatedContact.setFirstName(contactUpdate.getNewFirstName());
                logger.info("CONTACT UPDATED");
                contactRepository.save(updatedContact);
            }
        });
    }

    @Override
    public Boolean updateEmail(String email, String oldEmail) {
        Optional<Contact> contact = contactRepository.findByEmail(oldEmail);
        if(contact.isPresent()) {
            contact.get().setEmail(email);
            contactRepository.save(contact.get());
            return true;
        } else {
            logger.info("THERE IS NO CONTACT WITH THIS EMAIL");
            return false;
        }
    }

    @Override
    public void deleteContact(UserPrincipal currentUser, ContactDelete contactDelete) {
        Contact deleteContact = contactRepository.findById(contactDelete.getId())
                                                 .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactDelete.getId()));

        List<Contact> contactList = contactRepository.findAllByCurrentUser(currentUser.getId());
        contactList.forEach(contact -> {
            if(contact.getId().equals(deleteContact.getId())) {
                logger.info("CONTACT DELETED");
                contactRepository.deleteById(deleteContact.getId());
            }
        });
    }
}
