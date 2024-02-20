package com.aalonso.carregistry.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class VehicleDTO implements Serializable {
    private String id;
    private BrandDTO brand;
    private String model;
    private int mileage;
    private double price;
    private int year;
    private String description;
    private String colour;
    private String fuelType;
    private Integer numDoors;
}
