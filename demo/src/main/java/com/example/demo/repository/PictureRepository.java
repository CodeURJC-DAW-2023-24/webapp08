package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByName(String name);
}
