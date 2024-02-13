package com.aalonso.CarRegistry.services.interfaces;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ShowVehicleBrandService {
    CompletableFuture<Optional<List<VehicleDTO>>> showAllVehicles();
    Optional<VehicleDTO> showVehicleById(String id);
    CompletableFuture<Optional<List<BrandDTO>>> showAllBrands();
    Optional<BrandDTO> showBrandById(String id);
}
