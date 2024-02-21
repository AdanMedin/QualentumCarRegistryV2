package com.aalonso.carregistry.controller;

import com.aalonso.carregistry.dto.JwtResponse;
import com.aalonso.carregistry.dto.LogInRequest;
import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.services.imp.AuthenticationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @PostConstruct
    public void init() {
        log.info("UserController is operational...");
    }

    private final AuthenticationService authenticationService;

    // Endpoint registro usuario.
    @PostMapping(value = "/sign_up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<JwtResponse>> singUp(@RequestBody UserDTO userDTO) {

        log.info("Accessed car registry controller...");

        if (userDTO == null) {
            log.error("No user was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<JwtResponse> jwtResponse = authenticationService.signUp(userDTO);

        if (jwtResponse.isEmpty()) {
            log.error("User not registered");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    //Endpoint inicio de sesión.
    @PostMapping(value = "/log_in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<JwtResponse>> logIn(@RequestBody LogInRequest logInRequest) {

        log.info("Accessed car registry controller...");

        if (logInRequest == null) {
            log.error("User or pass wrong or not provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<JwtResponse> jwtResponse = authenticationService.logIn(logInRequest);

        if (jwtResponse.isEmpty()) {
            log.info("User not found. Email or password wrong.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
