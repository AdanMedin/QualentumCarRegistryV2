package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.persistence.entity.User;
import com.aalonso.carregistry.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @InjectMocks
    private UserServiceImp userServiceImp;

    @Mock
    private UserRepository userRepository;

    @Test
    void test_userDetailsService() {

        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetailsService result = userServiceImp.userDetailsService();

        assertEquals(user, result.loadUserByUsername(email));
    }

    @Test
    void test_userDetailsService_failed() {

        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserDetailsService result = userServiceImp.userDetailsService();

        assertThrows(UsernameNotFoundException.class, () -> result.loadUserByUsername(email));
    }

    @Test
    void test_save() {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@Example.com");
        userDTO.setPassword("testPassword");

        User user = new User();
        user.setEmail("test@Example.com");
        user.setPassword("testPassword");

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId("1");
        userDTO1.setEmail("test@Example.com");
        userDTO1.setPassword("testPassword");

        User user1 = new User();
        user1.setId("1");
        user1.setEmail("test@Example.com");
        user1.setPassword("testPassword");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user1);

        Optional<UserDTO> result = userServiceImp.save(userDTO);

        assertTrue(result.isPresent());
        assertEquals(userDTO1, result.get());
    }

    @Test
    void test_save_failed() {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@Example.com");
        userDTO.setPassword("testPassword");

        User user = new User();
        user.setId("1");
        user.setEmail("test@Example.com");
        user.setPassword("testPassword");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userServiceImp.save(userDTO);

        assertFalse(result.isPresent());
    }
}