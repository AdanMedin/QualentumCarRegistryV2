package com.aalonso.carregistry.persistence.repository;

import com.aalonso.carregistry.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<UserDetails> findByEmail(String email);
    Optional<User> findById(String id);
}
