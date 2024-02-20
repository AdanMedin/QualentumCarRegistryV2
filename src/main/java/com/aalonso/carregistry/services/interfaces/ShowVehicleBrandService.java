package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ShowVehicleBrandService {
    CompletableFuture<Optional<List<VehicleDTO>>> showAllVehicles();
    Optional<VehicleDTO> showVehicleById(String id);
    CompletableFuture<Optional<List<BrandDTO>>> showAllBrands();
    Optional<BrandDTO> showBrandById(String id);
}
