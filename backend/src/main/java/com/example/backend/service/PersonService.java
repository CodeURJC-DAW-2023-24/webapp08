package com.example.backend.service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Exercise;
import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;
  

	public void increaseFreq(Person user, String grp, String name) {
        Map<String, Integer> frequencies = user.getFrecuencia(grp);
		frequencies.put(name, frequencies.getOrDefault(name, 0) + 1);
		repository.save(user);
	}
	public List<Exercise> order(Person user, String grp,List<Exercise> exercises) {
		Map<String, Integer> frequencies = user.getFrecuencia(grp);
		exercises.sort(Comparator.comparingInt(ejercicio ->
		frequencies.getOrDefault(((Exercise) ejercicio).getName(), 0)
		).reversed());
		return exercises;

	}
}
