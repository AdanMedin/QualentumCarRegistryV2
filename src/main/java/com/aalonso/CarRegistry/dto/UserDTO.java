package com.aalonso.CarRegistry.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String mail;
    private String password;
    private String role;
}
