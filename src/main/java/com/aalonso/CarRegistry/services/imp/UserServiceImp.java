package com.aalonso.CarRegistry.services.imp;

import com.aalonso.CarRegistry.dto.LogInRequest;
import com.aalonso.CarRegistry.dto.UserDTO;
import com.aalonso.CarRegistry.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Override
    public Optional<UserDTO> singUp(UserDTO userDTO) {
        log.info("Accessed sign up service...");
        return Optional.of(userDTO);
    }

    @Override
    public Optional<UserDTO> singIn(LogInRequest logInRequest) {
        log.info("Accessed sign in service...");
        return Optional.of(new UserDTO());
    }
}
