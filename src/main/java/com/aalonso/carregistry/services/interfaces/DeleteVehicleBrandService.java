package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;

import java.util.Optional;

public interface DeleteVehicleBrandService {
    Optional<VehicleDTO> deleteVehicleById(String id);
    Optional<BrandDTO> deleteBrandById(String id);
}
