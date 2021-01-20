package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    public Optional<Contact> getContact(Integer contactId) { return contactRepository.findById(contactId); }

    public List<Contact> getAllContacts(Integer userId) { return contactRepository.findAllByCurrentUser(userId); }

    @Transactional
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact updateContact(Contact contact, Integer contactId) {
        Contact updatedContact = contactRepository.findById(contactId).get();
        updatedContact.setFirstName(contact.getFirstName());
        updatedContact.setEmail(contact.getEmail());
        return contactRepository.save(updatedContact);
    }

    public void deleteContact(Integer contactId) {
        contactRepository.deleteById(contactId);
    }
}
