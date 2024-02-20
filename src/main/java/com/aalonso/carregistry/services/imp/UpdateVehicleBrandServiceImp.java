package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.Brand;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import com.aalonso.carregistry.persistence.repository.BrandRepository;
import com.aalonso.carregistry.persistence.repository.VehicleRepository;
import com.aalonso.carregistry.services.interfaces.UpdateVehicleBrandService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UpdateVehicleBrandServiceImp implements UpdateVehicleBrandService {
    @PostConstruct
    public void init() {
        log.info("UpdateVehicleBrandServiceImp is operational...");
    }

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    BrandRepository brandRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    // Actualiza vehiculos en la base de datos
    @Override
    @Transactional
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
    @Override
    @Transactional
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
