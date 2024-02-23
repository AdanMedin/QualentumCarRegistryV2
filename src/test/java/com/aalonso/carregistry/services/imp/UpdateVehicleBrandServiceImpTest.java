package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.Brand;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import com.aalonso.carregistry.persistence.repository.BrandRepository;
import com.aalonso.carregistry.persistence.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateVehicleBrandServiceImpTest {

    @InjectMocks
    UpdateVehicleBrandServiceImp updateVehicleBrandServiceImp;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private BrandRepository brandRepository;

    @Test
    void test_updateVehicle() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");

        when(vehicleRepository.findById(vehicleDTO.getId())).thenReturn(Optional.of(vehicle));
        Optional<VehicleDTO> result = updateVehicleBrandServiceImp.updateVehicle(vehicleDTO);

        assertTrue(result.isPresent());
        assertEquals(vehicleDTO, result.get());
    }

    @Test
    void test_updateVehicle_failed() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");

        when(vehicleRepository.findById(vehicleDTO.getId())).thenReturn(Optional.empty());
        Optional<VehicleDTO> result = updateVehicleBrandServiceImp.updateVehicle(vehicleDTO);

        assertFalse(result.isPresent());
    }

    @Test
    void test_updateBrand() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        Brand brand = new Brand();
        brand.setId("1");

        when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.of(brand));
        Optional<BrandDTO> result = updateVehicleBrandServiceImp.updateBrand(brandDTO);

        assertTrue(result.isPresent());
        assertEquals(brandDTO, result.get());
    }

    @Test
    void test_updateBrand_failed() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        Brand brand = new Brand();
        brand.setId("1");

        when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.empty());
        Optional<BrandDTO> result = updateVehicleBrandServiceImp.updateBrand(brandDTO);

        assertFalse(result.isPresent());
    }
}