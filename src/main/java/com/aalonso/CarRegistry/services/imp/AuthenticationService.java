package com.aalonso.CarRegistry.services.imp;

import com.aalonso.CarRegistry.dto.JwtResponse;
import com.aalonso.CarRegistry.dto.LogInRequest;
import com.aalonso.CarRegistry.dto.UserDTO;
import com.aalonso.CarRegistry.persistence.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @PostConstruct
    public void init() {
        log.info("AuthenticationService is operational...");
    }

    private final UserRepository userRepository;
    private final UserServiceImp userServiceImp;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Optional<JwtResponse> signUp(UserDTO userDTO) {

        log.info("Accessed authentication service...");

        UserDTO userdto = UserDTO.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role("ROLE_CLIENT")
                .build();

        Optional<UserDTO> savedUserDTO = userServiceImp.save(userdto);

        if (savedUserDTO.isPresent()) {

            UserDetails userDetails = userServiceImp.userDetailsService().loadUserByUsername(savedUserDTO.get().getEmail());
            String jwtToken = jwtService.generateToken(userDetails);

            log.info("User saved correctly");

            return Optional.of(JwtResponse.builder().token(jwtToken).build());
        }

        log.error("User cold not be saved");

        return Optional.empty();
    }

    public Optional<JwtResponse> logIn(LogInRequest logInRequest) {

        log.info("Accessed authentication service...");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                logInRequest.getEmail(), logInRequest.getPassword()));

        Optional<UserDetails> userDetails = userRepository.findByEmail(logInRequest.getEmail());

        if (userDetails.isPresent()) {
            String jwtToken = jwtService.generateToken(userDetails.get());

            log.info("User authenticated correctly");

            return Optional.of(JwtResponse.builder().token(jwtToken).build());
        }

        log.error("User cold not be authenticated");

        return Optional.empty();
    }
}
