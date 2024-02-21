package com.aalonso.carregistry.controller;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.dto.VehicleOrBrandIdRequest;
import com.aalonso.carregistry.services.interfaces.AddVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.DeleteVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.ShowVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.UpdateVehicleBrandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRegistryControllerTest {

    @InjectMocks
    private CarRegistryController carRegistryController;

    @Mock
    private ShowVehicleBrandService showVehicleBrandService;
    @Mock
    private AddVehicleBrandService addVehicleBrandService;
    @Mock
    private DeleteVehicleBrandService deleteVehicleBrandService;
    @Mock
    private UpdateVehicleBrandService updateVehicleBrandService;

    /**
     * ***** Tests showAllVehicles ******
     */
    @Test
    void test_ShowAllVehicles() {

        VehicleDTO vehicle1 = new VehicleDTO();
        VehicleDTO vehicle2 = new VehicleDTO();
        List<VehicleDTO> vehicleList = Arrays.asList(vehicle1, vehicle2);

        when(showVehicleBrandService.showAllVehicles()).thenReturn(CompletableFuture.completedFuture(Optional.of(vehicleList)));

        CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> response = carRegistryController.showAllVehicles();

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertNotNull(response.join().getBody());
        assertTrue(Objects.requireNonNull(response.join().getBody()).isPresent());
        assertEquals(vehicleList, Objects.requireNonNull(response.join().getBody()).get());
    }

    @Test
    void test_ShowAllVehicles_failed() {

        when(showVehicleBrandService.showAllVehicles()).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> response = carRegistryController.showAllVehicles();

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
    }

    /**
     * ***** Tests showVehicleById ******
     */
    @Test
    void test_showVehicleById() {
        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");
        VehicleDTO vehicleDTO = new VehicleDTO();

        when(showVehicleBrandService.showVehicleById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.of(vehicleDTO));

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.showVehicleById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(vehicleDTO, response.getBody().get());
    }

    @Test
    void test_showVehicleById_failed() {
        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");

        when(showVehicleBrandService.showVehicleById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.empty());

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.showVehicleById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_showVehicleById_nullRequest() {
        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.showVehicleById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * ***** Tests showAllBrands ******
     */
    @Test
    void test_showAllBrands() {

        BrandDTO brandDTO1 = new BrandDTO();
        BrandDTO brandDTO2 = new BrandDTO();
        List<BrandDTO> brandList = Arrays.asList(brandDTO1, brandDTO2);

        when(showVehicleBrandService.showAllBrands()).thenReturn(CompletableFuture.completedFuture(Optional.of(brandList)));

        CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> response = carRegistryController.showAllBrands();

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        Optional<List<BrandDTO>> responseBody = response.join().getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isPresent());
        assertEquals(brandList, responseBody.get());
    }

    @Test
    void test_showAllBrands_failed() {

        when(showVehicleBrandService.showAllBrands()).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> response = carRegistryController.showAllBrands();

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
    }

    /**
     * ***** Tests showBrandById ******
     */
    @Test
    void test_showBrandById() {

        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");
        BrandDTO brandDTO = new BrandDTO();

        when(showVehicleBrandService.showBrandById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.of(brandDTO));

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.showBrandById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(brandDTO, response.getBody().get());
    }

    @Test
    void test_showBrandById_failed() {
        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");

        when(showVehicleBrandService.showBrandById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.empty());

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.showBrandById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void test_showBrandById_nullRequest() {
        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.showBrandById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    /**
     * ***** Tests addVehicle ******
     */
    @Test
    void test_addVehicle() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        VehicleDTO vehicleDTO1 = new VehicleDTO();
        List<VehicleDTO> vehicleList = Arrays.asList(vehicleDTO, vehicleDTO1);

        when(addVehicleBrandService.addVehicles(vehicleList)).thenReturn(CompletableFuture.completedFuture(Optional.of(vehicleList)));

        CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> response = carRegistryController.addVehicle(vehicleList);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        Optional<List<VehicleDTO>> responseBody = response.join().getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isPresent());
        assertEquals(2, responseBody.get().size());
    }

    @Test
    void test_addVehicle_partially() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        VehicleDTO vehicleDTO1 = new VehicleDTO();
        List<VehicleDTO> vehicleList = Arrays.asList(vehicleDTO, vehicleDTO1);

        when(addVehicleBrandService.addVehicles(vehicleList)).thenReturn(CompletableFuture.completedFuture(Optional.of(List.of(new VehicleDTO()))));

        CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> response = carRegistryController.addVehicle(vehicleList);

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.join().getStatusCode());
        Optional<List<VehicleDTO>> responseBody = response.join().getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isPresent());
        assertNotEquals(vehicleList.size(), responseBody.get().size());
    }

    @Test
    void test_addVehicle_failed() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        VehicleDTO vehicleDTO1 = new VehicleDTO();
        List<VehicleDTO> vehicleList = Arrays.asList(vehicleDTO, vehicleDTO1);

        when(addVehicleBrandService.addVehicles(vehicleList)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> response = carRegistryController.addVehicle(vehicleList);

        assertEquals(HttpStatus.CONFLICT, response.join().getStatusCode());
    }

    /**
     * ***** Tests addBrand ******
     */
    @Test
    void test_addBrand() {

        BrandDTO brandDTO = new BrandDTO();
        BrandDTO brandDTO1 = new BrandDTO();
        List<BrandDTO> brandList = Arrays.asList(brandDTO, brandDTO1);

        when(addVehicleBrandService.addBrands(brandList)).thenReturn(CompletableFuture.completedFuture(Optional.of(brandList)));

        CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> response = carRegistryController.addBrand(brandList);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        Optional<List<BrandDTO>> responseBody = response.join().getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isPresent());
        assertEquals(2, responseBody.get().size());
    }

    @Test
    void test_addBrand_partially() {

        BrandDTO brandDTO = new BrandDTO();
        BrandDTO brandDTO1 = new BrandDTO();
        List<BrandDTO> brandList = Arrays.asList(brandDTO, brandDTO1);

        when(addVehicleBrandService.addBrands(brandList)).thenReturn(CompletableFuture.completedFuture(Optional.of(List.of(new BrandDTO()))));

        CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> response = carRegistryController.addBrand(brandList);

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.join().getStatusCode());
        Optional<List<BrandDTO>> responseBody = response.join().getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isPresent());
        assertNotEquals(brandList.size(), responseBody.get().size());
    }

    @Test
    void test_addBrand_failed() {

        BrandDTO brandDTO = new BrandDTO();
        BrandDTO brandDTO1 = new BrandDTO();
        List<BrandDTO> brandList = Arrays.asList(brandDTO, brandDTO1);

        when(addVehicleBrandService.addBrands(brandList)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> response = carRegistryController.addBrand(brandList);

        assertEquals(HttpStatus.CONFLICT, response.join().getStatusCode());
    }

    /**
     * ***** Tests updateVehicle ******
     */
    @Test
    void test_updateVehicle() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        when(updateVehicleBrandService.updateVehicle(vehicleDTO)).thenReturn(Optional.of(vehicleDTO));

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.updateVehicle(vehicleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(vehicleDTO, response.getBody().get());
    }

    @Test
    void test_updateVehicle_failed() {

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId("1");

        when(updateVehicleBrandService.updateVehicle(vehicleDTO)).thenReturn(Optional.empty());

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.updateVehicle(vehicleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_updateVehicle_nullRequest() {

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.updateVehicle(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * ***** Tests updateBrand ******
     */
    @Test
    void test_updateBrand() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        when(updateVehicleBrandService.updateBrand(brandDTO)).thenReturn(Optional.of(brandDTO));

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.updateBrand(brandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(brandDTO, response.getBody().get());
    }

    @Test
    void test_updateBrand_failed() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId("1");

        when(updateVehicleBrandService.updateBrand(brandDTO)).thenReturn(Optional.empty());

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.updateBrand(brandDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_updateBrand_nullRequest() {

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.updateBrand(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * ***** Tests deleteVehicleById ******
     */
    @Test
    void test_deleteVehicleById() {

        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");
        VehicleDTO vehicleDTO = new VehicleDTO();

        when(deleteVehicleBrandService.deleteVehicleById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.of(vehicleDTO));

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.deleteVehicleById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(vehicleDTO, response.getBody().get());
    }

    @Test
    void test_deleteVehicleById_failed() {

        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");

        when(deleteVehicleBrandService.deleteVehicleById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.empty());

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.deleteVehicleById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_deleteVehicleById_failed_badRequest() {

        ResponseEntity<Optional<VehicleDTO>> response = carRegistryController.deleteVehicleById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * ***** Tests deleteBrandById ******
     */
    @Test
    void test_deleteBrandById() {

        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");
        BrandDTO brandDTO = new BrandDTO();

        when(deleteVehicleBrandService.deleteBrandById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.of(brandDTO));

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.deleteBrandById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        assertEquals(brandDTO, response.getBody().get());
    }

    @Test
    void test_deleteBrandById_failed() {

        VehicleOrBrandIdRequest vehicleOrBrandIdRequest = new VehicleOrBrandIdRequest("1");

        when(deleteVehicleBrandService.deleteBrandById(vehicleOrBrandIdRequest.getId())).thenReturn(Optional.empty());

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.deleteBrandById(vehicleOrBrandIdRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_deleteBrandById_failed_badRequest() {

        ResponseEntity<Optional<BrandDTO>> response = carRegistryController.deleteBrandById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}