package com.example.backend.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.model.Picture;

import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController implements CommandLineRunner {

	@Autowired
	private PersonService personService;

	@Autowired
	private PictureService imageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
	}

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {

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

	@GetMapping("/mainPage")
	public String mainPage(Model model, HttpServletRequest request) {
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		model.addAttribute("search", false);
		return "mainPage";
	}

	@SuppressWarnings("finally")
	@PostMapping("/register")
	public String register(@RequestParam("name") String name,
			@RequestParam("alias") String alias,
			@RequestParam("date") String date,
			@RequestParam("weight") Integer weight,
			@RequestParam("password") String password,
			@RequestParam("password1") String password1,
			@RequestParam("image") MultipartFile imagenFile, HttpSession session, Model model) {

		try {
			personService.findByAlias(alias);
			model.addAttribute("message", true);
			// existing user
			model.addAttribute("erroMg", "El user ya existe");
			return "error";
		} catch(Exception  e) {

			if (!password.equals(password1)) {
				// same passwords
				model.addAttribute("message", true);
				model.addAttribute("erroMg", "Las contraseñas no coinciden");
				return "error";
			}

			String pass = passwordEncoder.encode(password);
			Person user = new Person(alias, pass, name, date, weight, "USER");
			if (!imagenFile.isEmpty()) {
				try {
					// byte[] convert
					byte[] imageData = imagenFile.getBytes();

					// object Image
					Picture image = new Picture(null);
					image.setContent(imagenFile.getContentType());
					image.setName(imagenFile.getOriginalFilename());
					image.setData(imageData);
					user.setImage(image);
					imageService.savePicture(image);
				} catch (IOException e2) {
				}
			}
			personService.save(user);
			return "index";
		}
	}

	@GetMapping("/newUser")
	public String newUser(Model model) {
		model.addAttribute("search", false);
		return "register";
	}

	@GetMapping("/mainPage/person")
	public String privatePage(Model model, HttpServletRequest request) throws InterruptedException {
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		Picture image = user.getImage();
		String imagePath = "logo.jpg";
		if (!(image == null)) {
			imagePath = image.getName();
		}
		model.addAttribute("alias", user.getAlias());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("search", false);

		return "person";
	}

	@PostMapping("/mainPage/person/config")
	public String editUser(Model model, @RequestParam String name, @RequestParam String alias,
			@RequestParam String date, @RequestParam Integer weight,
			@RequestParam MultipartFile image, HttpServletRequest request) throws InterruptedException {
		String alias2 = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias2);
		user.setName(name);
		user.setAlias(alias);
		user.setDate(date);
		user.setWeight(weight);
		if (!image.isEmpty()) {
			try {
				// byte[] convert
				byte[] imageData = image.getBytes();

				// object Image
				Picture imageF = new Picture(null);
				imageF.setContent(image.getContentType());
				imageF.setName(image.getOriginalFilename());
				imageF.setData(imageData);

				imageService.savePicture(imageF);
				Thread.sleep(1000);

				user.setImage(imageF);
			} catch (IOException e) {
			}
		}
		personService.save(user);
		Picture imageN = user.getImage();
		String imagePath = "logo.jpg";
		if (!(imageN == null)) {
			imagePath = imageN.getName();

		}
		model.addAttribute("alias", user.getAlias());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));

		return "person";
	}

}
