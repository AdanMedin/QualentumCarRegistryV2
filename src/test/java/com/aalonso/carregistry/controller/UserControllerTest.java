package com.aalonso.carregistry.controller;

import com.aalonso.carregistry.dto.JwtResponse;
import com.aalonso.carregistry.dto.LogInRequest;
import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.services.imp.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void test_singUp() {

        UserDTO userDTO = new UserDTO();
        JwtResponse jwtResponse = new JwtResponse();

        when(authenticationService.signUp(userDTO)).thenReturn(Optional.of(jwtResponse));

        ResponseEntity<Optional<JwtResponse>> response = userController.singUp(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(jwtResponse, response.getBody().get());
    }

    @Test
    void test_singUp_nullRequest() {

        ResponseEntity<Optional<JwtResponse>> response = userController.singUp(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void test_singUp_emptyResponse() {
        UserDTO userDTO = new UserDTO();

        when(authenticationService.signUp(userDTO)).thenReturn(Optional.empty());

        ResponseEntity<Optional<JwtResponse>> response = userController.singUp(userDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_logIn() {

        LogInRequest logInRequest = new LogInRequest();
        JwtResponse jwtResponse = new JwtResponse();

        when(authenticationService.logIn(logInRequest)).thenReturn(Optional.of(jwtResponse));

        ResponseEntity<Optional<JwtResponse>> response = userController.logIn(logInRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(jwtResponse, response.getBody().get());
     }

    @Test
    void test_logIn_nullRequest() {

        ResponseEntity<Optional<JwtResponse>> response = userController.logIn(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void test_logIn_emptyResponse() {
        LogInRequest logInRequest = new LogInRequest();

        when(authenticationService.logIn(logInRequest)).thenReturn(Optional.empty());

        ResponseEntity<Optional<JwtResponse>> response = userController.logIn(logInRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}