package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Rutine;
import java.util.Optional;


public interface RutineRepository extends JpaRepository<Rutine, Long> {
    
    Optional<Rutine>  findById(Long id);
}
