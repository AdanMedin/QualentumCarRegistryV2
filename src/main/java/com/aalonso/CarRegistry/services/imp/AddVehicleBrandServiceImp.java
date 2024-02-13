package com.aalonso.CarRegistry.services.imp;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Brand;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
import com.aalonso.CarRegistry.persistence.repository.BrandRepository;
import com.aalonso.CarRegistry.persistence.repository.VehicleRepository;
import com.aalonso.CarRegistry.services.interfaces.AddVehicleBrandService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class AddVehicleBrandServiceImp implements AddVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("AddVehicleBrandServiceImp is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BrandRepository brandRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    //Agregar vehiculos a la base de datos
    @Override
    @Transactional
    @Async
    public CompletableFuture<Optional<List<VehicleDTO>>> addVehicles(List<VehicleDTO> vehicleDtoList) {

        log.info("Accessed add vehicle service...");
        long startTime = System.currentTimeMillis();

        List<VehicleDTO> vehiclesDtoAdded = new ArrayList<>();

        vehicleDtoList.forEach(vehicleDTO -> {

            if (vehicleDTO.getBrand() != null) {

                //Comprobamos que la marca exista en la base de datos y la añadimos si no existe.
                brandChecker(vehicleDTO.getBrand()).ifPresentOrElse(brandDTO -> {

                    //Añadimos la marca al vehiculo.
                    vehicleDTO.setBrand(brandDTO);

                    //Guardamos el vehiculo en la base de datos.
                    Vehicle savedVehicle = vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));

                    if (savedVehicle.getId() != null) {

                        log.info("Vehicle was added correctly: {}", savedVehicle);
                        vehiclesDtoAdded.add(modelMapper.map(savedVehicle, VehicleDTO.class));

                    } else {
                        log.error("Failed to save vehicle");

                    }
                }, () -> log.error("Brand does not exist in the database and cant be added"));
            }
        });

        long endTime = System.currentTimeMillis();
        log.info("Execution time: {} ms", (endTime - startTime));

        /* CompletableFuture devuelve una variable de tipo Future que se completa con el fin de la ejecución.
            Si la lista de vehiculos guardados está vacía se devuelve un Optional.empty().
            Si la lista de vehiculos guardados no está vacía, se devuelve un Optional que contiene la lista objetos VehicleDTO. */
        return CompletableFuture.completedFuture(vehiclesDtoAdded.isEmpty() ? Optional.empty() : Optional.of(vehiclesDtoAdded));
    }

    //Agregar marcas a la base de datos
    @Override
    @Transactional
    @Async
    public CompletableFuture<Optional<List<BrandDTO>>> addBrands(List<BrandDTO> brandDtoList) {

        log.info("Accessed add vehicle service...");
        long startTime = System.currentTimeMillis();

        List<BrandDTO> brandsDtoAdded = new ArrayList<>();

        brandDtoList.forEach(brandDTO -> {

            //Comprobamos que la marca exista en la base de datos y la añadimos si no existe.
            //En caso de que se añada una nueva marca, se devuelve el objeto marca añadido y se añade al vehiculo.
            Optional<Brand> brandToAdd = brandRepository.findByName(brandDTO.getName());

            brandToAdd.ifPresentOrElse(brand -> log.info("This brand already exist: {}", brand.getName()), () -> {

                // Guardamos la marca en la base de datos.
                Brand savedBrand = brandRepository.save(modelMapper.map(brandDTO, Brand.class));

                if (savedBrand.getId() != null) {

                    log.info("Brand was added correctly: {}", savedBrand);

                    // Añadimos la marca a la lista de marcas añadidas.
                    brandsDtoAdded.add(modelMapper.map(savedBrand, BrandDTO.class));

                } else {
                    log.error("Failed to save brand: {}", brandDTO.getName());

                }
            });
        });

        long endTime = System.currentTimeMillis();
        log.info("Execution time: {} ms", (endTime - startTime));

        /* CompletableFuture devuelve una variable de tipo Future que se completa con el fin de la ejecución.
            Si la lista de marcas guardadas está vacía se devuelve un Optional.empty().
            Si la lista de marcas guardadas no está vacía, se devuelve un Optional que contiene la lista objetos BrandDTO. */
        return CompletableFuture.completedFuture(brandsDtoAdded.isEmpty() ? Optional.empty() : Optional.of(brandsDtoAdded));
    }

    // Cuando añadimos un nuevo vehículo comprueba si la marca existe en la base de datos y si es válida, si no existe se añade.
    private Optional<BrandDTO> brandChecker(BrandDTO brandDTO) {

        // AtomicReference es una clase que permite almacenar una referencia a un objeto en un entorno concurrente.
        AtomicReference<Brand> brandChecked = new AtomicReference<>(modelMapper.map(brandDTO, Brand.class));

        // Si brand llega sin id, se añade a la base de datos.
        if (brandDTO.getId() == null || brandDTO.getId().isEmpty() || brandDTO.getId().equals("string")) {

            // Si el nombre de la marca es nulo, vacío o "string" se devuelve un Optional vacío.
            if (brandDTO.getName() == null || brandDTO.getName().equals("string") || brandDTO.getName().isEmpty()) {

                log.info("Not brand provided");
                brandChecked.set(null);

            } else {
                brandRepository.findByName(brandDTO.getName()).ifPresentOrElse(

                        // Sustituye al lambda "brand -> {brandChecked.set(brand);}"
                        brandChecked::set, () -> {
                            log.info("Brand provided does not exist in the database. New brand will be added.");
                            brandDTO.setId(null);
                            brandChecked.set(brandRepository.save(modelMapper.map(brandDTO, Brand.class)));
                            log.info("New brand added to the database: {}", brandRepository.findByName(brandDTO.getName()));
                        });
            }

            // Si brand llega con id, se comprueba si existe en la base de datos.
        } else if (brandRepository.findById(brandDTO.getId()).isEmpty()) {

            // Si no existe ninguna marca con ese id se comprueba si en la base de datos existe alguna marca con el nombre que trae el objeto brand.
            brandRepository.findByName(brandDTO.getName()).ifPresentOrElse(

                    // Este método se ejecuta si la marca existe en la base de datos.
                    // Sustituye al lambda "brand -> {brandChecked.set(brand);}"
                    brandChecked::set, () -> {
                        log.info("Brand provided does not exist and no brand name matches the one provided.");
                        brandChecked.set(null);
                    });
        }

        // Devuelve un Optional con el objeto Brand si este existe, si no existe devuelve un Optional vacío.
        if (brandChecked.get() == null) {
            return Optional.empty();

        } else {
            return Optional.of(modelMapper.map(brandChecked.get(), BrandDTO.class));
        }
    }
}