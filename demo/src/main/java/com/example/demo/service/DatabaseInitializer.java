package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.Repositorio;

@Service
public class DatabaseInitializer {


	@Autowired
	private Repositorio userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {

		
		// Sample users

		userRepository.save(new Usuario("user", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));
		userRepository.save(new Usuario("admin", passwordEncoder.encode("adminpass"), "paco","1993-04-06",90,"USER", "ADMIN"));
	}

}