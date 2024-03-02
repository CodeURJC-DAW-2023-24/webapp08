package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Rutina;
import java.util.Optional;


public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    
    Optional<Rutina>  findById(Long id);
}
