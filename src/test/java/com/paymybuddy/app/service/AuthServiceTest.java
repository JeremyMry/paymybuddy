package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Role;
import com.paymybuddy.app.entity.RoleName;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.exception.AppException;
import com.paymybuddy.app.model.ApiResponse;
import com.paymybuddy.app.model.LoginRequest;
import com.paymybuddy.app.model.SignUpRequest;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.JwtTokenProvider;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Test
    public void authenticateUserTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        Role role1 = new Role();
        role1.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(role);
        roleRepository.save(role1);

        authService.registerUser(signUpRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("johnny");
        loginRequest.setPassword("pwd");

        Assertions.assertFalse(authService.authenticateUser(loginRequest).getAccessToken().isEmpty());
    }

    @Test
    public void authenticateUserIncorrectCredentialsTest() {
        LoginRequest loginRequest = new LoginRequest();

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            authService.authenticateUser(loginRequest);
        });
    }

    @Test
    public void registerUser() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        Role role1 = new Role();
        role1.setName(RoleName.ROLE_ADMIN);

        roleRepository.save(role);
        roleRepository.save(role1);

        ApiResponse apiResponse = authService.registerUser(signUpRequest);

        Assertions.assertEquals(apiResponse.getMessage(), "User registered successfully");
        Assertions.assertTrue(apiResponse.getSuccess());
    }

    @Test
    public void registerUserWithNoRoleSet() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Assertions.assertThrows(AppException.class, () ->
            authService.registerUser(signUpRequest)
        );
    }

    @Test
    public void registerUserWithAnEmailThatAlreadyExist() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Users user = new Users("paul", "doe", "paulo", "johndoe@testmail.com", "450");
        userRepository.save(user);

        ApiResponse apiResponse = authService.registerUser(signUpRequest);

        Assertions.assertFalse(apiResponse.getSuccess());
        Assertions.assertEquals(apiResponse.getMessage(), "Email Address already in use!");
    }

    @Test
    public void registerUserWithAnUsernameThatAlreadyExist() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Users user = new Users("paul", "doe", "johnny", "pdoe@testmail.com", "450");
        userRepository.save(user);

        ApiResponse apiResponse = authService.registerUser(signUpRequest);

        Assertions.assertFalse(apiResponse.getSuccess());
        Assertions.assertEquals(apiResponse.getMessage(), "Username is already taken!");
    }
}
