package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Novedad;
import com.example.demo.model.Rutina;

public interface NovedadRepository extends JpaRepository<Novedad, Long> {
    Optional<Novedad> findByrutina(Rutina rutina);
     
}
