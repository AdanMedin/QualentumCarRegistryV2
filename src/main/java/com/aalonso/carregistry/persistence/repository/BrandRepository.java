package com.aalonso.carregistry.persistence.repository;

import com.aalonso.carregistry.persistence.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    @Query("SELECT b FROM Brand b WHERE b.name = ?1")
    Optional<Brand> findByName(@Param("name") String name);
}
