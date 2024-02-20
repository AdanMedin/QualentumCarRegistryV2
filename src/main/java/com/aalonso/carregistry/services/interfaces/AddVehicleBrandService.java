package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AddVehicleBrandService {
    CompletableFuture<Optional<List<VehicleDTO>>> addVehicles(List<VehicleDTO> vehicleDtoList);
    CompletableFuture<Optional<List<BrandDTO>>> addBrands(List<BrandDTO> brandDtoList);

}
