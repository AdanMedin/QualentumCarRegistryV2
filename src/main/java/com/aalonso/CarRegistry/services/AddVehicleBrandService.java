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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class AddVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("AddVehicleBrandService is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BrandRepository brandRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    //Agregar vehiculos a la base de datos
    public Optional<List<VehicleDTO>> addVehicles(List<VehicleDTO> vehicleDtoList) {
        log.info("Accessed add vehicle service...");
        List<VehicleDTO> vehiclesDtoAdded = new ArrayList<>();
        vehicleDtoList.forEach(vehicleDTO -> {
            //Comprobamos que la marca exista en la base de datos y la añadimos si no existe.
            //En caso de que se añada una nueva marca, se devuelve el objeto marca añadido y se añade al vehiculo.
            brandChecker(vehicleDTO.getBrand()).ifPresent(brand ->
                    vehicleDTO.setBrand(modelMapper.map(brand, BrandDTO.class))
            );

            //Guardamos el vehiculo en la base de datos.
            Vehicle savedVehicle = vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
            if (savedVehicle.getId() != null) {
                log.info("Vehicle was added correctly: {}", vehicleDTO);
                vehiclesDtoAdded.add(modelMapper.map(savedVehicle, VehicleDTO.class));
            } else {
                log.error("Failed to save vehicle: {}", vehicleDTO);
            }

        });
        return vehiclesDtoAdded.isEmpty() ? Optional.empty() : Optional.of(vehiclesDtoAdded);
    }

    //Agregar marcas a la base de datos
    public Optional<List<BrandDTO>> addBrands(List<BrandDTO> brandDtoList) {
        log.info("Accessed add vehicle service...");
        List<BrandDTO> brandsDtoAdded = new ArrayList<>();
        brandDtoList.forEach(brandDTO -> {
            //Comprobamos que la marca exista en la base de datos y la añadimos si no existe.
            //En caso de que se añada una nueva marca, se devuelve el objeto marca añadido y se añade al vehiculo.
            Optional<Brand> brandToAdd = brandRepository.findByName(brandDTO.getName());
            brandToAdd.ifPresentOrElse(
                    brand -> log.info("This brand already exist: {}", brand.getName()),
                    () -> {
                        // Guardamos la marca en la base de datos.
                        Brand savedBrand = brandRepository.save(modelMapper.map(brandDTO, Brand.class));
                        if (savedBrand.getId() != null) {
                            log.info("Brand was added correctly: {}", brandDTO);
                            brandsDtoAdded.add(modelMapper.map(savedBrand, BrandDTO.class));
                        } else {
                            log.error("Failed to save brand: {}", brandDTO.getName());
                        }
                    });
        });
        return brandsDtoAdded.isEmpty() ? Optional.empty() : Optional.of(brandsDtoAdded);
    }

    // Cuando añadimos un nuevo vehículo comprueba si la marca existe en la base de datos, si no existe se añade.
    private Optional<Brand> brandChecker(BrandDTO brandDTO) {
        AtomicReference<Brand> brandChecked = new AtomicReference<>(modelMapper.map(brandDTO, Brand.class));
        // Si brand llega sin id, se añade a la base de datos.
        if (brandDTO.getId() == null || brandDTO.getId().isEmpty() || brandDTO.getId().equals("string")) {
            log.info("Brand provided does not exist");
            brandChecked.set(brandRepository.save(modelMapper.map(brandDTO, Brand.class)));
            log.info("New brand added to the database: {}", brandDTO);
            // Si brand llega con id, se comprueba si existe en la base de datos.
        } else if (brandRepository.findById(brandDTO.getId()).isEmpty()) {
            // Si no existe ninguna marca con ese id se comprueba si en la base de datos existe alguna marca con el nombre que trae el objeto brand.
            brandRepository.findByName(brandDTO.getName()).ifPresentOrElse(
                    // Este método se ejecuta si la marca existe en la base de datos.
                    // Sustituye al lambda "brand -> {brandChecked.set(brand);}"
                    brandChecked::set,
                    () -> {
                        log.info("Brand provided does not exist");
                        if (brandDTO.getName().equals("string") || brandDTO.getName().isEmpty()) {
                            brandChecked.set(null);
                        }
                        brandDTO.setId(null);
                        brandChecked.set(brandRepository.save(modelMapper.map(brandDTO, Brand.class)));
                        log.info("New brand added to the database: {}", brandRepository.findByName(brandDTO.getName()));
                    }
            );
        }
        if (brandChecked.get() == null) {
            return Optional.empty();
        } else {
            return brandRepository.findById(brandChecked.get().getId());
        }
    }
}