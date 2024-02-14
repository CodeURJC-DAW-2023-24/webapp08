package com.example.demo.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Usuario;

public interface Repositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirstName(String name);

    
}
