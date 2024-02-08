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
public class UpdateVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("UpdateVehicleBrandService is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    BrandRepository brandRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    // Actualiza vehiculos en la base de datos
    public Optional<VehicleDTO> updateVehicle(VehicleDTO vehicleDTO) {

        log.info("Accessed update vehicle service...");
        Optional<Vehicle> vehicleToUpdate = vehicleRepository.findById(vehicleDTO.getId());

        vehicleToUpdate.ifPresentOrElse(
                vehicleFound -> {

                    // Con el método save() de JpaRepository.
                    // Si el vehiculo ya existe en la base de datos, este se actualiza.
                    log.info("Vehicle to update: {}", vehicleFound.getModel());
                    log.info("Updating vehicle: {}", vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class)));
                },

                () -> log.info("Vehicle with id: {} does not exist", vehicleDTO.getId())
        );

        // Si el vehiculo fue actualizado, lo mapeamos a un DTO.
        return vehicleRepository.findById(vehicleDTO.getId()).map(
                vehicle -> modelMapper.map(vehicle, VehicleDTO.class)
        );
    }


    // Actualiza marcas en la base de datos
    public Optional<BrandDTO> updateBrand(BrandDTO brandDTO) {

        log.info("Accessed update brand service...");
        Optional<Brand> brandToUpdate = brandRepository.findById(brandDTO.getId());

        // Si la marca ya existe en la base de datos, esta se actualiza.
        brandToUpdate.ifPresentOrElse(
                brandFound -> {

                    log.info("Brand to update: {}", brandFound.getName());
                    log.info("Updating brand: {}", brandRepository.save(modelMapper.map(brandDTO, Brand.class)));
                },

                () -> log.info("Brand with id: {} does not exist", brandDTO.getId())
        );

        // Si la marca fue actualizada, la mapeamos a un DTO.
                return brandRepository.findById(brandDTO.getId()).map(
                brand -> modelMapper.map(brand, BrandDTO.class)
        );
    }
}
