package com.example.demo.repository;

import java.util.Optional;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Usuario;


import java.util.List;


public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirstName(String name);
   
    @Query("SELECT u.id, u.firstName FROM Usuario u WHERE u.firstName LIKE %:nombre% AND  u.id <> :idUser AND u.id NOT IN (SELECT a.id FROM Usuario u JOIN u.amigos a WHERE u.id = :idUser)")
    List<String[]> getIdandFirstName(@Param("nombre") String nombre,@Param("idUser") Long idUser);


    @Query("SELECT amigo.firstName FROM Usuario usuario JOIN usuario.amigos amigo WHERE usuario = :usuario")
    List<String> findFirstNameOfAmigosByUsuario(Usuario usuario);

    
       
    
    
}


