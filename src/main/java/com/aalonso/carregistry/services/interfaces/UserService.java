package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> save(UserDTO userDTO);
}
