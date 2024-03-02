package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.EjerRutina;
import java.util.Optional;


public interface EjerRutinaRepository extends JpaRepository<EjerRutina, Long> {    
    Optional<EjerRutina>  findById(Long id);
}