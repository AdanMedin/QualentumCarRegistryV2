package com.aalonso.CarRegistry.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class VehicleIdRequest {
    @Id
    private String id;
}
