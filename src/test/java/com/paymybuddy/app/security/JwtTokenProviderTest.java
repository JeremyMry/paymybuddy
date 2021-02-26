package com.paymybuddy.app.security;

import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Mock
    JwtTokenProvider jwtTokenProviders;

    // test the generateToken method / must return a String containing the jwt / as the jwt is "random" we just test that the method is not null
    @Test
    public void generateTokenTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");

        authService.registerUser(signUpRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));

        Assertions.assertNotNull(jwtTokenProvider.generateToken(authentication));
    }

    // test the getUserIdFromJwt method / The method must return a long corresponding to the user id
    @Test
    public void getUserIdFromJwtTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");

        authService.registerUser(signUpRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));

        Assertions.assertEquals(jwtTokenProvider.getUserIdFromJWT(jwtTokenProvider.generateToken(authentication)).longValue(), 1L);
    }

    //test the validate token method / must return true
    @Test
    public void validateTokenTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");
        authService.registerUser(signUpRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);

        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    //test the validate token method with invalid Jwt / must return false
    @Test
    public void validateTokenInvalidJwtTokenTest() {
        assertFalse(jwtTokenProvider.validateToken("eeee"));
    }

    // test the validate token method with expired token / must return false
    @Test
    public void validateTokenExpiredTokenTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");
        authService.registerUser(signUpRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");

        when(jwtTokenProviders.generateToken(Mockito.any())).thenReturn(Jwts.builder().setSubject(String.valueOf(1L)).setIssuedAt(new Date()).setExpiration(new Date()).signWith(SignatureAlgorithm.HS512, "bonobozer").compact());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));
        String token = jwtTokenProviders.generateToken(authentication);

        assertFalse(jwtTokenProvider.validateToken(token));
    }

    // test the validate token method with invalid secret key / must return false
    @Test
    public void validateTokenInvalidSignatureTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");
        authService.registerUser(signUpRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");

        when(jwtTokenProviders.generateToken(Mockito.any())).thenReturn(Jwts.builder().setSubject(String.valueOf(1L)).setIssuedAt(new Date()).setExpiration(new Date()).signWith(SignatureAlgorithm.HS512, "onobozer").compact());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));
        String token = jwtTokenProviders.generateToken(authentication);

        assertFalse(jwtTokenProvider.validateToken(token));
    }

    // test the validate token method with null token / must return false
    @Test
    public void validateTokenIllegalArgumentExceptionTokenTest() {
        assertFalse(jwtTokenProvider.validateToken(null));
    }
}
