package com.example.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.model.Usuario;
import com.example.demo.repository.Repositorio;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.CommandLineRunner;




@Controller
public class UsuarioController implements CommandLineRunner{

     @Autowired
	 private Repositorio repository;

	 @Override
	 public void run(String... args) throws Exception {
 
		 // save a couple of Usuarios
		 repository.save(new Usuario("1", "1"));
		 repository.save(new Usuario("Chloe", "O'Brian"));
		 repository.save(new Usuario("Kim", "Bauer"));
		 repository.save(new Usuario("David", "Palmer"));
		 repository.save(new Usuario("Michelle", "Dessler"));
 
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
	 /*@GetMapping("/")
	public String index() {
		return "index.html";
	}*/
 
@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf"); 
	    model.addAttribute("token", token.getToken());   
		
		return "mainPage.html";
	}
/** 
    @GetMapping ("/iniciarSesion")
	public String iniciarSesion(Model model, @RequestParam String user, @RequestParam String password) {

		model.addAttribute("nombre", user);
		
		List<Usuario>  usuarios = repository.findByFirstName(user);
		for (Usuario usuario: usuarios) {
			if (usuario.getLastName().contentEquals(password)){
				return "mainPage.html";
			}
		}

		

		return "index.html";
	}
	**/

}
