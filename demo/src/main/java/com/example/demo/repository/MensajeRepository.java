package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    
}

