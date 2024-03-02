package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Imagen;

public interface ImagenRepositorio extends JpaRepository<Imagen, Long> {

    Optional<Imagen> findByName(String name);
}
