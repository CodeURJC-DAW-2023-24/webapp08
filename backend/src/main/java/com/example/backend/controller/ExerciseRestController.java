package com.example.backend.controller;

import com.example.backend.repository.ExerciseRepository;
import com.example.backend.service.PictureService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Exercise;
import com.example.backend.model.Picture;


@RestController
@RequestMapping("api/exercises")
public class ExerciseRestController {

    @Autowired
    private ExerciseRepository exerciseRepository;

	
	@GetMapping("/")
	public List<Exercise> getExercises() {
		return exerciseRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Exercise> getExercise(@PathVariable long id) {

		Optional exerciseOptional = exerciseRepository.findById(id);

		if (exerciseOptional.isPresent()) {
			Exercise exercise = (Exercise) exerciseOptional.get();
			return ResponseEntity.ok(exercise);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/groups/{group}")
	public List<Exercise> getExercisesByGroup(@PathVariable String group) {
		return exerciseRepository.findByGrp(group);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Exercise> deleteExercise(@PathVariable long id) {

		try {
			exerciseRepository.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);

		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Exercise createBook(@RequestBody Exercise exercise) {

		exerciseRepository.save(exercise);

		return exercise;
	}
	@PutMapping("/{id}")
	public ResponseEntity<Exercise> updateBook(@PathVariable long id, @RequestBody Exercise updatedExercise) throws SQLException {
		Optional exerciseOptional = exerciseRepository.findById(id);
		if (exerciseOptional.isPresent()) {

			updatedExercise.setId(id);
			exerciseRepository.save(updatedExercise);

			return new ResponseEntity<>(updatedExercise, HttpStatus.OK);
		} else	{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}

