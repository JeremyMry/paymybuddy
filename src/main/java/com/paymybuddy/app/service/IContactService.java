package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.DTO.ContactSummary;
import com.paymybuddy.app.DTO.ContactUpdate;
import com.paymybuddy.app.security.UserPrincipal;

import javax.transaction.Transactional;
import java.util.List;

public interface IContactService {

    Contact getContact(Long contactId);
    Contact getContactByEmail(String email);
    Boolean getEmailAvailability(String email);
    List<Contact> findAllByCurrentUser(Long userId);
    List<ContactSummary> getAllContacts(UserPrincipal currentUser);
    @Transactional
    Boolean createContact(UserPrincipal currentUser, ContactSummary contactSummary);
    void updateContactFirstName(UserPrincipal currentUser, ContactUpdate contactUpdate);
    void updateEmail(String email, String oldEmail);
    void deleteContact(UserPrincipal currentUser, Long contactId);
}
