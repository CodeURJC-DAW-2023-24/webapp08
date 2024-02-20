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
import com.example.demo.repository.Repositorio;

@Service
public class DatabaseInitializer {


	@Autowired
	private Repositorio userRepository;

	@Autowired
	 private NovedadRepository novedadRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {
		//Sample novedades
		
		for (int i=0; i< 12; i++){
		novedadRepository.save(new Novedad("Ruben"+ i));
		System.out.println(i);
		System.out.println(novedadRepository.findById(1L).get());
		}
		
		Page<Novedad> pagina = novedadRepository.findAll(PageRequest.of(0,10));
		System.out.println(pagina);
		System.out.println(pagina.getContent());
	
		
		// Sample users

		userRepository.save(new Usuario("user", passwordEncoder.encode("pass"), "USER"));
		userRepository.save(new Usuario("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
	}

}