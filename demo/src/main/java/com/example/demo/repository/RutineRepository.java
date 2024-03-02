package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Rutine;
import java.util.Optional;


public interface RutineRepository extends JpaRepository<Rutine, Long> {
    
    Optional<Rutine>  findById(Long id);
}
