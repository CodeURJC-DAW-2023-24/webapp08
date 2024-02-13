package com.example.demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.*;

import org.springframework.boot.CommandLineRunner;




@Controller
public class UsuarioController implements CommandLineRunner{

     @Autowired
	 private Repositorio repository;

	 @Override
	 public void run(String... args) throws Exception {
 
		 // save a couple of Usuarios
		 repository.save(new Usuario("Jack", "Bauer"));
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
		 List<Usuario> bauers = repository.findByLastName("Bauer");
		 System.out.println("Usuario found with findByLastName('Bauer'):");
		 System.out.println("--------------------------------------------");
		 for (Usuario bauer : bauers) {
			 System.out.println(bauer);
		 }
 
		 repository.delete(bauers.get(0));
	 }
    /*@PostMapping ("/guardaranuncio")
	public String guardarAnuncio(Model model, @RequestParam String nombre, @RequestParam String asunto,
			@RequestParam String comentario) {

		model.addAttribute("nombre", nombre);
		model.addAttribute("asunto", asunto);
		model.addAttribute("comentario", comentario);

		return "anuncio";
	}*/
	

}
