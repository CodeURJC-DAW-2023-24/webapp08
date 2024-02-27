package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Ejercicio;
import com.example.demo.model.Imagen;
import com.example.demo.model.Novedad;
import com.example.demo.model.Rutina;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EjercicioRepository;
import com.example.demo.repository.NotificacionRepository;
import com.example.demo.repository.ImagenRepositorio;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.repository.UserRepository;

@Service
public class DatabaseInitializer {

		
	@Autowired
		private EjercicioRepository ejRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImagenRepositorio imagenRepository;
	@Autowired
	private ImagenService imagenService;

	@Autowired
	 private NovedadRepository novedadRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	 @Autowired
	 private NotificacionRepository notificacionRepository;

	 @Autowired
	 private RutinaRepository rutinaRepository;
	
	@SuppressWarnings("null")
	@PostConstruct
	public void init() throws SerialException, SQLException {
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

		//Sample ejercicios
		ejRepository.save(new Ejercicio("Curl de bíceps con mancuerna de pie", "Para realizar el curl de bíceps", "Biceps", "https://www.youtube.com/embed/rqy0oxx__sU?si=8JkDYGNHXgpVg3NB"));
		ejRepository.save(new Ejercicio("Press de banca con agarre cerrado", "El press de banca con agarre cerrado es una variación del clásico press de banca que se enfoca más en el trabajo de los tríceps, aunque también involucra los pectorales y los deltoides frontales. ", "Triceps", "https://www.youtube.com/embed/SF0uoT4JWNw?si=1cSxJLlUKBy9N6rD"));
		ejRepository.save(new Ejercicio("Jalón al pecho con agarre supino", "Ejercicio de entrenamiento de fuerza que se enfoca en el desarrollo de la musculatura de la espalda, especialmente los músculos del dorsal ancho, los trapecios y los romboides.", "Espalda", "https://www.youtube.com/embed/SnLxcN1x3LU?si=INQ8p5xUHCFKEuHQ"));
		ejRepository.save(new Ejercicio("Curl1", "curl", "Pecho", "0"));
		ejRepository.save(new Ejercicio("Curl2", "curl", "Cardio", "0"));
		ejRepository.save(new Ejercicio("Curl4", "curl", "Inferior", "0"));
		ejRepository.save(new Ejercicio("Curl3", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl5", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl6", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl7", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl8", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl9", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl10", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl11", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl12", "curl", "Hombro", "0"));
		ejRepository.save(new Ejercicio("Curl13", "curl", "Hombro", "0"));
	


		/*String rutaCarpetaImagenes = "images";

        // Lógica para cargar las imágenes al arrancar la aplicación
        imagenService.cargarImagenesDesdeCarpeta(rutaCarpetaImagenes);
		List<Imagen> imagenes = imagenRepository.findAll();
		imagenService.guardarImagenes(imagenes);*/
	}

}