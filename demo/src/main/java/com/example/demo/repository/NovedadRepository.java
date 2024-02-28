package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;

public interface NovedadRepository extends JpaRepository<Novedad, Long> {
     
}
