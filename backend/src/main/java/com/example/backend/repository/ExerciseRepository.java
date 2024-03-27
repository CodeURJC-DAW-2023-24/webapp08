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
   /**  @Query("SELECT e " +
    "FROM Exercise e " +
    "LEFT JOIN Person p ON e.name = KEY(p.chestFrec) AND p.id = :userId  " +
    "WHERE (p.id = :userId OR p.id IS NULL)" +
    "GROUP BY e " +
    "ORDER BY COUNT(p.chestFrec[e.name]) DESC")*/
    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Pecho' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseChestOrderByFrec(@Param("userId") Long userId, Pageable page);
   

    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Espalda' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseBackOrderByFrec(@Param("userId") Long userId, Pageable page);
   

    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Hombro' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseShoulderOrderByFrec(@Param("userId") Long userId, Pageable page);
   


    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Biceps' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseBicepsOrderByFrec(@Param("userId") Long userId, Pageable page);
   

    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Triceps' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseTricepsOrderByFrec(@Param("userId") Long userId, Pageable page);
   

    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Inferior' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseLowerOrderByFrec(@Param("userId") Long userId, Pageable page);
   

    @Query("SELECT e, (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.grp = 'Cardio' " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p.chestFrec[e.name]) FROM Person p WHERE p.id = :userId) DESC")
    Page<Exercise> findExerciseCardioOrderByFrec(@Param("userId") Long userId, Pageable page);
   

   
   
   
   
    /*@Query("SELECT e, (SELECT COUNT(p." + ":frecMap[e.name]) FROM Person p WHERE p.id = :userId) " +
            "FROM Exercise e " +
            "WHERE e.groupMuscular = :groupMuscular " +
            "GROUP BY e " +
            "ORDER BY (SELECT COUNT(p." + ":frecMap[e.name]) FROM Person p WHERE p.id = :userId) DESC")
            Page<Exercise> findExerciseOrdenadosPorFrecuencia(
        @Param("userId") Long userId, 
        @Param("groupMuscular") String groupMuscular,
        @Param("frecMap") String frecMap, 
        Pageable page
    );*/

    
}
