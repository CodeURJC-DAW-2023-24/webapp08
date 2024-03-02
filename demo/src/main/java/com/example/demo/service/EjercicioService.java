package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Ejercicio;
import com.example.demo.repository.EjercicioRepository;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository repository;

    public Optional<Ejercicio> findById(long id) {
        return repository.findById(id);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Ejercicio> findAll() {
        return repository.findAll();
    }

    public void save(String name, String description, String grp, String video) {

        repository.save(new Ejercicio(name, description, grp, video));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
