package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.JwtResponse;
import com.aalonso.carregistry.dto.LogInRequest;
import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImp userServiceImp;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserDetails userDetails;

    @Test
    void test_signUp() {

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@example.com");

        when(userServiceImp.save(userDTO)).thenReturn(Optional.of(userDTO));
        when(userServiceImp.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(userDTO.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("testToken");

        Optional<JwtResponse> response = authenticationService.signUp(userDTO);

        assertTrue(response.isPresent());
        assertEquals("testToken", response.get().getToken());
    }

    @Test
    void test_signUp_fail() {

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@example.com");

        when(userServiceImp.save(userDTO)).thenReturn(Optional.empty());

        Optional<JwtResponse> response = authenticationService.signUp(userDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void test_logIn() {

        LogInRequest logInRequest = new LogInRequest();
        logInRequest.setPassword("testPassword");
        logInRequest.setEmail("test@example.com");

        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(Optional.of(userDetails));
        when(jwtService.generateToken(userDetails)).thenReturn("testToken");

        Optional<JwtResponse> response = authenticationService.logIn(logInRequest);

        assertTrue(response.isPresent());
        assertEquals("testToken", response.get().getToken());
    }

    @Test
    void test_logIn_fail() {

        LogInRequest logInRequest = new LogInRequest();
        logInRequest.setPassword("testPassword");
        logInRequest.setEmail("test@example.com");

        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(Optional.empty());

        Optional<JwtResponse> response = authenticationService.logIn(logInRequest);

        assertFalse(response.isPresent());
    }
}