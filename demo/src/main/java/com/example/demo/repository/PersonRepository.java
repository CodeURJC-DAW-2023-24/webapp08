package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Exercise;
import com.example.demo.model.News;
import com.example.demo.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByFirstName(String name);

    @Query("SELECT u.id, u.firstName FROM Person u WHERE u.firstName LIKE %:name% AND  u.id <> :idUser AND u.id NOT IN (SELECT a.id FROM Person u JOIN u.amigos a WHERE u.id = :idUser)")
    List<String[]> getIdandFirstName(@Param("name") String nombre, @Param("idUser") Long idUser);

    @Query("SELECT amigo.firstName FROM Person usuario JOIN usuario.amigos amigo WHERE usuario = :usuario")
    List<String> findFirstNameOfAmigosByUsuario(Person usuario);


    @Query("SELECT u FROM Person u JOIN u.rutinas r WHERE r.id = :rutinaId")
    Optional<Person> findByRutinaId(@Param("rutinaId") Long rutinaId);


    @Query("SELECT n FROM Person u JOIN u.novedades n WHERE n IN :novedades")
    Page<News> findByNovedades(List<News> novedades, Pageable page);
}
