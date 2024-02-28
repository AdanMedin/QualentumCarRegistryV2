package com.aalonso.carregistry.services.interfaces;

import com.aalonso.carregistry.dto.UserDTO;
import com.aalonso.carregistry.dto.VehicleDTO;
import com.aalonso.carregistry.persistence.entity.User;
import com.aalonso.carregistry.persistence.entity.Vehicle;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileService {
    List<VehicleDTO> uploadVehiclesCsv(MultipartFile file);
    Optional<UserDTO> uploadUserImage(String id, MultipartFile file);
    Optional<byte[]> downloadUserImage(String id);
    Optional<?> downloadVehiclesFile();
}
