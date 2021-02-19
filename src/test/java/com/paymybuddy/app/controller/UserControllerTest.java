package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.service.impl.ContactServiceImpl;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    ContactServiceImpl contactService;

    @MockBean
    UserController userController;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void findUserTest() throws Exception {

        Users user = new Users("john", "doe", "jdoe", "jdoe@testmail.com", "abc", BigDecimal.ZERO);
        userRepository.save(user);

        when(userRepository.findByEmail("jdoe@testmail.com")).thenReturn(Optional.of(user));
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/findUser")
                        .param("email", "jdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(response.getContentAsString(), "{\"username\":\"jdoe\",\"email\":\"jdoe@testmail.com\"}");
    }

    @Test
    public void findUserThatDoesntExistTest() throws Exception {

        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/findUser")
                        .param("email", "jdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getCurrentUserTest() throws Exception {
        Users user = new Users("john", "doe", "jdoe", "jdoe@testmail.com", "abc", null);
        userRepository.save(user);
        UserSummary userSummary = new UserSummary("jdoe", "john", "doe", "jdoe@testmail.com", null);

        when(userService.getCurrentUser(Mockito.any())).thenReturn(userSummary);

        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/me")
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(response.getContentAsString(), "{\"username\":\"jdoe\",\"firstName\":\"john\",\"lastName\":\"doe\",\"email\":\"jdoe@testmail.com\",\"wallet\":null}");
    }

    @Test
    public void getCurrentUserUnauthorizedTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(response.getContentAsString(), "");
    }

    @Test
    public void updateEmailTest() throws Exception {
        when(userService.getEmailAvailability(Mockito.any())).thenReturn(true);

        MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/user/email")
                                .content("jodoe@testmail.com")
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    /**

    @Test
    public void updatePassword() {
        Users user = new Users("john", "doe", "jdoe", "jdoe@testmail.com", "abc", null);
        userRepository.save(user);

        userController.updatePassword(UserPrincipal.create(user), "bcd");

        Assertions.assertNotEquals(userRepository.findByEmail("jdoe@testmail.com").get().getPassword(), user.getPassword());
    }**/
}
