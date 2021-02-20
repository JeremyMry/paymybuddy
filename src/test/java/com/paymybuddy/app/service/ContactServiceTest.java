package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.DTO.ContactSummary;
import com.paymybuddy.app.DTO.ContactUpdate;
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

    // get a specific contact with his id / return a contact
    @Test
    public void getContactTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        Assertions.assertEquals(contactService.getContact(1L).getEmail(), "jdoe@testmail.com");
    }


    // get a specific contact that doesnt exist / throw an exception
    @Test
    public void getContactThatDoesntExistTest() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.getContact(1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // get all the contact of the current user / return a List of contact
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

    // get all the contact of the current user when there is none / return an empty List
    @Test
    public void getAllContactsWhenThereIsNoneTest() {
        Users user = new Users("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO);
        Contact contact = new Contact("pdoe@testmail.com", "Paul", 2L);

        userRepository.save(user);
        contactRepository.save(contact);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 0);
    }

    // get all the contact of the current user when there is no current user / return an empty List
    @Test
    public void getAllContactsWhenThereIsNoCurrentUserTest() {
        Users user = new Users("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", 1L);

        contactRepository.save(contact);
        contactRepository.save(contact2);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 0);
    }

    // create a contact with the id of the current user / return true
    @Test
    public void createContactTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Users user2 = new Users("john", "doe", "johnny", "johndoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);
        userRepository.save(user2);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertTrue(bool);
        Assertions.assertEquals(contactService.getContact(1L).getEmail(), "johndoe@testmail.com");
        assertEquals((long) contactService.getContact(1L).getCreator(), 1L);
        Assertions.assertEquals(contactService.getContact(1L).getFirstName(), "john");
    }

    // create a contact with the id of the current user but the email doesn't exist in db / return false
    @Test
    public void createContactIncorrectMailTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertFalse(bool);
    }

    // create a contact without current user / return false
    @Test
    public void createContactWithoutCurrentUserTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummary("john", "johndoe@testmail.com"));

        Assertions.assertFalse(bool);
    }

    // update the contact firstName
    @Test
    public void updateContactFirstNameTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "johnny");
        Contact contact = new Contact("jdoe@testmail.com", "john", 1L);

        userRepository.save(user);
        contactRepository.save(contact);

        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "johnny");
    }

    // update the contact firstName but with the wrong current user creator
    @Test
    public void updateContactFirstNameThatIsNotPresentTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "johnny");
        Contact contact = new Contact("jdoe@testmail.com", "john", 2L);
        Contact contact2 = new Contact("jodoe@testmail.com", "john", 1L);

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "john");
    }

    // update the contact firstName but without current user link to the contact / throw an exception
    @Test
    public void updateContactFirstNameWithoutContactTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "johnny");
        userRepository.save(user);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // update the contact firstName when there is none created by the current user
    @Test
    public void updateContactFirstnameWithEmptyContactListTest() {
        Users user = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 2L);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", 2L);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "johnny");

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "Joey");
    }

    // update the contact email
    @Test
    public void updateEmailTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        contactService.updateEmail("jodoe@testmail.com", "jdoe@testmail.com");

        assertEquals(contactService.getContact(1L).getEmail(), "jodoe@testmail.com");
    }

    // update the contact email with incorrect email / throw an exception
    @Test
    public void updateEmailIncorrectOldEmailTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);
        contactRepository.save(contact);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.updateEmail("jodoe@testmail.com", "jodoe@testmail.com"));

        String expectedMessage = "Contact not found with email : 'jodoe@testmail.com'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact with his id
    @Test
    public void deleteContactTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 1L);

        contactRepository.save(contact);
        userRepository.save(users);
        contactService.deleteContact(UserPrincipal.create(users), 1L);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.getContact(1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact doesn't exist / throw an exception
    @Test
    public void deleteContactThatDoesntExistTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(users);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.deleteContact(UserPrincipal.create(users), 1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact that's not created by the current user
    @Test
    public void deleteContactThatIsNotCreatedByTheCurrentUserTest() {
        Users users = new Users("joe", "doe", "joey", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 2L);
        Contact contact1 = new Contact("jdoe@testmail.com", "Joey", 1L);

        contactRepository.save(contact);
        contactRepository.save(contact1);
        userRepository.save(users);
        contactService.deleteContact(UserPrincipal.create(users), 1L);

        assertEquals(contactService.getContact(1L).getFirstName(), "Joey");
    }

    // get the availability of an email / return true if that's the case
    @Test
    public void getEmailAvailabilityTest() {
        Assertions.assertTrue(contactService.getEmailAvailability("jdoe@testmail.com"));
    }

    // get the availability of an email / return false if the email isn't available
    @Test
    public void getEmailAvailabilityUnavailableTest() {
        Contact contact = new Contact("jdoe@testmail.com", "Joey", 2L);

        contactRepository.save(contact);

        Assertions.assertFalse(contactService.getEmailAvailability("jdoe@testmail.com"));
    }
}
