package com.paymybuddy.app.service;

import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class ContactServiceTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactService contactService;

    /**@Test
    public void getContactTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);

        userRepository.save(user);
        contactRepository.save(contact);

        Assert.assertTrue(contactService.getContact(1).isPresent());
    }

    @Test
    public void getContact2Test() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", user);

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        Assert.assertTrue(contactService.getContact(2).isPresent());
    }

    @Test
    public void getContactThatDoesntExistTest() {
        Assert.assertFalse(contactService.getContact(3).isPresent());
    }

    @Test
    public void getAllContactsTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact("jdoe@testmail.com", "Joey", user);
        Contact contact2 = new Contact("pdoe@testmail.com", "Paul", user);

        userRepository.save(user);
        contactRepository.save(contact);
        contactRepository.save(contact2);

        Assert.assertEquals(contactService.getAllContacts(1).size(), 2);
    }

    @Test
    public void getAllContactsWhenThereIsNoneTest() { ;
        Assert.assertEquals(contactService.getAllContacts(1).size(), 0);
    }

    @Test
    public void createContactTest() {
        User user = new User("joe", "doe", "jdoe@testmail.com", "eee", 450);
        userRepository.save(user);

        Boolean bool = contactService.createContact(new Contact("jdoe@testmail.com", "Joey", user));

        Assert.assertEquals(bool, true);
    }

    @Test
    public void createContactIncorrectMailTest() {
        User user = new User("joe", "doe", "jdoe@testmail.com", "eee", 450);
        userRepository.save(user);

        Boolean bool = contactService.createContact(new Contact("pdoe@testmail.com", "Joey", user));

        Assert.assertEquals(bool, false);
    }

    @Test
    public void updateContactTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact(1, "jdoe@testmail.com", "John", user);
        userRepository.save(user);
        contactRepository.save(contact);

        Boolean bool = contactService.updateContact(new Contact("jdoe@testmail.com", "Joey", user), 1);

        Assert.assertEquals(bool, true);
    }

    @Test
    public void updateContactErrorTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact(1, "jdoe@testmail.com", "John", user);
        userRepository.save(user);
        contactRepository.save(contact);

        Boolean bool = contactService.updateContact(new Contact("jdoe@testmail.com", "Joey", user), 2);

        Assert.assertEquals(bool, false);
    }

    @Test
    public void deleteContactTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        Contact contact = new Contact(1, "jdoe@testmail.com", "John", user);
        userRepository.save(user);
        contactRepository.save(contact);

        contactService.deleteContact(1);

        Assert.assertFalse(contactService.getContact(1).isPresent());
    }**/
}
