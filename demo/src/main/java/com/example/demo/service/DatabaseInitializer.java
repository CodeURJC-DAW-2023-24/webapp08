package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exercise;
import com.example.demo.model.Person;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.PersonRepository;

@Service
public class DatabaseInitializer {

		
	@Autowired
		private ExerciseRepository ejRepository;

	@Autowired
	private PersonRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@SuppressWarnings("null")
	@PostConstruct
	public void init() throws SerialException, SQLException {
		userRepository.save(new Person("user", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));
		userRepository.save(new Person("user2", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));

		userRepository.save(new Person("admin", passwordEncoder.encode("adminpass"), "paco","1993-04-06",90,"USER", "ADMIN"));
		userRepository.save(new Person("1", passwordEncoder.encode("1"),"paco","1993-04-06",90,"USER"));
		userRepository.save(new Person("2", passwordEncoder.encode("2"),"paco2","1993-04-06",90,"USER"));
		userRepository.save(new Person("3", passwordEncoder.encode("3"),"paco3","1993-04-06",90,"USER"));

		ejRepository.save(new Exercise("Curl de bíceps con mancuerna de pie", "Para realizar el curl de bíceps", "Biceps", "https://www.youtube.com/embed/rqy0oxx__sU?si=8JkDYGNHXgpVg3NB"));
		ejRepository.save(new Exercise("Press de banca con agarre cerrado", "El press de banca con agarre cerrado es una variación del clásico press de banca que se enfoca más en el trabajo de los tríceps, aunque también involucra los pectorales y los deltoides frontales. ", "Triceps", "https://www.youtube.com/embed/SF0uoT4JWNw?si=1cSxJLlUKBy9N6rD"));
		ejRepository.save(new Exercise("Jalón al pecho con agarre supino", "Ejercicio de entrenamiento de fuerza que se enfoca en el desarrollo de la musculatura de la espalda, especialmente los músculos del dorsal ancho, los trapecios y los romboides.", "Espalda", "https://www.youtube.com/embed/SnLxcN1x3LU?si=INQ8p5xUHCFKEuHQ"));
		ejRepository.save(new Exercise("Curl1", "curl", "Pecho", "0"));
		ejRepository.save(new Exercise("Curl2", "curl", "Cardio", "0"));
		ejRepository.save(new Exercise("Curl2", "curl", "Pecho", "0"));
		ejRepository.save(new Exercise("Curl1", "curl", "Espalda", "0"));
		ejRepository.save(new Exercise("Curl1", "curl", "Triceps", "0"));
		ejRepository.save(new Exercise("Curl1", "curl", "Biceps", "0"));
		ejRepository.save(new Exercise("Curl2", "curl", "Cardio", "0"));
		ejRepository.save(new Exercise("Curl4", "curl", "Inferior", "0"));
		ejRepository.save(new Exercise("Curl1", "curl", "Inferior", "0"));
		ejRepository.save(new Exercise("Curl3", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl5", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl6", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl7", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl8", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl9", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl10", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl11", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl12", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl13", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl9", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl10", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl11", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl12", "curl", "Hombro", "0"));
		ejRepository.save(new Exercise("Curl13", "curl", "Hombro", "0"));
	}

}