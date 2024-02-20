package com.aalonso.carregistry.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int warranty;
    @Column(nullable = false)
    private String country;

    @ToString.Exclude
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
}
