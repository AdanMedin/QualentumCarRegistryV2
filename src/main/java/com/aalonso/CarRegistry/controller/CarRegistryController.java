package com.aalonso.CarRegistry.controller;

import com.aalonso.CarRegistry.dto.VehicleDTO;
import com.aalonso.CarRegistry.dto.VehicleIdRequest;
import com.aalonso.CarRegistry.services.AddVehicleService;
import com.aalonso.CarRegistry.services.DeleteVehicleService;
import com.aalonso.CarRegistry.services.ShowVehicleService;
import com.aalonso.CarRegistry.services.UpdateVehicleService;
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
    ShowVehicleService showVehicleService;
    @Autowired
    AddVehicleService addVehicleService;
    @Autowired
    DeleteVehicleService deleteVehicleService;
    @Autowired
    UpdateVehicleService updateVehicleService;


    @GetMapping(value = "/show_all_vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<List<VehicleDTO>>> showAllVehicles() {
        log.info("Accessed car registry controller...");
        Optional<List<VehicleDTO>> vehicles = showVehicleService.showAllVehicles();
        if (vehicles.isEmpty()) {
            log.info("Not vehicles found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @GetMapping(value = "/show_vehicle_by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> showAllVehicleById(VehicleIdRequest vehicleIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleIdRequest.getId() == null || vehicleIdRequest.getId().isEmpty()) {
            log.error("No vehicle id was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<VehicleDTO> vehicle = showVehicleService.showVehicleById(vehicleIdRequest.getId());
            if (vehicle.isEmpty()) {
                log.info("Vehicle not found");
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
            log.error("No vehicles to add the vehicle list is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<List<VehicleDTO>> addedVehicles = addVehicleService.addVehicles(vehicles);
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

    @PutMapping(value = "/update_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> updateVehicle(@RequestBody VehicleDTO vehicle) {
        log.info("Accessed car registry controller...");
        if (vehicle.getId() == null || vehicle.getId().isEmpty()) {
            log.error("No vehicle id was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<VehicleDTO> updatedVehicle = updateVehicleService.updateVehicle(vehicle);
            if (updatedVehicle.isEmpty()) {
                log.info("Vehicle not updated");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Vehicle updated: {}", updatedVehicle);
            return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete_vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Optional<VehicleDTO>> deleteVehicleById(@RequestBody VehicleIdRequest vehicleIdRequest) {
        log.info("Accessed car registry controller...");
        if (vehicleIdRequest.getId() == null || vehicleIdRequest.getId().isEmpty()) {
            log.error("No vehicle id was provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Optional<VehicleDTO> deletedVehicle = deleteVehicleService.deleteVehicleById(vehicleIdRequest.getId());
            if (deletedVehicle.isEmpty()) {
                log.info("Vehicle not deleted");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(deletedVehicle, HttpStatus.OK);
        }
    }
}
