package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Person;
import com.example.backend.service.PersonService;

@RestController
public class RESTPersonController {
    @Autowired
    private PersonService personService;



    @GetMapping("/api/persons/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {

        Person person = personService.findById(id);

		if (person != null) {
			return ResponseEntity.ok(person);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
 }




