package com.aalonso.CarRegistry.services;

import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
import com.aalonso.CarRegistry.persistence.repository.VehicleRepository;
import com.aalonso.CarRegistry.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShowVehicleService {
    @PostConstruct
    public void init() {
        log.info("ShowVehicleService is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private Utils utils;

    private final ModelMapper modelMapper = new ModelMapper();

    public Optional<List<VehicleDTO>> showAllVehicles() {
        log.info("Accessed show all vehicles service...");

        List<Vehicle> vehicles;
        vehicles = vehicleRepository.findAll();

        // Aquí no utilizo un ifPresentOrElse porque findAll() siempre devuelve una lista vacía cuando no encuentra registros.
        utils.ifEmptyOrElse(
                vehicles,
                () -> log.error("The database is empty"),
                list -> {
                    log.info("Showing all vehicles...");
                    for (Vehicle vehicle : list) {
                        log.info("Vehicle found: {}", vehicle);
                        log.info("Brand: {}", vehicle.getBrand());
                    }
                }
        );
        return vehicles.isEmpty() ? Optional.empty() : Optional.of(vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList()));
    }

    public Optional<VehicleDTO> showVehicleById(String id) {
        log.info("Accessed show vehicle by id service...");
        Optional<Vehicle> vehicleToShow = vehicleRepository.findById(id);
        vehicleToShow.ifPresentOrElse(
                vehicleFound -> {
                    log.info("Vehicle found: {}", vehicleFound);
                    log.info("With brand: {}", vehicleFound.getBrand());
                },
                () -> log.info("Vehicle with id: {} does not exist on database", id)
        );
        return vehicleToShow.map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class));
    }
}
