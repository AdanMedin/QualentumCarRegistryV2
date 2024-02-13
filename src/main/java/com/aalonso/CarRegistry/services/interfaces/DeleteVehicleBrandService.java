package com.aalonso.CarRegistry.services.interfaces;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;

import java.util.Optional;

public interface DeleteVehicleBrandService {
    Optional<VehicleDTO> deleteVehicleById(String id);
    Optional<BrandDTO> deleteBrandById(String id);
}
