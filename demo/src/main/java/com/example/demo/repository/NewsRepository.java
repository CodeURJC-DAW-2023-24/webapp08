package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.News;
import com.example.demo.model.Rutine;

public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findByrutina(Rutine rutina);
     
}
