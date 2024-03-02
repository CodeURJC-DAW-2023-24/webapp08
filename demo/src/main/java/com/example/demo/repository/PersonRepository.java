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

    @Query("SELECT u.id, u.firstName FROM Person u WHERE u.firstName LIKE %:name% AND  u.id <> :idUser AND u.id NOT IN (SELECT a.id FROM Person u JOIN u.friends a WHERE u.id = :idUser)")
    List<String[]> getIdandFirstName(@Param("name") String nombre, @Param("idUser") Long idUser);

    @Query("SELECT friend.firstName FROM Person person JOIN person.friends friend WHERE person = :person")
    List<String> findFirstNameOfFriendsByPerson(Person person);


    @Query("SELECT u FROM Person u JOIN u.rutines r WHERE r.id = :rutineId")
    Optional<Person> findByRutineId(@Param("rutineId") Long rutineId);


    @Query("SELECT n FROM Person u JOIN u.news n WHERE n IN :news")
    Page<News> findByNews(List<News> news, Pageable page);
}
