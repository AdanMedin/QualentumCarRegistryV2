package com.aalonso.CarRegistry.services;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Brand;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
import com.aalonso.CarRegistry.persistence.repository.BrandRepository;
import com.aalonso.CarRegistry.persistence.repository.VehicleRepository;
import com.aalonso.CarRegistry.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShowVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("ShowVehicleBrandService is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private Utils utils;

    private final ModelMapper modelMapper = new ModelMapper();

    @Async
    public CompletableFuture<Optional<List<VehicleDTO>>> showAllVehicles() {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        log.info("Execution time: {} ms", (endTime - startTime));

        /* CompletableFuture devuelve una variable de tipo Future que se completa con el fin de la ejecución.
            Si la lista de vehículos está vacía se devuelve un Optional.empty().
            Si la lista de vehículos no está vacía, se devuelve un Optional que contiene la lista transformada de objetos VehicleDTO. */
        return CompletableFuture.completedFuture(
                vehicles.isEmpty() ? Optional.empty() : Optional.of(vehicles.stream()
                        .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                        .collect(Collectors.toList()))
        );
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

    @Async
    public CompletableFuture<Optional<List<BrandDTO>>> showAllBrands() {
        log.info("Accessed show all brand service...");
        long startTime = System.currentTimeMillis();

        List<Brand> brandsToShow;
        brandsToShow = brandRepository.findAll();

        // Aquí no utilizo un ifPresentOrElse porque findAll() siempre devuelve una lista vacía cuando no encuentra registros.
        utils.ifEmptyOrElse(
                brandsToShow,
                () -> log.error("The database is empty"),
                list -> {
                    log.info("Showing all brands...");
                    for (Brand brand : list) {
                        log.info("Brand found: {}", brand);
                    }
                }
        );

        long endTime = System.currentTimeMillis();
        log.info("Execution time: {} ms", (endTime - startTime));

        /* CompletableFuture devuelve una variable de tipo Future que se completa con el fin de la ejecución.
            Si la lista de vehículos está vacía se devuelve un Optional.empty().
            Si la lista de vehículos no está vacía, se devuelve un Optional que contiene la lista transformada de objetos BrandDTO. */
        return CompletableFuture.completedFuture(
                brandsToShow.isEmpty() ? Optional.empty() : Optional.of(brandsToShow.stream()
                        .map(brand -> modelMapper.map(brand, BrandDTO.class))
                        .collect(Collectors.toList()))
        );
    }

    public Optional<BrandDTO> showBrandById(String id) {
        log.info("Accessed show brand by id service...");
        Optional<Brand> brandToShow = brandRepository.findById(id);
        brandToShow.ifPresentOrElse(
                brandFound ->
                        log.info("Brand found: {}", brandFound),
                () -> log.info("Brand with id: {} does not exist on database", id)
        );
        return brandToShow.map(brand -> modelMapper.map(brand, BrandDTO.class));
    }
}
