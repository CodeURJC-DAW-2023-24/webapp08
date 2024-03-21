package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Rutine;
import com.example.backend.repository.RutineRepository;

@Service
public class RutineService {
    @Autowired
    private RutineRepository repository;  

    public RutineService(){

    } 
    public Optional<Rutine> findById(long id){
        return repository.findById(id);
    }

    public List<Rutine> findAll(){
        return repository.findAll();
    }

    public void save(Rutine rutine){
        repository.save(rutine);
    }
    public void delete(Rutine rutine){
        repository.delete(rutine);
    }
    
    public Optional<Rutine> findByExerciseId(Long id){
        return repository.findByExerciseId(id);
    }
}
