package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import com.example.backend.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
    Optional<Exercise> findById(long id);
    Page<Exercise> findByGrp(String grp, Pageable page);
    List<Exercise> findByGrp(String grp);
    @Query("SELECT e.name FROM Exercise e WHERE e.name LIKE %:name%")
    List<String[]> getNames(@Param("name") String name);
    List<Exercise> findAll();

}
