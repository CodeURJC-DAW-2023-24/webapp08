package com.example.demo.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.Ejercicio;
import com.example.demo.repository.EjercicioRepo;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepo repository;

    public Optional<Ejercicio> findById(long id) {
        return repository.findById(id);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Ejercicio> findAll() {
        return repository.findAll();
    }

    public void save(String name,  String date, Integer time) {

        repository.save(new Ejercicio(date, name, time));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
