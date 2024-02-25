package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Imagen;
import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ImagenRepositorio;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.Repositorio;


@Service
public class DatabaseInitializer {


	@Autowired
	private Repositorio userRepository;
	@Autowired
	private ImagenRepositorio imagenRepository;
	@Autowired
	private ImagenService imagenService;

	@Autowired
	 private NovedadRepository novedadRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() throws SerialException, SQLException {
		//Sample novedades
		
		for (int i=0; i< 22; i++){
		novedadRepository.save(new Novedad("Descripción: "+ i));
		}
		
		// Sample users

		userRepository.save(new Usuario("user", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));
		userRepository.save(new Usuario("admin", passwordEncoder.encode("adminpass"), "paco","1993-04-06",90,"USER", "ADMIN"));
		userRepository.save(new Usuario("1", passwordEncoder.encode("1"),"paco","1993-04-06",90,"USER"));

		String rutaCarpetaImagenes = "images";

        // Lógica para cargar las imágenes al arrancar la aplicación
        imagenService.cargarImagenesDesdeCarpeta(rutaCarpetaImagenes);
		List<Imagen> imagenes = imagenRepository.findAll();
		imagenService.guardarImagenes(imagenes);
	}

}