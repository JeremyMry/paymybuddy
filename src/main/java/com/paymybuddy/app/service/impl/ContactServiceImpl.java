package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.DTO.ContactSummary;
import com.paymybuddy.app.DTO.ContactUpdate;
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
    public Contact getContact(Long contactId) {
        return contactRepository.findById(contactId)
                                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId));
    }

    @Override
    public Contact getContactByEmail(String email) {
        return contactRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Contact", "email", email));
    }

    @Override
    public Boolean getEmailAvailability(String email) { return !contactRepository.existsByEmail(email); }

    @Override
    public List<Contact> findAllByCurrentUser(Long userId) { return contactRepository.findAllByCurrentUser(userId); }

    @Override
    public List<ContactSummary> getAllContacts(UserPrincipal currentUser) {
        List<Contact> contactList = findAllByCurrentUser(currentUser.getId());
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
            userRepository.existsByEmail(contactSummary.getEmail());
            return false;
        }
    }

    @Override
    public void updateContactFirstName(UserPrincipal currentUser, ContactUpdate contactUpdate) {
        Contact updatedContact = getContact(contactUpdate.getId());
        List<Contact> contactList = findAllByCurrentUser(currentUser.getId());
        contactList.forEach(contact -> {
            if(contact.getEmail().equals(updatedContact.getEmail())) {
                updatedContact.setFirstName(contactUpdate.getNewFirstName());
                logger.info("CONTACT UPDATED");
                contactRepository.save(updatedContact);
            }
        });
    }

    @Override
    public void updateEmail(String email, String oldEmail) {
        Contact contact = getContactByEmail(oldEmail);
        contact.setEmail(email);
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(UserPrincipal currentUser, Long contactId) {
        Contact deleteContact = getContact(contactId);
        List<Contact> contactList = contactRepository.findAllByCurrentUser(currentUser.getId());
        contactList.forEach(contact -> {
            if(contact.getId().equals(deleteContact.getId())) {
                logger.info("CONTACT DELETED");
                contactRepository.deleteById(deleteContact.getId());
            }
        });
    }
}
