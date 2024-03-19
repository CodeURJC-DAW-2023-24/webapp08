package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.DTO.PersonDTO;
import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;
import com.example.backend.service.PersonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/persons")
public class RESTPersonController {
    @Autowired
    private PersonService personService;




    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable long id) {
		try {
			Person person = personService.findById(id);
			PersonDTO personDTO =new PersonDTO(person, personService);
		return ResponseEntity.ok(personDTO);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}		
	}

	@PostMapping("/")
	public String createPerson(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	

	
 }




