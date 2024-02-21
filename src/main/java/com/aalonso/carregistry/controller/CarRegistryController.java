package com.aalonso.carregistry.controller;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.dto.VehicleOrBrandIdRequest;
import com.aalonso.carregistry.services.interfaces.AddVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.DeleteVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.ShowVehicleBrandService;
import com.aalonso.carregistry.services.interfaces.UpdateVehicleBrandService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/car_registry")
public class CarRegistryController {
    @PostConstruct
    public void init() {
        log.info("CarRegistryController is operational...");
    }

    private static final String ACCESS_MESSAGE = "Accessed car registry controller...";
    private static final String WRONG_BRAND_ID_MESSAGE = "Not brand id was provided";
    private static final String WRONG_VEHICLE_ID_MESSAGE = "Not vehicle id was provided";


    private final ShowVehicleBrandService showVehicleBrandService;

    private final AddVehicleBrandService addVehicleBrandService;

    private final DeleteVehicleBrandService deleteVehicleBrandService;

    private final UpdateVehicleBrandService updateVehicleBrandService;

    // Endpoint para mostrar todos los vehiculos.
    @GetMapping(value = "/show_all_vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
    public CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> showAllVehicles() {

        log.info(ACCESS_MESSAGE);
        CompletableFuture<Optional<List<VehicleDTO>>> vehicleList = showVehicleBrandService.showAllVehicles();

        /*  ".ThenApply" es un método de CompletableFuture que toma una función y la aplica al resultado cuando está disponible.    */
        return vehicleList.thenApply(vehicles -> {

            if (vehicles.isEmpty()) {
                log.info("Not vehicles found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(vehicles, HttpStatus.OK);
        });
    }

    // Endpoint para mostrar un vehiculo por id.
    @GetMapping(value = "/show_vehicle_by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseEntity<Optional<VehicleDTO>> showVehicleById(VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {

        log.info(ACCESS_MESSAGE);

        if (vehicleOrBrandIdRequest == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error(WRONG_VEHICLE_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        Optional<VehicleDTO> vehicle = showVehicleBrandService.showVehicleById(vehicleOrBrandIdRequest.getId());

        if (vehicle.isEmpty()) {
            log.info("Vehicle not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vehicle, HttpStatus.OK);

    }

    // Endpoint para mostrar todas las marcas.
    @GetMapping(value = "/show_all_brands", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
    public CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> showAllBrands() {

        log.info(ACCESS_MESSAGE);
        CompletableFuture<Optional<List<BrandDTO>>> brandList = showVehicleBrandService.showAllBrands();

        /*  ".ThenApply" es un método de CompletableFuture que toma una función y la aplica al resultado cuando está disponible.    */
        return brandList.thenApply(brands -> {

            if (brands.isEmpty()) {
                log.info("Not brands found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(brands, HttpStatus.OK);
        });
    }

    // Endpoint para mostrar una marca por id.
    @GetMapping(value = "/show_brand_by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseEntity<Optional<BrandDTO>> showBrandById(VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {

        log.info(ACCESS_MESSAGE);

        if (vehicleOrBrandIdRequest == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error(WRONG_BRAND_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        Optional<BrandDTO> vehicle = showVehicleBrandService.showBrandById(vehicleOrBrandIdRequest.getId());

        if (vehicle.isEmpty()) {
            log.info("Brand not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vehicle, HttpStatus.OK);

    }

    // Endpoint para añadir vehiculos a la base de datos.
    @PostMapping(value = "/add_vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CompletableFuture<ResponseEntity<Optional<List<VehicleDTO>>>> addVehicle(@RequestBody List<VehicleDTO> vehicleDTOList) {

        log.info(ACCESS_MESSAGE);

        if (vehicleDTOList == null || vehicleDTOList.isEmpty()) {

            log.error("Not vehicles to add. Vehicle list is empty");
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        } else {

            CompletableFuture<Optional<List<VehicleDTO>>> addedVehicles = addVehicleBrandService.addVehicles(vehicleDTOList);

            /*  ".ThenApply" es un método de CompletableFuture que toma una función y la aplica al resultado cuando está disponible.    */
            return addedVehicles.thenApply(vehicles -> {

                if (vehicles.isPresent() && vehicles.get().size() == vehicleDTOList.size()) {

                    log.info("List of vehicles added successfully");
                    return new ResponseEntity<>(vehicles, HttpStatus.OK);

                } else if (vehicles.isPresent() && !vehicles.get().isEmpty() && vehicles.get().size() < vehicleDTOList.size()) {

                    log.info("List of vehicles added partially, some vehicles were not added");
                    return new ResponseEntity<>(vehicles, HttpStatus.PARTIAL_CONTENT);

                } else {

                    log.info("List of vehicles was not added");
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            });
        }
    }

    // Endpoint para añadir marcas a la base de datos.
    @PostMapping(value = "/add_brands", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CompletableFuture<ResponseEntity<Optional<List<BrandDTO>>>> addBrand(@RequestBody List<BrandDTO> brandDTOList) {

        log.info(ACCESS_MESSAGE);

        if (brandDTOList == null || brandDTOList.isEmpty()) {

            log.error("Not brand to add. Brand list is empty");
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        } else {
            CompletableFuture<Optional<List<BrandDTO>>> addedBrands = addVehicleBrandService.addBrands(brandDTOList);

            /*  ".ThenApply" es un método de CompletableFuture que toma una función y la aplica al resultado cuando está disponible.    */
            return addedBrands.thenApply(brands -> {

                if (brands.isPresent() && brands.get().size() == brandDTOList.size()) {

                    log.info("List of brands added successfully");
                    return new ResponseEntity<>(brands, HttpStatus.OK);

                } else if (brands.isPresent() && !brands.get().isEmpty() && brands.get().size() < brandDTOList.size()) {

                    log.info("List of brands added partially, some brands were not added");
                    return new ResponseEntity<>(brands, HttpStatus.PARTIAL_CONTENT);

                } else {

                    log.info("List of brands was not added");
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            });
        }
    }

    // Endpoint para actualizar vehiculos en la base de datos.
    @PutMapping(value = "/update_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Optional<VehicleDTO>> updateVehicle(@RequestBody VehicleDTO vehicle) {

        log.info(ACCESS_MESSAGE);

        if (vehicle == null || vehicle.getId() == null || vehicle.getId().isEmpty()) {

            log.error(WRONG_VEHICLE_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {
            Optional<VehicleDTO> updatedVehicle = updateVehicleBrandService.updateVehicle(vehicle);

            if (updatedVehicle.isEmpty()) {

                log.info("Vehicle not updated");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            log.info("Vehicle updated successfully");
            return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
        }
    }

    // Endpoint para actualizar marcas en la base de datos.
    @PutMapping(value = "/update_brand", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Optional<BrandDTO>> updateBrand(@RequestBody BrandDTO brandDTO) {

        log.info(ACCESS_MESSAGE);

        if (brandDTO == null || brandDTO.getId() == null || brandDTO.getId().isEmpty()) {

            log.error(WRONG_BRAND_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {
            Optional<BrandDTO> updatedBrand = updateVehicleBrandService.updateBrand(brandDTO);

            if (updatedBrand.isEmpty()) {

                log.info("Brand not updated");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            log.info("Brand updated successfully");
            return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
        }
    }

    // Endpoint para eliminar vehiculos de la base de datos.
    @DeleteMapping(value = "/delete_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Optional<VehicleDTO>> deleteVehicleById(@RequestBody VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {

        log.info(ACCESS_MESSAGE);

        if (vehicleOrBrandIdRequest == null || vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {

            log.error(WRONG_VEHICLE_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {
            Optional<VehicleDTO> deletedVehicle = deleteVehicleBrandService.deleteVehicleById(vehicleOrBrandIdRequest.getId());

            if (deletedVehicle.isEmpty()) {

                log.info("Vehicle not deleted");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(deletedVehicle, HttpStatus.OK);
        }
    }

    // Endpoint para eliminar marcas de la base de datos.
    @DeleteMapping(value = "/delete_brand", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Optional<BrandDTO>> deleteBrandById(@RequestBody VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {

        log.info(ACCESS_MESSAGE);

        if (vehicleOrBrandIdRequest == null || vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {

            log.error(WRONG_BRAND_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {
            Optional<BrandDTO> deletedBrand = deleteVehicleBrandService.deleteBrandById(vehicleOrBrandIdRequest.getId());

            if (deletedBrand.isEmpty()) {

                log.info("Vehicle not deleted");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(deletedBrand, HttpStatus.OK);
        }
    }
}
