package com.example.backend.service;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;
import com.example.backend.repository.NewsRepository;
import com.example.backend.repository.PersonRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private NewsRepository newsRepository;

	public void increaseFreq(Person user, String grp, String name) {
		Map<String, Integer> frequencies = user.getFrecuencia(grp);
		frequencies.put(name, frequencies.getOrDefault(name, 0) + 1);
		personRepository.save(user);
	}

	public List<Exercise> order(Person user, String grp, List<Exercise> exercises) {
		Map<String, Integer> frequencies = user.getFrecuencia(grp);
		exercises.sort(Comparator
				.comparingInt(ejercicio -> frequencies.getOrDefault(((Exercise) ejercicio).getName(), 0)).reversed());
		return exercises;

	}

	public void deleteNewsById(List<News> newsToDelete) {
		for (News news : newsToDelete) {
			List<Person> peoples = personRepository.findByNews(news);
			for (Person person : peoples) {
				person.getNews().remove(news);
				personRepository.save(person);
			}
			newsRepository.delete(news);
		}

	}

	public Person findById(long id) {
		return personRepository.findById(id).orElseThrow();
	}

	public Person findByAlias(String alias) {
		return personRepository.findByalias(alias).orElseThrow();
	}

	public void save(Person person) {
		personRepository.save(person);

	}

	public List<String> findAliasofFriendsByPerson(Person person) {
		return personRepository.findaliasOfFriendsByPerson(person);
	}

	public Page<News> findNews(Person person, int iteracion) {
		return personRepository.findByNews(person.getNews(), PageRequest.of(iteracion, 10));

	}

	public void deletePerson(Person person) {
		List<Rutine> lrutines = person.getRutines();
		for (Rutine rutine : lrutines) {
			List<Person> lpersons = person.getFriends();
			Optional<List<News>> lnews = newsRepository.findByRutine(rutine);
			for (Person friend : lpersons) {
				if (lnews.isPresent()) {
					List<News> newsList = lnews.get();

					for (News news : newsList) {
						friend.getNews().remove(news);
					}
					friend.getFriends().remove(person);
					personRepository.save(friend);
				}
			}
			if (lnews.isPresent()) {
				List<News> newsList = lnews.get();
				
				for (News news : newsList) {
					newsRepository.delete(news);
				}
			}
		}
		List<Person> lAmigos = person.getFriends();
		for (Person amigo : lAmigos) {
			amigo.getFriends().remove(person);
			personRepository.save(amigo);
		}

		person.getFriends().clear();
		personRepository.save(person);
		personRepository.deleteById(person.getId());
	}

	public Person findPersonByHttpRequest(HttpServletRequest request){
		//return findByAlias(request.getUserPrincipal().getName());
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			return findByAlias(principal.getName());
		} else {
			
			return null; 
		}
	}

	public Optional<Person> findByRutineId(Long id){
		return personRepository.findByRutineId(id);
	}

	public List<String[]> getIdandAlias(String name, Long id){
		return personRepository.getIdandAlias(name, id);
	}

	
}
