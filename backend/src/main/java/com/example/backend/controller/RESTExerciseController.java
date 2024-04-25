package com.example.backend.controller;

import com.example.backend.service.ExerciseService;
import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
import com.example.backend.model.Person;
import com.example.backend.model.Picture;

@RestController
@RequestMapping("api/exercises")
public class RESTExerciseController {

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private PersonService personService;

	@Operation(summary = "Get exercise list ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	@GetMapping("/")
	public Page<Exercise> getExercises(Pageable page) {
		page = PageRequest.of(page.getPageNumber(), 5);
		return exerciseService.findAll(page);
	}

	@Operation(summary = "Get an exercise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the exercise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "404", description = "Exercise not found", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Exercise> getExercise(@PathVariable long id) {

		Optional exerciseOptional = exerciseService.findById(id);

		if (exerciseOptional.isPresent()) {
			Exercise exercise = (Exercise) exerciseOptional.get();
			return ResponseEntity.ok(exercise);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Get exercise list by group in pages ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	@GetMapping("/group/")
	public Page<Exercise> getExercisesByGroup(String group, int page, HttpServletRequest request) {
		Person person = personService.findPersonByHttpRequest(request);
		Pageable pageable = PageRequest.of(page, 5);

		if (person == null) {
			return exerciseService.findByGrp(group, pageable);
		} else {
			return exerciseService.findExerciseOrderByFrec(person.getId(), group, pageable);
		}

	}

	@Operation(summary = "Get exercise list by group ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
	})
	@GetMapping("/group/{group}")
	public List<Exercise> getExercisesGroup(@PathVariable String group, HttpServletRequest request) {
		Person person = personService.findPersonByHttpRequest(request);

		if (person == null) {
			return exerciseService.findByGrp(group);
		} else {
			return exerciseService.findExerciseOrderByFrec(person.getId(), group);
		}

	}

	@Operation(summary = "Get image by exercise id ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found image", content = {
					@Content(mediaType = "image/jpeg") }),
			@ApiResponse(responseCode = "404", description = "Exercise image is empty or exercise doesn't exit", content = @Content),
	})
	@GetMapping("/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable long id) throws IOException {
		Optional exerciseOptional = exerciseService.findById(id);
		if (exerciseOptional.isPresent()) {
			Exercise exercise = (Exercise) exerciseOptional.get();
			if (!(exercise.getImage() == null)) {
				Picture picture = exercise.getImage();
				byte[] imageData = picture.getData();
				return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@Operation(summary = "Delete image by exercise id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Exercise has been deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "That exercise doesn't exist", content = @Content),
			@ApiResponse(responseCode = "403", description = "You are not logged", content = @Content),
	})
	@DeleteMapping("/image/{id}")
	public ResponseEntity<Object> deleteImage(@PathVariable long id) {
		Optional exerciseOptional = exerciseService.findById(id);
		if (exerciseOptional.isPresent()) {
			Exercise exercise = (Exercise) exerciseOptional.get();
			if (!(exercise.getImage() == null)) {
				Picture picture = exercise.getImage();
				exerciseService.deleteImage(exercise);
				pictureService.delete(picture);
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		}

		else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Delete exercise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Exercise has been deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "That exercise doesn't exist", content = @Content),
			@ApiResponse(responseCode = "403", description = "You are not logged", content = @Content),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Exercise> deleteExercise(@PathVariable long id) {

		try {
			exerciseService.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);

		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Create new exercise by logged admin")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "New exercise created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "403", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "409", description = "Exercise already exists", content = @Content),
	})
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createExercise(@RequestBody Exercise exercise) {

		Exercise existingExercise = exerciseService.SearchByName(exercise.getName());
		if (existingExercise != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Exercise already exists");
		} else {
			exerciseService.save(exercise);
			URI location = fromCurrentRequest().path("{id}").buildAndExpand(exercise.getId()).toUri();
			return ResponseEntity.created(location).body(exercise);
		}

	}

	private void print(String name) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'print'");
	}

	@Operation(summary = "Create new image by logged admin")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "New image created", content = {
					@Content(mediaType = "image/jpeg") }),
			@ApiResponse(responseCode = "403", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "404", description = "That exercise doesn't exist", content = @Content),
	})
	@PostMapping("/image/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createImage(@PathVariable long id, @RequestParam MultipartFile image) {
		Optional exerciseOptional = exerciseService.findById(id);
		URI location = fromCurrentRequest().build().toUri();
		if (exerciseOptional.isPresent()) {
			Exercise exercise = (Exercise) exerciseOptional.get();
			if (!image.isEmpty()) {
				try {
					Picture imageN = pictureService.newPicture(image);
					exerciseService.setImage(exercise, imageN);
					return ResponseEntity.created(location).build();
				} catch (IOException e) {
				}
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return null;
	}

	@Operation(summary = "Edit exercise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Exercise has been edited", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))
			}),
			@ApiResponse(responseCode = "404", description = "That exercise doesn't exist", content = @Content),
			@ApiResponse(responseCode = "403", description = "You are not logged", content = @Content),
	})
	@PutMapping("/{id}")
	public ResponseEntity<Exercise> updateExercise(@PathVariable long id, @RequestBody Exercise updatedExercise)
			throws SQLException {
		Optional exerciseOptional = exerciseService.findById(id);
		if (exerciseOptional.isPresent()) {

			updatedExercise.setId(id);
			exerciseService.save(updatedExercise);

			return new ResponseEntity<>(updatedExercise, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/names")
	public ResponseEntity<?> searcExs(@RequestParam("nameEx") String name) {
		List<String[]> lName= exerciseService.getNames(name);

		return ResponseEntity.ok().body(lName);
	}
	@GetMapping("/namesEx")
	public ResponseEntity<?> searchExByName(@RequestParam("nameEx") String name) {
		Exercise exer= exerciseService.SearchByName(name);

		return ResponseEntity.ok().body(exer);
	}


}
