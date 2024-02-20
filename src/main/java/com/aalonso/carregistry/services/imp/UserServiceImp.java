package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.persistence.entity.User;
import com.aalonso.carregistry.persistence.repository.UserRepository;
import com.aalonso.carregistry.services.interfaces.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Esta clase implementa la interfaz UserService y proporciona métodos para el registro y el inicio de sesión de usuarios.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    @PostConstruct
    public void init() {
        log.info("UserService is operational...");
    }

    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Este método devuelve una implementación de UserDetailsService.
     * UserDetailsService es una interfaz proporcionada por Spring Security para cargar detalles de usuario por nombre de usuario.
     * En este caso, el nombre de usuario es el correo electrónico del usuario.
     */
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) {

                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    // Este método registra a un usuario en la base de datos.
    @Override
    public Optional<UserDTO> save(UserDTO userDTO) {

        log.info("Accessed user service...");

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            log.error("User already exists");

            return Optional.empty();
        }

        User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));

        if (savedUser.getId() == null) {
            log.info("User colud not be saved");

            return Optional.empty();
        }

        log.info("User saved correctly");

        return Optional.of(modelMapper.map(savedUser, UserDTO.class));
    }
}
