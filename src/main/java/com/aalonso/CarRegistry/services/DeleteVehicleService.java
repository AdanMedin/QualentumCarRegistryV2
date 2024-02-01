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
public class DeleteVehicleService {
    @PostConstruct
    public void init() {
        log.info("DeleteVehicleService is operational...");
    }
    @Autowired
    private VehicleRepository vehicleRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public Optional<VehicleDTO> deleteVehicleById(String id) {
        log.info("Accessed delete vehicle service...");
        Optional<Vehicle> vehicleToDelete = vehicleRepository.findById(Integer.valueOf(id));
        vehicleToDelete.ifPresentOrElse(
                vehicle -> {
                    vehicleRepository.deleteById(Integer.valueOf(id));
                    log.info("This vehicle was deleted successfully: {}", vehicle);
                },
                () -> log.info("Vehicle with id: " + id + " does not exist")
        );
        return vehicleToDelete.map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)); //Si el Optional contiene un valor "vehicleToDelete.map()", lo mapeamos a un DTO "modelMapper.map()"
    }
}
