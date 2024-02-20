package com.example.demo.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.model.Usuario;
import com.example.demo.repository.Repositorio;


import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.CommandLineRunner;

@Controller
public class UsuarioController implements CommandLineRunner {

	@Autowired
	private Repositorio repository;
	 @Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		// save a couple of Usuarios
		repository.save(new Usuario("1", passwordEncoder.encode("1"),"paco","1993-04-06",90,"USER"));
		

		// fetch all Usuarios
		List<Usuario> Usuarios = repository.findAll();
		System.out.println("Usuarios found with findAll():");
		System.out.println("-------------------------------");
		for (Usuario usuario : Usuarios) {
			System.out.println(usuario);
		}
		System.out.println();

		// fetch an individual Usuario by ID
		Usuario usuario = repository.findById(1L).get();
		System.out.println("Usuario found with findOne(1L):");
		System.out.println("--------------------------------");
		System.out.println(usuario);
		System.out.println();

		// fetch Usuarios by last name

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


	@GetMapping("/login")
	public String login() {
		return "index";
	}
	

	@GetMapping("/newUser")
	public String newUser() {
		return "register";
	}
	@GetMapping("/user")
	public String privatePage(Model model, HttpServletRequest request) {

		String name = request.getUserPrincipal().getName();
		
		Usuario user = repository.findByFirstName(name).orElseThrow();

		model.addAttribute("firstName", user.getFirstName());	
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());	
		model.addAttribute("weight", user.getWeight());
		

		return "user";
	}
	@GetMapping("/")
	public String main() {
		return "mainPage";
	}
	

	/*@GetMapping("/")
	public String login2(Model model, HttpServletRequest request) {

		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		model.addAttribute("token", token.getToken());

		return "index";
	}
	/**
	 * @GetMapping ("/iniciarSesion")
	 *             public String iniciarSesion(Model model, @RequestParam String
	 *             user, @RequestParam String password) {
	 * 
	 *             model.addAttribute("nombre", user);
	 * 
	 *             List<Usuario> usuarios = repository.findByFirstName(user);
	 *             for (Usuario usuario: usuarios) {
	 *             if (usuario.getLastName().contentEquals(password)){
	 *             return "mainPage.html";
	 *             }
	 *             }
	 * 
	 * 
	 * 
	 *             return "index.html";
	 *             }
	 **/

}
