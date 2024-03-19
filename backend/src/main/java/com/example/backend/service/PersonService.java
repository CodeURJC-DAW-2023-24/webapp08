package com.example.backend.service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;


  

	public void increaseFreq(Person user, String grp, String name) {
        Map<String, Integer> frequencies = user.getFrecuencia(grp);
		frequencies.put(name, frequencies.getOrDefault(name, 0) + 1);
		personRepository.save(user);
	}
	public List<Exercise> order(Person user, String grp,List<Exercise> exercises) {
		Map<String, Integer> frequencies = user.getFrecuencia(grp);
		exercises.sort(Comparator.comparingInt(ejercicio ->
		frequencies.getOrDefault(((Exercise) ejercicio).getName(), 0)
		).reversed());
		return exercises;

	}
	 public void deleteNewsById(List<News> newsToDelete) {

    
            for (News news: newsToDelete) {
				List<Person> peoples = personRepository.findByNews(news);
				for(Person person: peoples){
                person.getNews().remove(newsToDelete);
                personRepository.save(person);
				}
            }
			
       
			
    }

	public Person findById(long id) {
		return personRepository.findById(id).orElseThrow();
	}

	public void save(Person person) {
		personRepository.save(person);

	}

	public List<String> findAliasofFriendsByPerson(Person person){
	return personRepository.findaliasOfFriendsByPerson(person);
	}
}
