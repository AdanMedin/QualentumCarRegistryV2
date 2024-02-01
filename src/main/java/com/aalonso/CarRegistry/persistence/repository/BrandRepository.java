package com.aalonso.CarRegistry.persistence.repository;

import com.aalonso.CarRegistry.persistence.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
}
