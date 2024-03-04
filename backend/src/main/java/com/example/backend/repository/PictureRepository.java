package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByName(String name);
}
