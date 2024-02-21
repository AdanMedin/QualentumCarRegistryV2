package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.Brand;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import com.aalonso.carregistry.persistence.repository.BrandRepository;
import com.aalonso.carregistry.persistence.repository.VehicleRepository;
import com.aalonso.carregistry.services.interfaces.ShowVehicleBrandService;
import com.aalonso.carregistry.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShowVehicleBrandServiceImp implements ShowVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("ShowVehicleBrandServiceImp is operational...");
    }

    private final VehicleRepository vehicleRepository;
    private final BrandRepository brandRepository;
    private final Utils utils;
    private final ModelMapper modelMapper = new ModelMapper();

    // Este método devuelve un Optional que contiene una lista de objetos VehicleDTO.
    @Override
    @Async
    public CompletableFuture<Optional<List<VehicleDTO>>> showAllVehicles() {

        long startTime = System.currentTimeMillis();
        log.info("Accessed show all vehicles service...");

        List<Vehicle> vehicles = vehicleRepository.findAll();

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
            Si la lista de vehículos no está vacía, se devuelve un Optional que contiene la lista transformada de objetos VehicleDTO.
            .collect(Collectors.toList()) convierte el Stream en una lista. */
        return CompletableFuture.completedFuture(
                vehicles.isEmpty() ? Optional.empty() : Optional.of(vehicles.stream()
                        .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                        .toList())
        );
    }

    // Este método devuelve un Optional que contiene un objeto VehicleDTO si el vehículo con el id proporcionado existe en la base de datos.
    @Override
    public Optional<VehicleDTO> showVehicleById(String id) {

        log.info("Accessed show vehicle by id service...");
        Optional<Vehicle> vehicleToShow = vehicleRepository.findById(id);

        // Si el Optional contiene un valor, ejecutamos el primer lambda, si no, ejecutamos el segundo lambda.
        vehicleToShow.ifPresentOrElse(
                vehicleFound -> {

                    log.info("Vehicle found: {}", vehicleFound);
                    log.info("With brand: {}", vehicleFound.getBrand());

                },
                () -> log.info("Vehicle with id: {} does not exist on database", id)
        );

        // Si el Optional contiene un valor "vehicleToShow.map()", lo mapeamos a un DTO "modelMapper.map()"
        return vehicleToShow.map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class));
    }

    // Este método devuelve un Optional que contiene una lista de objetos BrandDTO.
    @Override
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
            Si la lista de vehículos no está vacía, se devuelve un Optional que contiene la lista transformada de objetos BrandDTO.
            .collect(Collectors.toList()) convierte el Stream en una lista. */
        return CompletableFuture.completedFuture(
                brandsToShow.isEmpty() ? Optional.empty() : Optional.of(brandsToShow.stream()
                        .map(brand -> modelMapper.map(brand, BrandDTO.class))
                        .toList())
        );
    }

    // Este método devuelve un Optional que contiene un objeto BrandDTO si la marca con el id proporcionado existe en la base de datos.
    @Override
    public Optional<BrandDTO> showBrandById(String id) {

        log.info("Accessed show brand by id service...");
        Optional<Brand> brandToShow = brandRepository.findById(id);

        // Si el Optional contiene un valor, ejecutamos el primer lambda, si no, ejecutamos el segundo lambda.
        brandToShow.ifPresentOrElse(
                brandFound ->
                        log.info("Brand found: {}", brandFound),
                () -> log.info("Brand with id: {} does not exist on database", id)
        );

        // Si el Optional contiene un valor "brandToShow.map()", lo mapeamos a un DTO "modelMapper.map()"
        return brandToShow.map(brand -> modelMapper.map(brand, BrandDTO.class));
    }
}
