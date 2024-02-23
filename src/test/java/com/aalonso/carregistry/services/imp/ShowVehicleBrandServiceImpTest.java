package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.Brand;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import com.aalonso.carregistry.persistence.repository.BrandRepository;
import com.aalonso.carregistry.persistence.repository.VehicleRepository;
import com.aalonso.carregistry.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowVehicleBrandServiceImpTest {

    @InjectMocks
    private ShowVehicleBrandServiceImp showVehicleBrandServiceImp;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private Utils utils;

    @Test
    void test_showAllVehicles() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");

        VehicleDTO vehicleDTO2 = new VehicleDTO();
        vehicleDTO2.setId("2");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId("2");

        List<VehicleDTO> vehicleDTOList = List.of(vehicleDTO, vehicleDTO2);

        List<Vehicle> vehicleList = List.of(vehicle, vehicle2);

        when(vehicleRepository.findAll()).thenReturn(vehicleList);
        CompletableFuture<Optional<List<VehicleDTO>>> result = showVehicleBrandServiceImp.showAllVehicles();

        assertTrue(result.join().isPresent());
        assertEquals(vehicleDTOList, result.join().get());
    }

    @Test
    void test_showAllVehicles_failed() {

        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>());
        CompletableFuture<Optional<List<VehicleDTO>>> result = showVehicleBrandServiceImp.showAllVehicles();

        assertTrue(result.join().isEmpty());
    }

    @Test
    void test_showVehicleById() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");

        when(vehicleRepository.findById("1")).thenReturn(Optional.of(vehicle));

        Optional<VehicleDTO> result = showVehicleBrandServiceImp.showVehicleById("1");

        assertTrue(result.isPresent());
        assertEquals(vehicleDTO, result.get());
    }

    @Test
    void test_showVehicleById_failed() {

        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");

        when(vehicleRepository.findById("1")).thenReturn(Optional.empty());

        Optional<VehicleDTO> result = showVehicleBrandServiceImp.showVehicleById("1");

        assertFalse(result.isPresent());
    }

    @Test
    void test_showAllBrands() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        Brand brand = new Brand();
        brand.setId("1");

        BrandDTO brandDTO2 = new BrandDTO();
        brandDTO2.setId("1");

        Brand brand2 = new Brand();
        brand2.setId("1");

        List<BrandDTO> brandDTOList = List.of(brandDTO, brandDTO2);

        List<Brand> brandList = List.of(brand, brand2);

        when(brandRepository.findAll()).thenReturn(brandList);
        CompletableFuture<Optional<List<BrandDTO>>> result = showVehicleBrandServiceImp.showAllBrands();

        assertTrue(result.join().isPresent());
        assertEquals(brandDTOList, result.join().get());
    }

    @Test
    void test_showAllBrands_failed() {

        when(brandRepository.findAll()).thenReturn(new ArrayList<>());
        CompletableFuture<Optional<List<BrandDTO>>> result = showVehicleBrandServiceImp.showAllBrands();

        assertTrue(result.join().isEmpty());
    }

    @Test
    void test_showBrandById() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        Brand brand = new Brand();
        brand.setId("1");

        when(brandRepository.findById("1")).thenReturn(Optional.of(brand));

        Optional<BrandDTO> result = showVehicleBrandServiceImp.showBrandById("1");

        assertTrue(result.isPresent());
        assertEquals(brandDTO, result.get());
    }

    @Test
    void test_showBrandById_failed() {

        Brand brand = new Brand();
        brand.setId("1");

        when(brandRepository.findById("1")).thenReturn(Optional.empty());

        Optional<BrandDTO> result = showVehicleBrandServiceImp.showBrandById("1");

        assertFalse(result.isPresent());
    }
}