package com.example.demo.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Novedad;
import com.example.demo.model.Usuario;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.Repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
public class UsuarioController implements CommandLineRunner {

	@Autowired
	private Repositorio repository;
	@Autowired
	private PasswordEncoder passwordEncoder;

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
	

	@GetMapping("/novedades-iniciales")
	public @ResponseBody List<Object> getNovedades(@RequestParam  int iteracion) {
		Page<Novedad> pagina= novedadRepository.findAll(PageRequest.of(iteracion,10));
		List<Novedad> listPaginas = pagina.getContent();
		long numPaginas = novedadRepository.count(); 
		List<Object> data =new ArrayList<>(Arrays.asList(listPaginas, numPaginas));
	
		return data; //Devuelve un list<novedad>
	}
	
}
