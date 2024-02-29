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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    private final List<String> header = List.of("brand_id", "model", "mileage", "price",
            "year", "description", "colour", "fuel_type", "num_doors");

    @Override
    public Optional<UserDTO> uploadUserImage(String id, MultipartFile imageFile) {
        log.info("Accessed user image management service (Upload)...");

        Optional<User> userEntity = userRepository.findById(id);

        //  Se crea un AtomicReference para almacenar el usuario guardado y poder modificarlo dentro del if.
        AtomicReference<User> savedUser = new AtomicReference<>(new User());

        userEntity.ifPresentOrElse(
                user -> {
                    log.info("Preparing file to upload: {} ......", imageFile.getOriginalFilename());

                    try {
                        user.setImage(imageFile.getBytes());
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
        log.info("Accessed user image management service (Download)...");
        Optional<User> userEntity = userRepository.findById(id);

        return userEntity.map(User::getImage);
    }

    @Override
    public List<VehicleDTO> uploadVehiclesCsv(MultipartFile file) {
        log.info("Accessed vehicles csv upload service...");

        List<VehicleDTO> vehicleList = new ArrayList<>();


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)); // Se crea un bufferedReader para leer el archivo.
             CSVParser csvParser = new CSVParser(bufferedReader, // Se crea un csvParser para parsear el archivo.
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) { // Se configura el formato del archivo.

            Iterable<CSVRecord> csvRecords = csvParser.getRecords(); // Se crea un iterable de CSVRecords para recorrer el archivo.

            for (CSVRecord csvRecord : csvRecords) {

                Optional<Brand> brand = brandRepository.findById(csvRecord.get(header.get(0)));

                brand.ifPresentOrElse(
                        value -> {
                            // Se crea un VehicleDTO y se mapea el BrandDTO con el valor de la marca y se añaden los demás campos.
                            VehicleDTO vehicleDTO = new VehicleDTO();

                            vehicleDTO.setBrand(modelMapper.map(value, BrandDTO.class));
                            vehicleDTO.setModel(csvRecord.get(header.get(1)));
                            vehicleDTO.setMileage(Integer.parseInt(csvRecord.get(header.get(2))));
                            vehicleDTO.setPrice(Double.parseDouble(csvRecord.get(header.get(3))));
                            vehicleDTO.setYear(Integer.parseInt(csvRecord.get(header.get(4))));
                            vehicleDTO.setDescription(csvRecord.get(header.get(5)));
                            vehicleDTO.setColour(csvRecord.get(header.get(6)));
                            vehicleDTO.setFuelType(csvRecord.get(header.get(7)));
                            vehicleDTO.setNumDoors(Integer.parseInt(csvRecord.get(header.get(8))));

                            // Se guarda el VehicleDTO en la base de datos y se añade a la lista de vehículos añadidos.
                            vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
                            vehicleList.add(vehicleDTO);
                        },
                        () -> {
                            log.error("Brand with id: " + csvRecord.get(header.get(0)) + " does not exist");
                            log.error("Vehicle model: " + csvRecord.get(header.get(1)) + " could not be saved");
                        }
                );
            }
        } catch (Exception e) {
            log.error("Error reading vehicles file", e);
        }
        return vehicleList;
    }

    @Override
    public Optional<byte[]> downloadVehiclesExcel() {
        log.info("Accessed vehicles excel download service...");

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) { // Se crea un workbook y un ByteArrayOutputStream para escribir el archivo.
            Sheet sheet = workbook.createSheet("Vehicles"); // Se crea una hoja en el workbook.

            // Se crea la cabecera del excel.
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Brand", "Model", "Mileage", "Price", "Year", "Description", "Colour", "Fuel Type", "Num Doors"};

            // Se añaden los headers al excel.
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Se obtienen los vehículos de la base de datos.
            List<Vehicle> vehicles = vehicleRepository.findAll();
            int rowNum = 1;

            // Se añaden al excel.
            for (Vehicle vehicle : vehicles) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vehicle.getBrand().getName());
                row.createCell(1).setCellValue(vehicle.getModel());
                row.createCell(2).setCellValue(vehicle.getMileage());
                row.createCell(3).setCellValue(vehicle.getPrice());
                row.createCell(4).setCellValue(vehicle.getYear());
                row.createCell(5).setCellValue(vehicle.getDescription());
                row.createCell(6).setCellValue(vehicle.getColour());
                row.createCell(7).setCellValue(vehicle.getFuelType());
                row.createCell(8).setCellValue(vehicle.getNumDoors());
            }

            // Se escribe el archivo.
            workbook.write(out);
            return Optional.of(out.toByteArray());

        } catch (IOException e) {
            log.error("Error creating vehicles file", e);
            return Optional.empty();
        }
    }
}
