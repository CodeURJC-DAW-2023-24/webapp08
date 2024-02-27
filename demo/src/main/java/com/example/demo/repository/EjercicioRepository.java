package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Ejercicio;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    Optional<Ejercicio> findByName(String name);
    Optional<Ejercicio> findById(long id);
    Page<Ejercicio> findByGrp(String grp, Pageable page);
    List<Ejercicio> findByGrp(String grp);
    @Query("SELECT e.name FROM Ejercicio e WHERE e.name LIKE %:nombre%")
    List<String[]> getNames(@Param("nombre") String nombre);

}
