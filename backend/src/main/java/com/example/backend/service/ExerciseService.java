package com.example.backend.service;

import java.util.List;
import java.util.Optional;


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

    public Page<Exercise> findExerciseOrderByFrec(Long id, String group,Pageable page){
        switch (group) {
			case "Pecho":
            return repository.findExerciseChestOrderByFrec( id, page);
               
            case "Espalda":
			 return repository.findExerciseBackOrderByFrec( id, page);

            case "Hombro":
			return repository.findExerciseShoulderOrderByFrec( id, page);
              
            case "Biceps":
			return repository.findExerciseBicepsOrderByFrec( id, page);
             
            case "Triceps":
			return repository.findExerciseTricepsOrderByFrec( id, page);

            case "Inferior":
			return repository.findExerciseLowerOrderByFrec( id, page);

            default:
			return repository.findExerciseCardioOrderByFrec( id, page);
               
        }
        
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
    public String getFrecString(String group){
        switch (group) {
			case "Pecho":
            return "chestFrec";
               
            case "Espalda":
			return "backFrec";

            case "Hombro":
			return "shoulderFrec";
              
            case "Biceps":
			return "bicepsFrec";
             
            case "Triceps":
			return "tricepsFrec";
          
            case "Inferior":
			return "lowerFrec";
               
            default:
			return "cardioFrec";
               
        }
    }
}

