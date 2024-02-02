package com.aalonso.CarRegistry.controller;

import com.aalonso.CarRegistry.dto.BrandDTO;
import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.dto.VehicleOrBrandIdRequest;
import com.aalonso.CarRegistry.services.AddVehicleBrandService;
import com.aalonso.CarRegistry.services.DeleteVehicleBrandService;
import com.aalonso.CarRegistry.services.ShowVehicleBrandService;
import com.aalonso.CarRegistry.services.UpdateVehicleBrandService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@Validated
@RequestMapping(value = "/car_registry")
public class CarRegistryController {
    @PostConstruct
    public void init() {
        log.info("CarRegistryController is operational...");
    }

    @Autowired
    ShowVehicleBrandService showVehicleBrandService;
    @Autowired
    AddVehicleBrandService addVehicleBrandService;
    @Autowired
    DeleteVehicleBrandService deleteVehicleBrandService;
    @Autowired
    UpdateVehicleBrandService updateVehicleBrandService;


    @GetMapping(value = "/show_all_vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<List<VehicleDTO>>> showAllVehicles() {
        log.info("Accessed car registry controller...");
        Optional<List<VehicleDTO>> vehicles = showVehicleBrandService.showAllVehicles();
        if (vehicles.isEmpty()) {
            log.info("Not vehicles found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @GetMapping(value = "/show_vehicle_by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> showVehicleById(VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error("No vehicle id was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<VehicleDTO> vehicle = showVehicleBrandService.showVehicleById(vehicleOrBrandIdRequest.getId());
            if (vehicle.isEmpty()) {
                log.info("Vehicle not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/show_all_brands", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<List<BrandDTO>>> showAllBrands() {
        log.info("Accessed car registry controller...");
        Optional<List<BrandDTO>> brandList = showVehicleBrandService.showAllBrands();
        if (brandList.isEmpty()) {
            log.info("Not brands found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(brandList, HttpStatus.OK);
    }

    @GetMapping(value = "/show_brand_by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<BrandDTO>> showBrandById(VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error("Not brand id was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<BrandDTO> vehicle = showVehicleBrandService.showBrandById(vehicleOrBrandIdRequest.getId());
            if (vehicle.isEmpty()) {
                log.info("Brand not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/add_vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<List<VehicleDTO>>> addVehicle(@RequestBody List<VehicleDTO> vehicles) {
        log.info("Accessed car registry controller...");

        if (vehicles == null || vehicles.isEmpty()) {
            log.error("Not vehicles to add. Vehicle list is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<List<VehicleDTO>> addedVehicles = addVehicleBrandService.addVehicles(vehicles);
            if (addedVehicles.isPresent() && addedVehicles.get().size() == vehicles.size()) {
                log.info("List of vehicles added successfully");
                return new ResponseEntity<>(addedVehicles, HttpStatus.OK);
            } else if (addedVehicles.isPresent() && !addedVehicles.get().isEmpty() && addedVehicles.get().size() < vehicles.size()) {
                log.info("List of vehicles added partially, some vehicles were not added");
                return new ResponseEntity<>(addedVehicles, HttpStatus.PARTIAL_CONTENT);
            } else {
                log.info("List of vehicles was not added");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }

    @PostMapping(value = "/add_brands", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<List<BrandDTO>>> addBrand(@RequestBody List<BrandDTO> brandDtoList) {
        log.info("Accessed car registry controller...");

        if (brandDtoList == null || brandDtoList.isEmpty()) {
            log.error("Not brand to add. Brand list is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<List<BrandDTO>> addedBrands = addVehicleBrandService.addBrands(brandDtoList);
            if (addedBrands.isPresent() && addedBrands.get().size() == brandDtoList.size()) {
                log.info("List of brands added successfully");
                return new ResponseEntity<>(addedBrands, HttpStatus.OK);
            } else if (addedBrands.isPresent() && !addedBrands.get().isEmpty() && addedBrands.get().size() < brandDtoList.size()) {
                log.info("List of brands added partially, some brands were not added");
                return new ResponseEntity<>(addedBrands, HttpStatus.PARTIAL_CONTENT);
            } else {
                log.info("List of brands was not added");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }

    @PutMapping(value = "/update_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> updateVehicle(@RequestBody VehicleDTO vehicle) {
        log.info("Accessed car registry controller...");
        if (vehicle.getId() == null || vehicle.getId().isEmpty()) {
            log.error("Not vehicle id was provided");
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

    @PutMapping(value = "/update_brand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<BrandDTO>> updateBrand(@RequestBody BrandDTO brandDTO) {
        log.info("Accessed car registry controller...");
        if (brandDTO.getId() == null || brandDTO.getId().isEmpty()) {
            log.error("Not brand id was provided");
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

    @DeleteMapping(value = "/delete_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> deleteVehicleById(@RequestBody VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error("Not vehicle id was provided");
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

    @DeleteMapping(value = "/delete_brand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<BrandDTO>> deleteBrandById(@RequestBody VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error("Not brand id was provided");
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
