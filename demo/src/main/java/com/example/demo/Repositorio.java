package com.example.demo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Repositorio extends JpaRepository<Usuario, Long> {
    List<Usuario> findByFirstName(String nombre);
    List<Usuario> findByPassword(String password);
    
}
