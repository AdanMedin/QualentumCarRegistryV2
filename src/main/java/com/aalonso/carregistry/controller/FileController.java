package com.aalonso.carregistry.controller;

import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.dto.VehicleOrBrandIdRequest;
import com.aalonso.carregistry.services.interfaces.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/car_registry")
public class FileController {

    private static final String WRONG_ID_MESSAGE = "Id was not provided";

    private final FileService fileService;

    @PostMapping(value = "/userImage/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadUserImage(@RequestParam(value = "id") String id, @RequestParam(value = "imageFile") MultipartFile imageFile) {

        if (imageFile == null) {
            log.error("Image not provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!Objects.requireNonNull(imageFile.getOriginalFilename()).contains(".png") && !imageFile.getOriginalFilename().contains(".jpg") && !imageFile.getOriginalFilename().contains(".jpeg")){
            log.error("Image format not supported");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null || id.isEmpty()) {
            log.error(WRONG_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<UserDTO> userDTO = fileService.uploadUserImage(id, imageFile);

        if (userDTO.isEmpty()) {
            log.error("User with id: " + id + " does not exist or image could not be saved.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("File uploaded: {}", imageFile.getOriginalFilename());

        return ResponseEntity.ok("Image successfully uploaded.");
    }

    @GetMapping(value = "/userImage/download", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> downloadUserImage(VehicleOrBrandIdRequest vehicleOrBrandIdRequest) {

        if (vehicleOrBrandIdRequest == null || vehicleOrBrandIdRequest.getId() == null || vehicleOrBrandIdRequest.getId().isEmpty()) {
            log.error(WRONG_ID_MESSAGE);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<byte[]> response = fileService.downloadUserImage(vehicleOrBrandIdRequest.getId());

        if (response.isEmpty()) {
            log.error("User with id: " + vehicleOrBrandIdRequest.getId() + " does not exist or has no image");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Image downloaded for user with id: {}", vehicleOrBrandIdRequest.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(response.get(), headers, HttpStatus.OK);
    }


    @PostMapping(value = "/vehiclesData/uploadCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVehiclesCsv(@RequestParam(value = "file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            log.error("File is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (Objects.requireNonNull(file.getOriginalFilename()).contains(".csv")) {

            log.info("Csv name: {}", file.getOriginalFilename());

            List<VehicleDTO> vehiclesAdded = fileService.uploadVehiclesCsv(file);

            if (vehiclesAdded.isEmpty()) {
                log.error("Error uploading csv");
                log.error("No vehicles added");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            log.info("Vehicles uploaded: {}", vehiclesAdded.size());

            return ResponseEntity.ok("Csv successfully uploaded");
        }

        log.error("File name: {}", file.getOriginalFilename());

        return new ResponseEntity<>("File format not supported",HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/vehiclesData_download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<String> downloadVehiclesFile() {

        log.info("File {} downloaded", "file.txt");

        return ResponseEntity.ok("User image successfully downloaded.");
    }
}
