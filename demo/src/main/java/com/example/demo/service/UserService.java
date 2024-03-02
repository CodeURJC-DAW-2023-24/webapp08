package com.example.demo.service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exercise;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;

@Service
public class UserService {

	@Autowired
	private PersonRepository repository;
  

	public Optional<Person> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Person> findAll() {
		return repository.findAll();
	}

	public void save(String firstName,  String password, String name, String date, Integer weight) {

		repository.save(new Person(firstName, password, name, date, weight, "USER"));
	}

	public void delete(long id) {
		repository.deleteById(id);
	}

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
