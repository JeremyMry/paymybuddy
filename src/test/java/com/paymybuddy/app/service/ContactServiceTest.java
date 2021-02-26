package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.dto.ContactSummaryDto;
import com.paymybuddy.app.dto.ContactUpdateDto;
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
import java.util.ArrayList;
import java.util.List;

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
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
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
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paul", "p.b@testmail.com", "ee", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user1 = new User("paulo", "doe", "paulo", "pdoe.b@testmail.com", "ee", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user1);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", user);
        Contact contact3 = new Contact("pdoe@testmail.com", "Paul", user1);
        contactRepository.save(contact);
        contactRepository.save(contact2);
        contactRepository.save(contact3);

        user.getContactList().add(contact);
        user.getContactList().add(contact2);
        userRepository.save(user);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 2);
    }

    // get all the contact of the current user when there is none / return an empty List
    @Test
    public void getAllContactsWhenThereIsNoneTest() {
        User user = new User();
        userRepository.save(user);

        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(user)).size(), 0);
    }

    // create a contact with the id of the current user / return true
    @Test
    public void createContactTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paul", "p.b@testmail.com", "ee", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user2 = new User("paul", "doe", "paulo", "pdoe@testmail.com", "ee", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user2);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummaryDto("paulo", "pdoe@testmail.com"));

        Assertions.assertTrue(bool);
        Assertions.assertEquals(contactService.getContact(1L).getEmail(), "pdoe@testmail.com");
        Assertions.assertEquals(contactService.getContact(1L).getFirstName(), "paulo");
        Assertions.assertEquals(contactService.getAllContacts(UserPrincipal.create(userRepository.findById(1L).get())).size(), 1);
    }

    // create a contact with the id of the current user but the email doesn't exist in db / return false
    @Test
    public void createContactIncorrectMailTest() {
        User user = new User();
        userRepository.save(user);

        Boolean bool = contactService.createContact(UserPrincipal.create(user), new ContactSummaryDto("john", "johndoe@testmail.com"));

        Assertions.assertFalse(bool);
    }

    // update the contact firstName
    @Test
    public void updateContactFirstNameTest() {
        User user = new User();
        userRepository.save(user);

        Contact contact = new Contact("jdoe@testmail.com", "john", user);
        contactRepository.save(contact);

        user.getContactList().add(contact);
        userRepository.save(user);

        ContactUpdateDto contactUpdate = new ContactUpdateDto(1L, "johnny");
        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "johnny");
    }

    // update the contact firstName but with the wrong current user creator
    @Test
    public void updateContactFirstNameThatIsNotPresentTest() {
        User user = new User();
        User user1 = new User();
        userRepository.save(user);
        userRepository.save(user1);

        Contact contact = new Contact("jdoe@testmail.com", "john", user1);
        Contact contact2 = new Contact("jodoe@testmail.com", "john", user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        ContactUpdateDto contactUpdate = new ContactUpdateDto(1L, "johnny");
        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "john");
    }

    // update the contact firstName but without current user link to the contact / throw an exception
    @Test
    public void updateContactFirstNameWithoutContactTest() {
        User user = new User();
        ContactUpdateDto contactUpdate = new ContactUpdateDto(1L, "johnny");
        userRepository.save(user);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // update the contact firstName when there is none created by the current user
    @Test
    public void updateContactFirstnameWithEmptyContactListTest() {
        User user = new User();
        User user1 = new User();
        userRepository.save(user);
        userRepository.save(user1);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user1);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", user1);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        ContactUpdateDto contactUpdate = new ContactUpdateDto(1L, "johnny");
        contactService.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        assertEquals(contactService.getContact(1L).getFirstName(), "Joey");
    }

    // update the contact email
    @Test
    public void updateEmailTest() {
        User user = new User();
        userRepository.save(user);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        contactRepository.save(contact);

        contactService.updateEmail("jodoe@testmail.com", "jdoe@testmail.com");

        assertEquals(contactService.getContact(1L).getEmail(), "jodoe@testmail.com");
    }

    // update the contact email with incorrect email / throw an exception
    @Test
    public void updateEmailIncorrectOldEmailTest() {
        User user = new User();
        userRepository.save(user);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        contactRepository.save(contact);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.updateEmail("jodoe@testmail.com", "jodoe@testmail.com"));

        String expectedMessage = "Contact not found with email : 'jodoe@testmail.com'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact with his id
    @Test
    public void deleteContactTest() {
        User user = new User();
        userRepository.save(user);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        contactRepository.save(contact);

        user.getContactList().add(contact);
        userRepository.save(user);

        contactService.deleteContact(UserPrincipal.create(user), 1L);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.getContact(1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact doesn't exist / throw an exception
    @Test
    public void deleteContactThatDoesntExistTest() {
        User users = new User();
        userRepository.save(users);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactService.deleteContact(UserPrincipal.create(users), 1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // delete a contact that's not created by the current user
    @Test
    public void deleteContactThatIsNotCreatedByTheCurrentUserTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User users = new User("paul", "doe", "paul", "p.b@testmail.com", "ee", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user1 = new User();
        userRepository.save(users);
        userRepository.save(user1);

        Contact contact = new Contact("jdoe@testmail.com", "Joey", user1);
        Contact contact1 = new Contact("jdoe@testmail.com", "Joey", users);
        contactRepository.save(contact);
        contactRepository.save(contact1);

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
        User user = new User();
        userRepository.save(user);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        contactRepository.save(contact);

        Assertions.assertFalse(contactService.getEmailAvailability("jdoe@testmail.com"));
    }
}
