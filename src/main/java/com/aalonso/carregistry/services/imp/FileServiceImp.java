package com.aalonso.carregistry.services.imp;

import com.aalonso.carregistry.dto.BrandDTO;
import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.Brand;
import com.aalonso.carregistry.persistence.entity.User;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import com.aalonso.carregistry.persistence.repository.BrandRepository;
import com.aalonso.carregistry.persistence.repository.UserRepository;
import com.aalonso.carregistry.persistence.repository.VehicleRepository;
import com.aalonso.carregistry.services.interfaces.FileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImp implements FileService {

    @PostConstruct
    public void init() {
        log.info("FileService is operational...");
    }

    private final UserRepository userRepository;

    private final BrandRepository brandRepository;

    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    private final List<String> headers = List.of("brand_id", "model", "mileage", "price",
            "year", "description", "colour", "fuel_type", "num_doors");

    @Override
    public Optional<UserDTO> uploadUserImage(String id, MultipartFile imageFile) {

        Optional<User> userEntity = userRepository.findById(id);

        AtomicReference<User> savedUser = new AtomicReference<>(new User());

        userEntity.ifPresentOrElse(
                user -> {
                    log.info("Preparing file to upload: {} ......", imageFile.getOriginalFilename());

                    try {
                        user.setImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
                        savedUser.set(userRepository.save(user));

                    } catch (IOException e) {
                        log.error("Error encoding user image", e);
                    }
                },
                () -> log.error("User with id: " + id + " does not exist")
        );

        // Si userEntity contiene un User, se devuelve un Optional<UserDTO>.
        // Si userEntity está vacío, se devuelve un Optional vacío.
        return savedUser.get() != null ? Optional.of(modelMapper.map(savedUser.get(), UserDTO.class)) : Optional.empty();
    }

    @Override
    public Optional<byte[]> downloadUserImage(String id) {
        Optional<User> userEntity = userRepository.findById(id);

        return userEntity.map(user -> Base64.getDecoder().decode(user.getImage()));
    }

    @Override
    public List<VehicleDTO> uploadVehiclesCsv(MultipartFile file) {

        List<VehicleDTO> vehicleList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(bufferedReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                Optional<Brand> brand = brandRepository.findById(csvRecord.get(headers.get(0)));

                brand.ifPresentOrElse(
                        value -> {
                            VehicleDTO vehicleDTO = new VehicleDTO();

                            vehicleDTO.setBrand(modelMapper.map(value, BrandDTO.class));
                            vehicleDTO.setModel(csvRecord.get(headers.get(1)));
                            vehicleDTO.setMileage(Integer.parseInt(csvRecord.get(headers.get(2))));
                            vehicleDTO.setPrice(Double.parseDouble(csvRecord.get(headers.get(3))));
                            vehicleDTO.setYear(Integer.parseInt(csvRecord.get(headers.get(4))));
                            vehicleDTO.setDescription(csvRecord.get(headers.get(5)));
                            vehicleDTO.setColour(csvRecord.get(headers.get(6)));
                            vehicleDTO.setFuelType(csvRecord.get(headers.get(7)));
                            vehicleDTO.setNumDoors(Integer.parseInt(csvRecord.get(headers.get(8))));

                            vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
                            vehicleList.add(vehicleDTO);
                        },
                        () -> {
                            log.error("Brand with id: " + csvRecord.get(headers.get(0)) + " does not exist");
                            log.error("Vehicle model: " + csvRecord.get(headers.get(1)) + " could not be saved");
                        }
                );
            }
        } catch (Exception e) {
            log.error("Error reading vehicles file", e);
        }
        return vehicleList;
    }


    @Override
    public Optional<?> downloadVehiclesFile() {
        return Optional.empty();
    }

}
