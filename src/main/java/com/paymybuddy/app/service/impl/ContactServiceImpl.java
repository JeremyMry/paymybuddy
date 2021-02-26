package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.dto.ContactSummaryDto;
import com.paymybuddy.app.dto.ContactUpdateDto;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.IContactService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    Logger logger;

    public ContactServiceImpl() {
    }

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
    public List<Contact> getAllContacts(UserPrincipal currentUser) {
        return currentUser.getContactList();
    }

    @Transactional
    @Override
    public Boolean createContact(UserPrincipal currentUser, ContactSummaryDto contactSummaryDto) {
        if(!userService.getEmailAvailability(contactSummaryDto.getEmail())) {
            User user = UserPrincipal.create(currentUser);
            Contact contact = new Contact(contactSummaryDto.getEmail(), contactSummaryDto.getFirstName(), user);
            contactRepository.save(contact);
            return true;
        } else {
            userService.getEmailAvailability(contactSummaryDto.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public void updateContactFirstName(UserPrincipal currentUser, ContactUpdateDto contactUpdateDto) {
        Contact updatedContact = getContact(contactUpdateDto.getId());
        List<Contact> contactList = currentUser.getContactList();
        contactList.forEach(contact -> {
            if(contact.getEmail().equals(updatedContact.getEmail())) {
                updatedContact.setFirstName(contactUpdateDto.getNewFirstName());
                logger.info("CONTACT UPDATED");
                contactRepository.save(updatedContact);
            }
        });
    }

    @Transactional
    @Override
    public void updateEmail(String email, String oldEmail) {
        Contact contact = getContactByEmail(oldEmail);
        contact.setEmail(email);
        contactRepository.save(contact);
    }

    @Transactional
    @Override
    public void deleteContact(UserPrincipal currentUser, Long contactId) {
        Contact deleteContact = getContact(contactId);
        List<Contact> contactList = currentUser.getContactList();
        contactList.forEach(contact -> {
            if(contact.getId().equals(deleteContact.getId())) {
                logger.info("CONTACT DELETED");
                contactRepository.deleteById(deleteContact.getId());
            }
        });
    }
}
