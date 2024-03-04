package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.ExRutine;
import java.util.Optional;


public interface ExRutineRepository extends JpaRepository<ExRutine, Long> {    
    Optional<ExRutine>  findById(Long id);
}