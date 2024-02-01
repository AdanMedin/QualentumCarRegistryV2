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

@Service
@Slf4j
public class AddVehicleService {
    @PostConstruct
    public void init() {
        log.info("AddVehicleService is operational...");
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
            brandChecker(vehicleDTO.getBrand()).ifPresent(brand -> vehicleDTO.setBrand(modelMapper.map(brand, BrandDTO.class)));

            //Guardamos el vehiculo en la base de datos.
            Vehicle savedVehicle = vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
            if (savedVehicle.getId() != null) {
                log.info("Vehicle was added correctly: \n{}", vehicleDTO);
                vehiclesDtoAdded.add(modelMapper.map(savedVehicle, VehicleDTO.class));
            } else {
                log.error("Failed to save vehicle: \n{}", vehicleDTO);
            }

        });
        return vehiclesDtoAdded.isEmpty() ? Optional.empty() : Optional.of(vehiclesDtoAdded);
    }

    private Optional<Brand> brandChecker(BrandDTO brandDTO) {
        Brand savedBrand = new Brand();
        if (brandDTO.getId() == null || brandDTO.getId().isEmpty() || brandDTO.getId().equals("string")) {
            log.info("New brand will be added to the database: \n{}", brandDTO);
            savedBrand = brandRepository.save(modelMapper.map(brandDTO, Brand.class));
        } else if (brandRepository.findById(brandDTO.getId()).isEmpty()) {
            log.info("Brand provided does not exist");
            log.info("New brand will be added to the database: \n{}", brandDTO);
            brandDTO.setId(null);
            savedBrand = brandRepository.save(modelMapper.map(brandDTO, Brand.class));
        }
        return brandRepository.findById(savedBrand.getId());
    }
}