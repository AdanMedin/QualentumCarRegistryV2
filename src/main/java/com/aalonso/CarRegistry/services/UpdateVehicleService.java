package com.aalonso.CarRegistry.services;

import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
import com.aalonso.CarRegistry.persistence.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UpdateVehicleService {
    @PostConstruct
    public void init() {
        log.info("UpdateVehicleService is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public Optional<VehicleDTO> updateVehicle(VehicleDTO vehicleDTO) {
        log.info("Accessed update vehicle service...");
        Optional<Vehicle> vehicleToUpdate = vehicleRepository.findById(Integer.valueOf(vehicleDTO.getId()));
        vehicleToUpdate.ifPresentOrElse(
                vehicleFound -> {
                    // Con el método save() de JpaRepository.
                    // Si el vehiculo ya existe en la base de datos, este se actualiza.
                    vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
                    log.info("Vehicle to update: {}", vehicleToUpdate);
                    log.info("Vehicle with id: {} was updated successfully.", vehicleDTO.getId());
                },
                () -> log.info("Vehicle with id: " + vehicleDTO.getId() + " does not exist")
        );
        return vehicleRepository.findById(Integer.valueOf(vehicleDTO.getId())).map(
                vehicle -> modelMapper.map(vehicle, VehicleDTO.class)
        );
    }
}
