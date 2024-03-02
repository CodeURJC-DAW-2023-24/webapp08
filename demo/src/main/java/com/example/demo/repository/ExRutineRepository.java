package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ExRutine;
import java.util.Optional;


public interface ExRutineRepository extends JpaRepository<ExRutine, Long> {    
    Optional<ExRutine>  findById(Long id);
}