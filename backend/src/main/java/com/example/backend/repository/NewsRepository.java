package com.example.backend.repository;

import java.util.Optional;
import java.util.*;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.News;
import com.example.backend.model.Rutine;

public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<List<News>> findByRutine(Rutine rutine);
    List<News> findByRutineId(Long rutineId);
     
  
}
