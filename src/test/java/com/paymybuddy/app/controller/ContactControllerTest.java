package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.ContactSummary;
import com.paymybuddy.app.DTO.ContactUpdate;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.ContactServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
public class ContactControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactController contactController;

    @Autowired
    ContactServiceImpl contactService;

    @Autowired
    MockMvc mockMvc;

    // test the create contact controller / must return an HttpStatus.CREATED
    @Test
    public void createContactTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Users user2 = new Users("john", "doe", "johnny", "johndoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user2);
        ContactSummary contactSummary = new ContactSummary("johnny", "johndoe@testmail.com");

        Assertions.assertEquals(contactController.createContact(UserPrincipal.create(user), contactSummary), new ResponseEntity<>(HttpStatus.CREATED));
    }

    // test the create contact controller  with unknown email / must return an HttpStatus.BAD_REQUEST
    @Test
    public void createContactUnknownEmail() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        ContactSummary contactSummary = new ContactSummary("johnny", "johndoe@testmail.com");

        Assertions.assertEquals(contactController.createContact(UserPrincipal.create(user), contactSummary), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // test the create contact controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void createContactNoCurrentUser() throws Exception {

        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/contact/create")
                        .content("{\"firstName\":\"joe\",\"email\":\"jdoe@testmail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the update contact controller / must update the contact and return an HttpStatus.OK
    @Test
    public void updateContactTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Contact contact = new Contact("jdoe@testmail.com", "johnny", 1L);
        contactRepository.save(contact);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "john");

        ResponseEntity<HttpStatus> responseEntity = contactController.updateContactFirstName(UserPrincipal.create(user), contactUpdate);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(contactService.getContact(1L).getFirstName(), "john");
    }

    // test the update contact controller with an unknown / throw an exception
    @Test
    public void updateUnknownContactTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        ContactUpdate contactUpdate = new ContactUpdate(1L, "john");

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactController.updateContactFirstName(UserPrincipal.create(user), contactUpdate));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // test the update contact controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void updateContactNoCurrentUserTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/contact/put")
                        .content("{\"id\": 1L, \"newFirstName\":\"johnny\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the delete contact controller / must delete the contact and return an HttpStatus.OK
    @Test
    public void deleteContactTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Contact contact = new Contact("jdoe@testmail.com", "johnny", 1L);
        contactRepository.save(contact);

        Assertions.assertEquals(contactController.deleteContact(UserPrincipal.create(user), 1L), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertTrue(contactService.getEmailAvailability("jdoe@testmail.com"));
    }

    // test the delete contact controller when there is no contact to delete / must throw an exception
    @Test
    public void deleteContactWhenThereIsNoContactToDeleteTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> contactController.deleteContact(UserPrincipal.create(user), 1L));

        String expectedMessage = "Contact not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // test the delete contact controller when there is no current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void deleteContactWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/contact/delete")
                        .content("{\"id\": 1L}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the get all contacts controller / must return a List of contact and an HttpStatus.OK
    @Test
    public void getAllContactTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Contact contact = new Contact("jdoe@testmail.com", "johnny", 1L);
        contactRepository.save(contact);
        Contact contact1 = new Contact("pdoe@testmail.com", "paul", 1L);
        contactRepository.save(contact1);

        Assertions.assertEquals(contactController.getAllContacts(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(contactController.getAllContacts(UserPrincipal.create(user)).getBody()).size(), 2);
    }

    // test the get all contacts controller when there is no contact / must return an empty List of contact and an HttpStatus.OK
    @Test
    public void getAllContactWhenThereIsNoneTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        Assertions.assertEquals(contactController.getAllContacts(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(Objects.requireNonNull(contactController.getAllContacts(UserPrincipal.create(user)).getBody()).isEmpty());
    }

    // test the get all contacts controller when there is no current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void getAllContactWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/contact/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }
}
