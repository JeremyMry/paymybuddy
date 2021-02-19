package com.paymybuddy.app.controller;

import ch.ethz.ssh2.auth.AuthenticationManager;
import com.paymybuddy.app.DTO.JwtAuthenticationResponse;
import com.paymybuddy.app.DTO.LoginRequest;
import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.RoleName;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthServiceImpl authService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void signinTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("jdoe");
        loginRequest.setPassword("abc");

        when(authService.authenticateUser(loginRequest)).thenReturn(new JwtAuthenticationResponse("eeee"));
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signin")
                                      .contentType(MediaType.APPLICATION_JSON).content("{\"usernameOrEmail\": \"jdoe\",\"password\": \"abc\"}"))
                                      .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.ACCEPTED.value());
    }

    @Test
    public void signinTestError() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"usernameOrEmail\": \"jdoe\",\"password\": \"abc\"}"))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }
}
