package com.example.demo.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
}
