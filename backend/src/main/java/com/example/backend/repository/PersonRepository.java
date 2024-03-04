package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByalias(String name);

    @Query("SELECT u.id, u.alias FROM Person u WHERE u.alias LIKE %:name% AND  u.id <> :idUser AND u.id NOT IN (SELECT a.id FROM Person u JOIN u.friends a WHERE u.id = :idUser)")
    List<String[]> getIdandAlias(@Param("name") String nombre, @Param("idUser") Long idUser);

    @Query("SELECT friend.alias FROM Person person JOIN person.friends friend WHERE person = :person")
    List<String> findaliasOfFriendsByPerson(Person person);


    @Query("SELECT u FROM Person u JOIN u.rutines r WHERE r.id = :rutineId")
    Optional<Person> findByRutineId(@Param("rutineId") Long rutineId);


    @Query("SELECT n FROM Person u JOIN u.news n WHERE n IN :news")
    Page<News> findByNews(List<News> news, Pageable page);
}
