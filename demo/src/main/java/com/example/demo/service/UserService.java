package com.example.demo.service;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Ejercicio;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
    @Autowired
	private PasswordEncoder passwordEncoder;

	public Optional<Usuario> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Usuario> findAll() {
		return repository.findAll();
	}

	public void save(String firstName,  String password, String name, String date, Integer weight) {

		repository.save(new Usuario(firstName, password,name, date, weight, "USER"));
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
	public void aumentarFrecuencia(Usuario usuario, String grupo, String name) {
        Map<String, Integer> frecuencias = usuario.getFrecuencia(grupo);
		frecuencias.put(name, frecuencias.getOrDefault(name, 0) + 1);
		repository.save(usuario);
	}
	public List<Ejercicio> ordenar(Usuario usuario, String grupo,List<Ejercicio> ejercicios) {
		Map<String, Integer> frecuencias = usuario.getFrecuencia(grupo);
		ejercicios.sort(Comparator.comparingInt(ejercicio ->
		frecuencias.getOrDefault(((Ejercicio) ejercicio).getName(), 0)
		).reversed());
		return ejercicios;

	}
}
