package com.aalonso.CarRegistry.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private int mileage;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private int year;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String colour;
    @Column(nullable = false)
    private String fuelType;
    @Column(nullable = false)
    private Integer numDoors;


    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
