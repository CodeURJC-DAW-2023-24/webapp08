package com.example.backend.controller;

import com.example.backend.repository.ExerciseRepository;
import com.example.backend.repository.PictureRepository;
import com.example.backend.service.PictureService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.example.backend.model.Exercise;
import com.example.backend.model.Picture;


@RestController
@RequestMapping("api/exercises")
public class ExerciseRestController {

    @Autowired
    private ExerciseRepository exerciseRepository;
	@Autowired
    private PictureRepository pictureRepository;
	@Autowired
    private PictureService pictureService;
	
	@GetMapping("/")
	public Page<Exercise> getExercises(Pageable page) {
		return exerciseRepository.findAll(page);
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
	@GetMapping("/group/")
	public Page<Exercise> getExercisesByGroup( String group, Pageable page) {
		return exerciseRepository.findByGrp(group,page);
	}

	@GetMapping("/image/")
	public ResponseEntity<byte[]> getImage(long id) throws IOException {
		Optional exerciseOptional = exerciseRepository.findById(id);
		if(exerciseOptional.isPresent()){
			Exercise exercise = (Exercise) exerciseOptional.get();
			if(!(exercise.getImage()==null)){
				Picture picture = exercise.getImage();
				byte[] imageData =picture.getData();
				return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			}

		
		else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
	}
	@DeleteMapping("/image/")
	public ResponseEntity<Object> deleteImage(long id){
		Optional exerciseOptional = exerciseRepository.findById(id);
		if(exerciseOptional.isPresent()){
			Exercise exercise = (Exercise) exerciseOptional.get();
			if(!(exercise.getImage()==null)){
				Picture picture = exercise.getImage();
				exercise.setImage(null);
				exercise.setbImage(false);
				exerciseRepository.save(exercise);
				pictureRepository.delete(picture);
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			}

		
		else{
			return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/")
	public ResponseEntity<Exercise> deleteExercise(long id) {

		try {
			exerciseRepository.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);

		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {

		exerciseRepository.save(exercise);
		URI location = fromCurrentRequest().path("{id}").buildAndExpand(exercise.getId()).toUri();

		return ResponseEntity.created(location).body(exercise);
	}
	@PostMapping("/image/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createImage(long id, @RequestParam MultipartFile image) {
		Optional exerciseOptional = exerciseRepository.findById(id);
		URI location = fromCurrentRequest().build().toUri();
		if(exerciseOptional.isPresent()){
			Exercise exercise = (Exercise) exerciseOptional.get();
			if (!image.isEmpty()) {
				try {
					byte[] datosImage = image.getBytes();

					Picture imageN = new Picture(null);
					imageN.setContent(image.getContentType());
					imageN.setName(image.getOriginalFilename());
					imageN.setData(datosImage);
				
					pictureService.savePicture(imageN);
					//Thread.sleep(1000);

					exercise.setImage(imageN);
					exercise.setbImage(true);
					exerciseRepository.save(exercise);
					return ResponseEntity.created(location).build();
				} catch (IOException e) {
				}
			}
			else{
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
        }
		else{
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return null;
	}
	

	@PutMapping("/")
	public ResponseEntity<Exercise> updateExercise( long id, @RequestBody Exercise updatedExercise) throws SQLException {
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

