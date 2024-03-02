package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Ejercicio;
import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;

import java.util.List;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirstName(String name);

    @Query("SELECT u.id, u.firstName FROM Usuario u WHERE u.firstName LIKE %:nombre% AND  u.id <> :idUser AND u.id NOT IN (SELECT a.id FROM Usuario u JOIN u.amigos a WHERE u.id = :idUser)")
    List<String[]> getIdandFirstName(@Param("nombre") String nombre, @Param("idUser") Long idUser);

    @Query("SELECT amigo.firstName FROM Usuario usuario JOIN usuario.amigos amigo WHERE usuario = :usuario")
    List<String> findFirstNameOfAmigosByUsuario(Usuario usuario);


    @Query("SELECT u FROM Usuario u JOIN u.rutinas r WHERE r.id = :rutinaId")
    Optional<Usuario> findByRutinaId(@Param("rutinaId") Long rutinaId);


    @Query("SELECT n FROM Usuario u JOIN u.novedades n WHERE n IN :novedades")
    Page<Novedad> findByNovedades(List<Novedad> novedades, Pageable page);
}
