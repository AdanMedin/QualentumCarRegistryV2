package com.aalonso.CarRegistry.services.interfaces;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;

import java.util.Optional;

public interface UpdateVehicleBrandService {
    Optional<VehicleDTO> updateVehicle(VehicleDTO vehicleDTO);
    Optional<BrandDTO> updateBrand(BrandDTO brandDTO);

}
