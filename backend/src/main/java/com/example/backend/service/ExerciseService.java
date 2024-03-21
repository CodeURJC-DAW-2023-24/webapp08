package com.example.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.backend.model.Exercise;
import com.example.backend.model.Picture;
import com.example.backend.repository.ExerciseRepository;


@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository repository;    

    public ExerciseService(){
        
    }
    public void save(Exercise exercise) {
        repository.save(exercise);
    }
    public Optional<Exercise> findByName(String name){
        return repository.findByName(name);
    }

    public Optional<Exercise> findById(long id){
        return repository.findById(id);
    }
    public  Page<Exercise> findByGrp(String grp, Pageable page){
        return repository.findByGrp(grp,page);
    }
    public  List<Exercise> findByGrp(String grp){
        return repository.findByGrp(grp);
    }
    public List<String[]> getNames(@Param("name") String name){
        return repository.getNames(name);

    }
    public List<Exercise> findAll(){
        return repository.findAll();
    }
    public Page<Exercise> findAll(Pageable page){
        return repository.findAll(page);
    }
    public void deleteById(Long id){
        repository.deleteById(id);
    }
    public void deleteImage(Exercise exercise){
        exercise.setImage(null);
        exercise.setbImage(false);
        save(exercise); 

    }
    public void setImage(Exercise exercise, Picture image){
        
					exercise.setImage(image);
					exercise.setbImage(true);
					save(exercise);
    }
}

