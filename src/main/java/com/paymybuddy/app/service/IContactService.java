package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.dto.ContactSummaryDto;
import com.paymybuddy.app.dto.ContactUpdateDto;
import com.paymybuddy.app.security.UserPrincipal;

import java.util.List;

public interface IContactService {

    Contact getContact(Long contactId);
    Contact getContactByEmail(String email);
    Boolean getEmailAvailability(String email);
    List<Contact> getAllContacts(UserPrincipal currentUser);
    Boolean createContact(UserPrincipal currentUser, ContactSummaryDto contactSummaryDto);
    void updateContactFirstName(UserPrincipal currentUser, ContactUpdateDto contactUpdateDto);
    void updateEmail(String email, String oldEmail);
    void deleteContact(UserPrincipal currentUser, Long contactId);
}
