package com.aalonso.CarRegistry.services;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Brand;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
import com.aalonso.CarRegistry.persistence.repository.BrandRepository;
import com.aalonso.CarRegistry.persistence.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
public class DeleteVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("DeleteVehicleBrandService is operational...");
    }
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BrandRepository brandRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    // Elimina vehiculos de la base de datos
    public Optional<VehicleDTO> deleteVehicleById(String id) {
        log.info("Accessed delete vehicle service...");
        Optional<Vehicle> vehicleToDelete = vehicleRepository.findById(id);
        vehicleToDelete.ifPresentOrElse(
                vehicle -> {
                    vehicleRepository.deleteById(id);
                    log.info("This vehicle was deleted successfully: {}", vehicle);
                },
                () -> log.info("Vehicle with id: " + id + " does not exist")
        );
        return vehicleToDelete.map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)); //Si el Optional contiene un valor "vehicleToDelete.map()", lo mapeamos a un DTO "modelMapper.map()"
    }

    // Elimina marcas de la base de datos
    public Optional<BrandDTO> deleteBrandById(String id) {
        log.info("Accessed delete brand service...");
        Optional<Brand> brandToDelete = brandRepository.findById(id);
        brandToDelete.ifPresentOrElse(
                brand -> {
                    brandRepository.deleteById(id);
                    log.info("Brand {} was deleted successfully", brand.getName());
                },
                () -> log.info("Brand with id: " + id + " does not exist")
        );
        return brandToDelete.map(brand -> modelMapper.map(brand, BrandDTO.class));
    }
}
