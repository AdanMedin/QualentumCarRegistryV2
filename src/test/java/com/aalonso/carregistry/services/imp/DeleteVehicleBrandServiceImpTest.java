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
class DeleteVehicleBrandServiceImpTest {

    @InjectMocks
    private DeleteVehicleBrandServiceImp deleteVehicleBrandServiceImp;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private BrandRepository brandRepository;

    @Test
    void test_deleteVehicleById() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");
        vehicleDTO.setModel("Coupe");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");
        vehicle.setModel("Coupe");

        when(vehicleRepository.findById("1")).thenReturn(Optional.of(vehicle));

        Optional<VehicleDTO> result = deleteVehicleBrandServiceImp.deleteVehicleById("1");

        assertTrue(result.isPresent());
        assertEquals(vehicleDTO, result.get());
    }

    @Test
    void test_deleteVehicleById_failed() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");
        vehicleDTO.setModel("Coupe");

        when(vehicleRepository.findById("1")).thenReturn(Optional.empty());

        Optional<VehicleDTO> result = deleteVehicleBrandServiceImp.deleteVehicleById("1");

        assertFalse(result.isPresent());
    }

    @Test
    void test_deleteBrandById() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");
        brandDTO.setName("Toyota");

        Brand brand = new Brand();
        brand.setId("1");
        brand.setName("Toyota");

        when(brandRepository.findById("1")).thenReturn(Optional.of(brand));

        Optional<BrandDTO> result = deleteVehicleBrandServiceImp.deleteBrandById("1");

        assertTrue(result.isPresent());
        assertEquals(brandDTO, result.get());
    }

    @Test
    void test_deleteBrandById_failed() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");
        brandDTO.setName("Toyota");

        when(brandRepository.findById("1")).thenReturn(Optional.empty());

        Optional<BrandDTO> result = deleteVehicleBrandServiceImp.deleteBrandById("1");

        assertFalse(result.isPresent());
    }
}