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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddVehicleBrandServiceImpTest {

    @InjectMocks
    private AddVehicleBrandServiceImp addVehicleBrandServiceImp;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private BrandRepository brandRepository;

    @Test
    void test_addVehicles() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testBrand");

        Brand brand = new Brand();
        brand.setId("testId");
        brand.setName("testBrand");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setBrand(brandDTO);
        vehicleDTO.setId("testId");

        VehicleDTO vehicleDTO1 = new VehicleDTO();
        vehicleDTO1.setBrand(brandDTO);
        vehicleDTO1.setId("testId1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("testId");
        vehicle.setBrand(brand);

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setId("testId1");
        vehicle1.setBrand(brand);

        List<VehicleDTO> vehicleDTOList = List.of(vehicleDTO, vehicleDTO1);

        when(addVehicleBrandServiceImp.brandChecker(brandDTO)).thenReturn(Optional.of(brandDTO));
        when(brandRepository.findById("testId")).thenReturn(Optional.empty());
        when(brandRepository.findByName("testBrand")).thenReturn(Optional.of(brand));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        CompletableFuture<Optional<List<VehicleDTO>>> result = addVehicleBrandServiceImp.addVehicles(vehicleDTOList);

        assertTrue(result.join().isPresent());
        assertEquals(2, result.join().get().size());
        assertEquals(vehicleDTO, result.join().get().get(0));
        assertEquals(vehicleDTO1, result.join().get().get(1));
    }


    @Test
    void test_addVehicles_partially() {

        //Uno de los vehículos llega sin marca. Vehicle1 no se debería guardar.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testBrand");

        Brand brand = new Brand();
        brand.setId("testId");
        brand.setName("testBrand");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setBrand(brandDTO);
        vehicleDTO.setId("testId");

        VehicleDTO vehicleDTO1 = new VehicleDTO();
        vehicleDTO1.setId("testId1");

        Vehicle vehicle = new Vehicle();
        vehicle.setId("testId");
        vehicle.setBrand(brand);

        List<VehicleDTO> vehicleDTOList = List.of(vehicleDTO, vehicleDTO1);

        when(addVehicleBrandServiceImp.brandChecker(brandDTO)).thenReturn(Optional.of(brandDTO));
        when(brandRepository.findById("testId")).thenReturn(Optional.empty());
        when(brandRepository.findByName("testBrand")).thenReturn(Optional.of(brand));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        CompletableFuture<Optional<List<VehicleDTO>>> result = addVehicleBrandServiceImp.addVehicles(vehicleDTOList);

        assertTrue(result.join().isPresent());
        assertEquals(1, result.join().get().size());
        assertEquals(vehicleDTO, result.join().get().get(0));
    }

    @Test
    void test_addVehicles_failed() {

        //Los dos vehículos llega sin marca. No se debería guardar ninguno.

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("testId");

        VehicleDTO vehicleDTO1 = new VehicleDTO();
        vehicleDTO1.setId("testId1");

        List<VehicleDTO> vehicleDTOList = List.of(vehicleDTO, vehicleDTO1);

        CompletableFuture<Optional<List<VehicleDTO>>> result = addVehicleBrandServiceImp.addVehicles(vehicleDTOList);

        assertFalse(result.join().isPresent());
    }

    @Test
    void test_addBrands() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testBrand");

        BrandDTO brandDTO2 = new BrandDTO();
        brandDTO2.setId("testId2");
        brandDTO2.setName("testBrand2");

        List<BrandDTO> brandDtoList = Arrays.asList(brandDTO, brandDTO2);

        Brand brand = new Brand();
        brand.setId("testId");
        brand.setName("testBrand");

        Brand brand2 = new Brand();
        brand2.setId("testId2");
        brand2.setName("testBrand2");

        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(brandRepository.save(brand)).thenReturn(brand);
        when(brandRepository.save(brand2)).thenReturn(brand2);

        CompletableFuture<Optional<List<BrandDTO>>> result = addVehicleBrandServiceImp.addBrands(brandDtoList);

        assertTrue(result.join().isPresent());
        assertEquals(2, result.join().get().size());
        assertEquals(brandDTO, result.join().get().get(0));
        assertEquals(brandDTO2, result.join().get().get(1));
    }

    @Test
    void test_addBrands_failed() {

        //  Las marcas ya existe en la base de datos. No se debería guardar ninguna marca.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testBrand");
        BrandDTO brandDTO2 = new BrandDTO();
        brandDTO2.setId("testId2");
        brandDTO2.setName("testBrand2");

        List<BrandDTO> brandDtoList = Arrays.asList(brandDTO, brandDTO2);

        Brand brand = new Brand();
        brand.setId("testId");
        brand.setName("testBrand");

        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));

        CompletableFuture<Optional<List<BrandDTO>>> result = addVehicleBrandServiceImp.addBrands(brandDtoList);

        assertFalse(result.join().isPresent());
    }

    @Test
    void test_addBrands_partially() {

        // Una de las marcas ya existe en la base de datos. Solo se debería guardar una marca.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testBrand");

        BrandDTO brandDTO2 = new BrandDTO();
        brandDTO2.setId("testId2");
        brandDTO2.setName("testBrand2");

        List<BrandDTO> brandDtoList = Arrays.asList(brandDTO, brandDTO2);

        Brand brand2 = new Brand();
        brand2.setId("testId2");
        brand2.setName("testBrand2");

        when(brandRepository.findByName("testBrand")).thenReturn(Optional.of(new Brand()));
        when(brandRepository.findByName("testBrand2")).thenReturn(Optional.empty());
        when(brandRepository.save(any(Brand.class))).thenReturn(brand2);

        CompletableFuture<Optional<List<BrandDTO>>> result = addVehicleBrandServiceImp.addBrands(brandDtoList);

        assertTrue(result.join().isPresent());
        assertEquals(1, result.join().get().size());
        assertEquals("testBrand2", result.join().get().get(0).getName());
    }

    @Test
    void test_brandChecker_exist() {

        // La marca existe en la base de datos. Se debería devolver el objeto BrandDTO.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testId");
        brandDTO.setName("testName");

        Brand brand = new Brand();
        brand.setId("testId");
        brand.setName("testName");

        when(brandRepository.findByName(brandDTO.getName())).thenReturn(Optional.of(brand));

        Optional<BrandDTO> result = addVehicleBrandServiceImp.brandChecker(brandDTO);

        assertTrue(result.isPresent());
        assertEquals(brandDTO, result.get());
    }

    @Test
    void test_brandChecker_notExist() {

        // La marca no existe en la base de datos. Se debería añadir y devolver el objeto BrandDTO.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("testName");

        Brand brand = new Brand();
        brand.setName("testName");

        BrandDTO brandDTO1 = new BrandDTO();
        brandDTO1.setId("testId");
        brandDTO1.setName("testName");

        Brand brand1 = new Brand();
        brand1.setId("testId");
        brand1.setName("testName");

        when(brandRepository.findByName(brandDTO.getName())).thenReturn(Optional.empty());
        when(brandRepository.save(brand)).thenReturn(brand1);

        Optional<BrandDTO> result = addVehicleBrandServiceImp.brandChecker(brandDTO);

        assertTrue(result.isPresent());
        assertEquals(brandDTO1, result.get());
    }

    @Test
    void test_brandChecker_invalid() {

        // El id de la marca no existe en la base de datos y el nombre facilitado tampoco.
        // No debería añadir la marca y devolverá un Optional vacío.

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("testInvalidId");
        brandDTO.setName("testInvalidName");

        Brand brand = new Brand();
        brand.setId("testInvalidId");
        brand.setName("testInvalidName");

        BrandDTO brandDTO1 = new BrandDTO();
        brandDTO1.setId("testId");
        brandDTO1.setName("testName");

        Brand brand1 = new Brand();
        brand1.setId("testId");
        brand1.setName("testName");


        when(brandRepository.findById(brandDTO.getId())).thenReturn(Optional.empty());
        when(brandRepository.findByName(brandDTO.getName())).thenReturn(Optional.empty());

        Optional<BrandDTO> result = addVehicleBrandServiceImp.brandChecker(brandDTO);

        assertFalse(result.isPresent());
    }
}