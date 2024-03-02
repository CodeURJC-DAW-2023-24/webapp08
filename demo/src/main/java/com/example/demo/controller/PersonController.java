package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.service.ImagenService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Notification;
import com.example.demo.model.ExRutine;
import com.example.demo.model.Picture;
import com.example.demo.model.Comment;
import com.example.demo.model.News;
import com.example.demo.model.Rutine;
import com.example.demo.model.Person;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.RutineRepository;
import com.example.demo.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController implements CommandLineRunner {

	@Autowired
	private PersonRepository userRepository;

	@Autowired
	private ImagenService imageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private RutineRepository rutineRepository;

	@Autowired
	private CommentRepository messageRepository;

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

	@GetMapping("/error")
	public String error() {
		return "error";
	}

	@GetMapping("/main")
	public String main(Model model, HttpServletRequest request) {
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
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
			@RequestParam("image") MultipartFile imagenFile, HttpSession session, Model model) {
		Optional<Person> existingUserOptional = userRepository.findByFirstName(firstName);
		if (name.isEmpty() || firstName.isEmpty() || date.isEmpty() || password.isEmpty() || password1.isEmpty()) {
			model.addAttribute("message", true);
			model.addAttribute("erroMg", "Rellene todos los campos");
			return "error";
		}

		if (!password.equals(password1)) {
			//same passwords
			model.addAttribute("message", true);
			model.addAttribute("erroMg", "Las contraseñas no coinciden");
			return "error";
		}

		if (existingUserOptional.isPresent()) {
			model.addAttribute("message", true);
			// existing user
			model.addAttribute("erroMg", "El user ya existe");
			return "error";
		}
		String pass = passwordEncoder.encode(password);
		Person user = new Person(firstName, pass, name, date, weight, "USER");
		if (!imagenFile.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = imagenFile.getBytes();

				// object Image
				Picture image = new Picture(null);
				image.setContent(imagenFile.getContentType());
				image.setName(imagenFile.getOriginalFilename());
				image.setDatos(datosImagen);
				user.setImagen(image);
				imageService.guardarImagen(image);
			} catch (IOException e) {
			}
		}
		userRepository.save(user);
		return "index";
	}

	@GetMapping("/newUser")
	public String newUser(Model model) {
		model.addAttribute("search", false);
		return "register";
	}

	@GetMapping("/user")
	public String privatePage(Model model, HttpServletRequest request) throws InterruptedException {
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		String name = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(name).orElseThrow();
		Picture image = user.getImagen();
		String imagePath= "logo.jpg";
		if (!(image == null)) {
			imagePath = image.getName();
		}
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("search", false);

		return "user";
	}

	@GetMapping("/comunity")
	public String comunity(Model model) {
		model.addAttribute("search", false);
		return "comunity";
	}

	@GetMapping("/exForm")
	public String exForm() {
		return "exFormAd";
	}

	@PostMapping("/editUser")
	public String editUser(Model model, @RequestParam String name, @RequestParam String firstName,
			@RequestParam String date, @RequestParam Integer weight,
			@RequestParam MultipartFile image, HttpServletRequest request) throws InterruptedException {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		user.setName(name);
		user.setFirstName(firstName);
		user.setDate(date);
		user.setWeight(weight);
		if (!image.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = image.getBytes();

				// object Image
				Picture imageF = new Picture(null);
				imageF.setContent(image.getContentType());
				imageF.setName(image.getOriginalFilename());
				imageF.setDatos(datosImagen);
				imageService.guardarImagen(imageF);
				user.setImagen(imageF);
			} catch (IOException e) {
			}
		}
		userRepository.save(user);
		Picture imageN = user.getImagen();
		String imagePath = "logo.jpg";
		if (!(imageN == null)) {
			imagePath = imageN.getName();

		}
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("imagePath", imagePath);
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));

		return "user";
	}

	@GetMapping("/starterNews")
	public @ResponseBody List<Object> getNews(@RequestParam int iteracion, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		Page<News> pNews =userRepository.findByNews(user.getNews(),PageRequest.of(iteracion, 10));
		List<News> page = pNews.getContent();
		Boolean top = pNews.hasNext();
		List<Object> data = new ArrayList<>(Arrays.asList(page, top));

		return data; // list<news>
	}

	@GetMapping("/muscGr")
	public String muscGr(Model model, HttpServletRequest request) {
		model.addAttribute("search", true);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		return "muscleGroup";
	}

	@GetMapping("/searchUsers")
	public @ResponseBody Map<String, Object> searchbyName(@RequestParam String nombre, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		Boolean bAdmin = request.isUserInRole("ADMIN");
		List<String[]> lNameId = userRepository.getIdandFirstName(nombre, user.getId());
		Map<String, Object> response = new HashMap<>();
		response.put("lNameId", lNameId);
		response.put("bAdmin", bAdmin);

		return response;
	}

	@PostMapping("/sendRequest")
	public @ResponseBody Boolean sendRequest(@RequestParam String id, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person sender = userRepository.findByFirstName(nameUser).orElseThrow();
		Person receiver = userRepository.findById(Long.parseLong(id)).orElseThrow();
		Notification notificacion = new Notification(sender.getFirstName());
		notificationRepository.save(notificacion);
		receiver.getLNotifications().add(notificacion);
		userRepository.save(receiver);
		return true;
	}

	@PostMapping("/deleteUser")
	public @ResponseBody Boolean deleteUser(@RequestParam Long id) {
		Person user = userRepository.findById(id).orElseThrow();
		List<Rutine> lrutines = user.getRutines();
		for (Rutine rutine : lrutines) {
			List<Person> lUsers = user.getFriends();
			Optional<News> news = newsRepository.findByRutine(rutine);
			for (Person friend : lUsers) {
				if (news.isPresent()) {
					friend.getNews().remove(news.get());
					friend.getFriends().remove(user);
					userRepository.save(friend);
				}
			}

			if (news.isPresent()) {
				newsRepository.delete(news.get());
			}
		}

		user.getFriends().clear(); 
		userRepository.save(user);

		userRepository.deleteById(id);
		return true;
	}

	@GetMapping("/notifications")
	public @ResponseBody List<Notification> getNotifications(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();

		List<Notification> lNotifications = userRepository.findByFirstName(nameUser).orElseThrow().getLNotifications();

		return lNotifications;
	}

	@PostMapping("/processRequest")
	public @ResponseBody void processRequest(@RequestParam Notification notificacion, @RequestParam boolean aceptar,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person receptor = userRepository.findByFirstName(nameUser).orElseThrow();

		if (aceptar) {
			String originalText = notificacion.getContent();
			int positionTwoPoints = originalText.indexOf(":");
			String textAfterPoints = originalText.substring(positionTwoPoints + 1);
			String cleanText = textAfterPoints.trim();
			Person sender = userRepository.findByFirstName(cleanText).orElseThrow();
			if (!receptor.getFriends().contains(sender)) { 
				receptor.getFriends().add(sender); 
				sender.getFriends().add(receptor);
				userRepository.save(sender);
			}
		}
		List<Notification> notificationsUser = receptor.getLNotifications();
		notificationsUser.remove(notificacion);
		userRepository.save(receptor);

	}

	@GetMapping("/loadFriends")
	public @ResponseBody List<String> loadFriends(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<String> lFriends = userRepository.findFirstNameOfFriendsByPerson(user);

		return lFriends;
	}

	@GetMapping("/loadRutines")
	public @ResponseBody List<Rutine> getOwnRutines(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutine> rutines = user.getRutines();
		return rutines;
	}

	@GetMapping("/showRutine")
	public String showRutine(Model model, @RequestParam Long id, HttpServletRequest request) {
		Person user = userRepository.findByRutineId(id).orElseThrow();
		Rutine rutine = rutineRepository.findById(id).orElseThrow();
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("nameUser", user.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(rutine.getDate());
		model.addAttribute("date", formatedDate);
		model.addAttribute("rutName", rutine.getName());
		model.addAttribute("ejercicios", rutine.getExercises());
		model.addAttribute("mensajes", rutine.getMensajes());
		model.addAttribute("id", id);
		if (user.getImagen() != null) {

			model.addAttribute("imagePath", user.getImagen().getName());
		} 
		else
			model.addAttribute("imagePath", "logo.jpg");

		return "rutine";
	}

	@PostMapping("/sendComment")
	public @ResponseBody Comment postMethodName(@RequestParam String comentario, @RequestParam Long id,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		String firstNameUser = user.getFirstName();
		Comment message = new Comment(firstNameUser, comentario);
		messageRepository.save(message);
		Rutine rutine = rutineRepository.findById(id).orElseThrow();
		rutine.getMensajes().add(message);
		rutineRepository.save(rutine);

		return message;
	}

	@GetMapping("/statistics")
	public String statistics() {
		return "progress";
	}

	@GetMapping("/loadCharts")
	public @ResponseBody Map<String, Integer> loadCharts(HttpServletRequest request) {
		Map<String, Integer> map = new HashMap<>();
		String[] gruposMusculares = {
				"Pecho",
				"Espalda",
				"Bíceps",
				"Tríceps",
				"Hombro",
				"Tren Inferior",
				"Cardio"
		};
		for (int i = 0; i < gruposMusculares.length; i++) {
			map.put(gruposMusculares[i], 0);

		}
		String nameUser = request.getUserPrincipal().getName();
		Person user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutine> lrutines = user.getRutines();
		for (Rutine rutine : lrutines) {
			List<ExRutine> lExcer = rutine.getExercises();
			for (ExRutine exercise : lExcer) {
				int index = Arrays.asList(gruposMusculares).indexOf(exercise.getGrp());
				int value = map.get(gruposMusculares[index]);
				value += 1;
				map.put(gruposMusculares[index], value);

			}
		}
		return map;
	}

}
