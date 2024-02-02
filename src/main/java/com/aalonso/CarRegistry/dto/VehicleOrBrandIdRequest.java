package com.aalonso.CarRegistry.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class VehicleOrBrandIdRequest {
    @Id
    private String id;
}
