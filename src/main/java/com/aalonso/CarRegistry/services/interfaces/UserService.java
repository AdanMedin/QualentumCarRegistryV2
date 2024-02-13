package com.aalonso.CarRegistry.services.interfaces;

import com.aalonso.CarRegistry.dto.LogInRequest;
import com.aalonso.CarRegistry.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> singUp(UserDTO userDTO);
    Optional<UserDTO> singIn(LogInRequest logInRequest);
}
