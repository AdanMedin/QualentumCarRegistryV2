package com.aalonso.CarRegistry.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    int id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false, unique = true)
    String mail;
    @Column(nullable = false)
    String password;
    @Column(nullable = false, columnDefinition = "default 'ROLE_CLIENT'")
    String role;
}
