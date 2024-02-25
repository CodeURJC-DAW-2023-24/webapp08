package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;

import com.example.demo.service.ImagenService;
import com.example.demo.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Imagen;
import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.Repositorio;

import java.util.ArrayList;
import java.util.Arrays;import io.micrometer.common.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController implements CommandLineRunner {

	@Autowired
	private Repositorio repository;
	@Autowired
	private ImagenService imagenService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService service;
	@Autowired
	 private NovedadRepository novedadRepository;

	@Override
	public void run(String... args) throws Exception {
	}

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if(principal != null) {
		
			model.addAttribute("logged", true);		
			model.addAttribute("userName", principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			
		} else {
			model.addAttribute("logged", false);
		}
	}


	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	

	@GetMapping ("/error")
	public String error(){
		return "error";
	}
	

	@GetMapping ("/main")
	public String main(Model model){
		model.addAttribute("search", false);
		return "mainPage";
	}

	@PostMapping("/register")
	public String register(@RequestParam("name") String name,
	@RequestParam("firstName") String firstName,
	@RequestParam("date") String date,
	@RequestParam("weight") Integer weight,
	@RequestParam("password") String password,
	@RequestParam("password1") String password1, 
	@RequestParam("image") MultipartFile imagenFile,HttpSession session, Model model) {
		Optional<Usuario> existingUserOptional = repository.findByFirstName(firstName);
		if (name.isEmpty() || firstName.isEmpty() || date.isEmpty() || password.isEmpty() || password1.isEmpty()) {
			model.addAttribute("erroMg", "Rellene todos los campos");
			return "error";
		}

		if (!password.equals(password1)) {
			// Manejar el error de contraseñas que no coinciden
			model.addAttribute("erroMg", "Las contraseñas no coinciden");
			return "error";
		}

		if (existingUserOptional.isPresent()) {
			// Si se encuentra un usuario con el mismo primer nombre, regresar un error
			model.addAttribute("erroMg", "El usuario ya existe");
			return "error";
		}	
		String pass = passwordEncoder.encode(password);
		Usuario usuario = new Usuario(name, pass,name, date,weight, "USER");
		if(!imagenFile.isEmpty()){
			try {
            // Convertir MultipartFile a byte[]
            byte[] datosImagen = imagenFile.getBytes();

            // Crear objeto Imagen
            Imagen imagen = new Imagen();
            imagen.setContenido(imagenFile.getContentType());
            imagen.setName(imagenFile.getOriginalFilename());
            imagen.setDatos(datosImagen);
			usuario.setImagen(imagen);
			}catch (IOException e) {}
		}
			repository.save(usuario);
			//service.save(firstName, pass, name, date, weight);
			//repository.save(new Usuario(name, passwordEncoder.encode(password),name, date,weight, "USER"));
		return "index";
	}
		
 @GetMapping ("/adRutine")
	public String adRutine(){
		return "adRutine";
	}
	@GetMapping("/newUser")
	public String newUser(Model model) {
		model.addAttribute("search", false);
		return "register";
	}
	@GetMapping("/user")
	public String privatePage(Model model, HttpServletRequest request) {
		model.addAttribute("search", false);
		String name = request.getUserPrincipal().getName();
		System.out.println(name);
		Usuario user = repository.findByFirstName(name).orElseThrow();
		Imagen image = user.getImagen();
		String rutaImagen = "logo.jpg";
		if(!(image == null)){
			 rutaImagen = image.getName();
			if(!imagenService.verificarExistenciaImagen(rutaImagen)){
				imagenService.guardarImagen(image);
			}
		}
		model.addAttribute("firstName", user.getFirstName());	
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());	
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("rutaImagen", rutaImagen);
		model.addAttribute("search", false);
		

		return "user";
	}

	
	
	
	@PostMapping("/editUser")
	public String editUser(Model model, @RequestParam String name, @RequestParam String firstName,@RequestParam String
	            date, @RequestParam Integer
	             weight ,
				 @RequestParam MultipartFile image,HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = repository.findByFirstName(nameUser).orElseThrow();
		usuario.setName(name);
		usuario.setFirstName(firstName);
		usuario.setDate(date);
		usuario.setWeight(weight);
		if(!image.isEmpty()){
			try {
            // Convertir MultipartFile a byte[]
            byte[] datosImagen = image.getBytes();

            // Crear objeto Imagen
            Imagen imagen = new Imagen();
            imagen.setContenido(image.getContentType());
            imagen.setName(image.getOriginalFilename());
            imagen.setDatos(datosImagen);
			usuario.setImagen(imagen);
			}catch (IOException e) {}
		}
		repository.save(usuario);
		Imagen imageN = usuario.getImagen();
		String rutaImagen = "logo.jpg";
		if(!(imageN == null)){
			 rutaImagen = imageN.getName();
			if(!imagenService.verificarExistenciaImagen(rutaImagen)){
				imagenService.guardarImagen(imageN);
			}
		}
		model.addAttribute("firstName", usuario.getFirstName());	
		model.addAttribute("name", usuario.getName());
		model.addAttribute("date", usuario.getDate());	
		model.addAttribute("weight", usuario.getWeight());
		model.addAttribute("rutaImagen", rutaImagen);
		model.addAttribute("search", false);
		
		return "user";
	}

	

	@GetMapping("/novedades-iniciales")
	public @ResponseBody List<Object> getNovedades(@RequestParam  int iteracion) {
		Page<Novedad> pagina= novedadRepository.findAll(PageRequest.of(iteracion,10));
		List<Novedad> listPaginas = pagina.getContent();
		long numPaginas = novedadRepository.count(); 
		List<Object> data =new ArrayList<>(Arrays.asList(listPaginas, numPaginas));
	
		return data; //Devuelve un list<novedad>
	}

	@GetMapping("/muscGr")
	public String muscGr(Model model) {
		model.addAttribute("search", true);
		return "muscleGroup";
	}
	
	
}
