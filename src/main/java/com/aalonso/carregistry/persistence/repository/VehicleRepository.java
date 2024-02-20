package com.aalonso.carregistry.persistence.repository;

import com.aalonso.carregistry.persistence.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
