package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.UserRepository;

@Service
public class DatabaseInitializer {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	 private NovedadRepository novedadRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {
		//Sample novedades
		
		for (int i=0; i< 22; i++){
		novedadRepository.save(new Novedad("Descripción: "+ i));
		}
		
		// Sample users

		userRepository.save(new Usuario("user", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));
		userRepository.save(new Usuario("user2", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));

		userRepository.save(new Usuario("admin", passwordEncoder.encode("adminpass"), "paco","1993-04-06",90,"USER", "ADMIN"));
		userRepository.save(new Usuario("1", passwordEncoder.encode("1"),"paco","1993-04-06",90,"USER"));
		userRepository.save(new Usuario("2", passwordEncoder.encode("2"),"paco2","1993-04-06",90,"USER"));
		userRepository.save(new Usuario("3", passwordEncoder.encode("3"),"paco3","1993-04-06",90,"USER"));


	}

}