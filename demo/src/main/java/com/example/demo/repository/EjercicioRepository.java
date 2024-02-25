package com.example.demo.repository;

import java.util.Optional;
import com.example.demo.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    Optional<Ejercicio> findByName(String name);
    Optional<Ejercicio> findByGrp(String grp);
}
