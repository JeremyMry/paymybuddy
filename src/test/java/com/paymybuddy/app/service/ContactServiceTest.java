package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Contact;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.ContactDelete;
import com.paymybuddy.app.model.ContactSummary;
import com.paymybuddy.app.model.ContactUpdate;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.ContactServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class ContactServiceTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactServiceImpl contactService;

    @Test
    public void getContactTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        Assertions.assertTrue(contactService.getContact(1L).isPresent());
    }

    @Test
    public void getContactThatDoesntExistTest() {
        Assertions.assertFalse(contactService.getContact(1L).isPresent());
    }

    @Test
    public void getAllContactsTest() {
        Users user = new Users("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", 1L);
        Contact contact3 = new Contact("pdoe@testmail.com", "Paul", 2L);

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);
        contactRepository.save(contact3);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 2);
    }

    @Test
    public void getAllContactsWhenThereIsNoneTest() {
        Users user = new Users("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO);
        Contact contact = new Contact("pdoe@testmail.com", "Paul", 2L);

        userRepository.save(user);
        contactRepository.save(contact);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 0);
    }

    @Test
    public void getAllContactsWhenThereIsNoCurrentUserTest() {
        Users user = new Users("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", 1L);

        contactRepository.save(contact);
        contactRepository.save(contact2);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 0);
    }


    @Test
    public void createContactTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Users user2 = new Users("john", "doe", "johnny", "johndoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);
        userRepository.save(user2);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertTrue(bool);
        Assertions.assertEquals(contactService.getContact(1L).get().getEmail(), "johndoe@testmail.com");
        Assertions.assertTrue(contactService.getContact(1L).get().getCreator() == 1L);
        Assertions.assertEquals(contactService.getContact(1L).get().getFirstName(), "john");
    }

    @Test
    public void createContactIncorrectMailTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertFalse(bool);
    }

    @Test
    public void createContactWithoutCurrentUserTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertFalse(bool);
    }

    @Test
    public void updateContactFirstNameTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "john", "johnny");
        Contact contact = new Contact("jdoe@testmail.com", "john", 1L);

        userRepository.save(user);
        contactRepository.save(contact);

        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).get().getFirstName(), "johnny");
    }

    @Test
    public void updateContactFirstNameWithoutContactTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "john", "johnny");
        userRepository.save(user);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);
        });

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateContactFirstnameWithEmptyContactListTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 2L);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", 2L);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "john", "johnny");

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).get().getFirstName(), "Joey");
    }

    @Test
    public void updateEmailTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        assertTrue(contactService.updateEmail("joeydoe@testmail.com", "jdoe@testmail.com"));
        assertEquals(contactService.getContact(1L).get().getEmail(), "joeydoe@testmail.com");
    }

    @Test
    public void updateEmailIncorrectOldEmailTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        assertFalse(contactService.updateEmail("joeydoe@testmail.com", "johndoe@testmail.com"));
        assertEquals(contactService.getContact(1L).get().getEmail(), "jdoe@testmail.com");
    }

    @Test
    public void deleteContactTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactDelete contactDelete = new ContactDelete(1L, 1L);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);

        contactRepository.save(contact);
        userRepository.save(users);
        contactService.deleteContact(UserPrincipal.create(users), contactDelete);

        assertFalse(contactService.getContact(1L).isPresent());
    }

    @Test
    public void deleteContactThatDoesntExistTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactDelete contactDelete = new ContactDelete(1L, 1L);
        userRepository.save(users);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> {
            contactService.deleteContact(UserPrincipal.create(users), contactDelete);
        });

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteContactThatIsNotCreatedByTheCurrentUserTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactDelete contactDelete = new ContactDelete(1L, 1L);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 2L);

        contactRepository.save(contact);
        userRepository.save(users);
        contactService.deleteContact(UserPrincipal.create(users), contactDelete);

        assertTrue(contactService.getContact(1L).isPresent());
    }
}
