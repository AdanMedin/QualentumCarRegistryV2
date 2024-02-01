package com.aalonso.CarRegistry.services;

import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.persistence.entity.Vehicle;
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

    private final ModelMapper modelMapper = new ModelMapper();


    //Agregar vehiculos a la base de datos
    public Optional<List<VehicleDTO>> addVehicles(List<VehicleDTO> vehicleDtoList) {
        log.info("Accessed add vehicle service...");
        List<VehicleDTO> vehiclesDtoAdded = new ArrayList<>();
        vehicleDtoList.forEach(vehicleDTO -> {
            if (vehicleDTO.getId() == null) {
                log.error("Vehicle does not have an ID. It was not added.");
            } else {
                vehicleRepository.findById(Integer.valueOf(vehicleDTO.getId())).ifPresentOrElse(
                        vehicleFound -> log.info("Vehicle with id: {} already exists. Was not added", vehicleDTO.getId()),
                        () -> {
                            log.info("Vehicle with does not exist. Was added correctly: \n{}", vehicleDTO);
                            vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));// Mapeamos el DTO a la entidad
                            vehiclesDtoAdded.add(vehicleDTO);
                        }
                );
            }
        });
        return vehiclesDtoAdded.isEmpty() ? Optional.empty() : Optional.of(vehiclesDtoAdded);
    }
}