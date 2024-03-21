package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Rutine;
import java.util.Optional;


public interface RutineRepository extends JpaRepository<Rutine, Long> {
    
    Optional<Rutine>  findById(Long id);
    @Query("SELECT r FROM Rutine r JOIN r.exercises e WHERE e.id = :exerciseId")
 Optional<Rutine> findByExerciseId(@Param("exerciseId") Long exerciseId);
}
