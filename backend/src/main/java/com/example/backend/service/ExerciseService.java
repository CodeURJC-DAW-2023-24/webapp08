package com.example.backend.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.backend.model.Exercise;

@Service
public class ExerciseService {

    private ConcurrentMap<Long, Exercise> exercises = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    public ExerciseService(){
        
    }

    public Collection<Exercise>  findAll(){
        return exercises.values();
    }
    
}
