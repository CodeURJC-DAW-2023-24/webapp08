package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Exercise;
import com.example.demo.repository.ExerciseRepository;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository repository;

    public Optional<Exercise> findById(long id) {
        return repository.findById(id);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Exercise> findAll() {
        return repository.findAll();
    }

    public void save(String name, String description, String grp, String video) {

        repository.save(new Exercise(name, description, grp, video));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
