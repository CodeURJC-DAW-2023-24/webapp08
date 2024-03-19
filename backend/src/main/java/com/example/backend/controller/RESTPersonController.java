package com.example.backend.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.PersonDTO;
import com.example.backend.model.News;
import com.example.backend.model.Notification;
import com.example.backend.model.Person;
import com.example.backend.model.Picture;
import com.example.backend.model.Rutine;
import com.example.backend.repository.PersonRepository;
import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/persons")
public class RESTPersonController {
	@Autowired
	private PersonService personService;

	@Autowired
	private PictureService pictureService;

	@GetMapping("/{id}")
	public ResponseEntity<PersonDTO> getPerson(@PathVariable long id) {
		try {
			Person person = personService.findById(id);
			PersonDTO personDTO = new PersonDTO(person, personService);
			return ResponseEntity.ok(personDTO);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		personService.save(person);
		URI location = fromCurrentRequest().path("{id}").buildAndExpand(person.getId()).toUri();

		return ResponseEntity.created(location).body(person);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> editPerson(@PathVariable Long id,
			@RequestParam(required = false) String alias,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) Integer weight,
			@RequestParam(required = false) MultipartFile image) {

		Person person;
		try {
			person = personService.findById(id);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
		try {
			if (alias != null)
				person.setAlias(alias);
			if (name != null)
				person.setName(name);
			if (date != null)
				person.setDate(date);

			if (weight != null)
				person.setWeight(weight);

			if (image != null) {
				byte[] imageData = image.getBytes();

				// object Image
				Picture imageF = new Picture(null);
				imageF.setContent(image.getContentType());
				imageF.setName(image.getOriginalFilename());
				imageF.setData(imageData);

				pictureService.savePicture(imageF);
				Thread.sleep(1000);

				person.setImage(imageF);
			}
			personService.save(person);
			PersonDTO personDTO = new PersonDTO(person, personService);
			return ResponseEntity.ok(personDTO);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@DeleteMapping("/{id}") //TO DO/////////////
	public ResponseEntity<PersonDTO> deletePerson(@PathVariable long id) {
		try {
			Person person = personService.findById(id);
			PersonDTO personDTO = new PersonDTO(person, personService);
			return ResponseEntity.ok(personDTO);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{id}/newFriend/{friendId}") ///TO DO///
	public ResponseEntity<?> addFriend(@PathVariable long id, @PathVariable long friendId ) {
		Person person = personService.findById(id);
		//TODO: process POST request
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}/rutines")
	public ResponseEntity<List<Rutine>> getPersonRutines(@PathVariable long id){
		try {
			Person person = personService.findById(id);
			return ResponseEntity.ok(person.getRutines());
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}/news") //Added page parameter (check it)
	public ResponseEntity<List<News>> getNews(@PathVariable long id,@RequestParam int iteracion){
		try {
			Person person = personService.findById(id);
			Page<News> pNews = personService.findNews(person, iteracion);
			List<News> page = pNews.getContent();
			return ResponseEntity.ok(page);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}/notifications")
	public ResponseEntity<List<Notification>> getNotifications(@PathVariable long id){
		try {
			Person person = personService.findById(id);
			return ResponseEntity.ok(person.getlNotifications());
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	
}
