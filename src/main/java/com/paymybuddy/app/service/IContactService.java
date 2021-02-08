package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Contact;
import com.paymybuddy.app.model.ContactDelete;
import com.paymybuddy.app.model.ContactSummary;
import com.paymybuddy.app.model.ContactUpdate;
import com.paymybuddy.app.security.UserPrincipal;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IContactService {

    Optional<Contact> getContact(Long contactId);
    List<ContactSummary> getAllContacts(UserPrincipal currentUser);
    @Transactional
    Boolean createContact(UserPrincipal currentUser, ContactSummary contactSummary);
    void updateContactFirstName(UserPrincipal currentUser, ContactUpdate contactUpdate);
    Boolean updateEmail(String email, String oldEmail);
    void deleteContact(UserPrincipal currentUser, ContactDelete contactDelete);
}
