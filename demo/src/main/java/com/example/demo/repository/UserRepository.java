package com.example.demo.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Usuario;


import java.util.List;


public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirstName(String name);
   
    @Query("SELECT u.id, u.firstName FROM Usuario u WHERE u.firstName LIKE %:nombre%")
    List<String[]> findByFirstNameContaining(@Param("nombre") String nombre);


    //List<Usuario> findByFirstNameContaining(String nombre);

    
       
    
    
}


