package com.aalonso.carregistry.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    @Spy
    private JwtService jwtServiceSpy;
    @Mock
    private UserDetails userDetails;
    @Mock
    private JwtService jwtServiceMock;

    @Test
    void test_extractEmail() {

        String token = "testToken";
        String email = "test@Example.com";

        doReturn(email).when(jwtServiceSpy).extractClaim(eq(token), any());

        String result = jwtServiceSpy.extractEmail(token);

        assertEquals(email, result);
    }

    @Test
    void test_extractEmail_failed() {

        String token = "testToken";

        doThrow(new IllegalArgumentException()).when(jwtServiceSpy).extractClaim(eq(token), any());

        assertThrows(IllegalArgumentException.class, () -> jwtServiceSpy.extractEmail(token));
    }

    @Test
    void test_generateToken() {

        UserDetails userDetails = new User(
                "testUser",
                "testPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEST"))
        );

        String jwtSecretKeyTest = Base64.getEncoder().encodeToString("jwtSecretKeyTestjwtSecretKeyTestjwtSecretKeyTest".getBytes());

        // Establecer un valor para jwtExpirationMs
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 3600000L);
        // Establecer un valor para jwtSecretKey
        ReflectionTestUtils.setField(jwtService, "jwtSecretKey", jwtSecretKeyTest);


        String result = jwtService.generateToken(userDetails);

        log.info("Token: {}", result);

        assertFalse(result.isEmpty());
    }

    @Test
    void test_generateToken_failed() {

        UserDetails userDetails = new User(
                "testUser",
                "testPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEST"))
        );

        String jwtSecretKeyTest = Base64.getEncoder().encodeToString("jwtSecretKeyTestjwtSecretKeyTestjwtSecretKeyTest".getBytes());

        // Establece un valor para jwtExpirationMs
        ReflectionTestUtils.setField(jwtServiceSpy, "jwtExpirationMs", 3600000L);
        // Establece un valor para jwtSecretKey
        ReflectionTestUtils.setField(jwtServiceSpy, "jwtSecretKey", jwtSecretKeyTest);

        doReturn(null).when(jwtServiceSpy).generateToken(any(), any());

        String result = jwtServiceSpy.generateToken(userDetails);

        assertNull(result);
    }

    @Test
    void test_isTokenValid() {

        String token = "testToken";
        String email = "test@Example";

        doReturn(email).when(jwtServiceSpy).extractEmail(token);
        when(userDetails.getUsername()).thenReturn(email);
        doReturn(false).when(jwtServiceSpy).isTokenExpired(token);

        boolean result = jwtServiceSpy.isTokenValid(token, userDetails);
        assertTrue(result);
    }

    @Test
    void test_isTokenValid_failed() {

        // User details sin username

        String token = "testToken";
        String email = "test@Example";

        doReturn(email).when(jwtServiceSpy).extractEmail(token);
        when(userDetails.getUsername()).thenReturn(null);

        boolean result = jwtServiceSpy.isTokenValid(token, userDetails);
        assertFalse(result);
    }

    @Test
    void test_isTokenValid_tokenExpired() {

        String token = "testToken";
        String email = "test@Example";

        doReturn(email).when(jwtServiceSpy).extractEmail(token);
        when(userDetails.getUsername()).thenReturn(email);
        doReturn(true).when(jwtServiceSpy).isTokenExpired(token);

        boolean result = jwtServiceSpy.isTokenValid(token, userDetails);
        assertFalse(result);
    }

    @Test
    void test_isTokenValid_invalidToken() {

        String token = "testToken";

        doReturn(null).when(jwtServiceSpy).extractEmail(token);

        assertThrows(NullPointerException.class, () -> jwtServiceSpy.isTokenValid(token, userDetails));
    }

}