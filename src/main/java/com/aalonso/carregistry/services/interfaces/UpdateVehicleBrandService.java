package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;

import java.util.Optional;

public interface UpdateVehicleBrandService {
    Optional<VehicleDTO> updateVehicle(VehicleDTO vehicleDTO);
    Optional<BrandDTO> updateBrand(BrandDTO brandDTO);

}
