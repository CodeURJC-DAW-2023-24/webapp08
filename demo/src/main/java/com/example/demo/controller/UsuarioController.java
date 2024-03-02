package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.service.ImagenService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Notificacion;
import com.example.demo.model.EjerRutina;
import com.example.demo.model.Imagen;
import com.example.demo.model.Mensaje;
import com.example.demo.model.Novedad;
import com.example.demo.model.Rutina;
import com.example.demo.model.Usuario;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.repository.NotificacionRepository;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.repository.UserRepository;

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
public class UsuarioController implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImagenService imageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NovedadRepository newsRepository;

	@Autowired
	private NotificacionRepository notificationRepository;

	@Autowired
	private RutinaRepository rutineRepository;

	@Autowired
	private MensajeRepository messageRepository;

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
		Optional<Usuario> existingUserOptional = userRepository.findByFirstName(firstName);
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
		Usuario user = new Usuario(firstName, pass, name, date, weight, "USER");
		if (!imagenFile.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = imagenFile.getBytes();

				// object Image
				Imagen image = new Imagen();
				image.setContenido(imagenFile.getContentType());
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
		Usuario user = userRepository.findByFirstName(name).orElseThrow();
		Imagen image = user.getImagen();
		String rutaImagen = "logo.jpg";
		if (!(image == null)) {
			rutaImagen = image.getName();
		}
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("rutaImagen", rutaImagen);
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
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		user.setName(name);
		user.setFirstName(firstName);
		user.setDate(date);
		user.setWeight(weight);
		if (!image.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = image.getBytes();

				// object Image
				Imagen imageF = new Imagen();
				imageF.setContenido(image.getContentType());
				imageF.setName(image.getOriginalFilename());
				imageF.setDatos(datosImagen);
				imageService.guardarImagen(imageF);
				user.setImagen(imageF);
			} catch (IOException e) {
			}
		}
		userRepository.save(user);
		Imagen imageN = user.getImagen();
		String rutaImagen = "logo.jpg";
		if (!(imageN == null)) {
			rutaImagen = imageN.getName();

		}
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("name", user.getName());
		model.addAttribute("date", user.getDate());
		model.addAttribute("weight", user.getWeight());
		model.addAttribute("rutaImagen", rutaImagen);
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));

		return "user";
	}

	@GetMapping("/starterNews")
	public @ResponseBody List<Object> getNovedades(@RequestParam int iteracion, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		Page<Novedad> pNews =userRepository.findByNovedades(user.getNovedades(),PageRequest.of(iteracion, 10));
		List<Novedad> page = pNews.getContent();
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
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
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
		Usuario sender = userRepository.findByFirstName(nameUser).orElseThrow();
		Usuario receiver = userRepository.findById(Long.parseLong(id)).orElseThrow();
		Notificacion notificacion = new Notificacion(sender.getFirstName());
		notificationRepository.save(notificacion);
		receiver.getNotificaciones().add(notificacion);
		userRepository.save(receiver);
		return true;
	}

	@PostMapping("/deleteUser")
	public @ResponseBody Boolean deleteUser(@RequestParam Long id) {
		Usuario user = userRepository.findById(id).orElseThrow();
		List<Rutina> lrutines = user.getRutinas();
		for (Rutina rutine : lrutines) {
			List<Usuario> lUsers = user.getAmigos();
			Optional<Novedad> news = newsRepository.findByrutina(rutine);
			for (Usuario friend : lUsers) {
				if (news.isPresent()) {
					friend.getNovedades().remove(news.get());
					friend.getAmigos().remove(user);
					userRepository.save(friend);
				}
			}

			if (news.isPresent()) {
				newsRepository.delete(news.get());
			}
		}

		user.getAmigos().clear(); 
		userRepository.save(user);

		userRepository.deleteById(id);
		return true;
	}

	@GetMapping("/notifications")
	public @ResponseBody List<Notificacion> getNotifications(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();

		List<Notificacion> lNotifications = userRepository.findByFirstName(nameUser).orElseThrow().getNotificaciones();

		return lNotifications;
	}

	@PostMapping("/processRequest")
	public @ResponseBody void processRequest(@RequestParam Notificacion notificacion, @RequestParam boolean aceptar,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario receptor = userRepository.findByFirstName(nameUser).orElseThrow();

		if (aceptar) {
			String originalText = notificacion.getContenido();
			int positionTwoPoints = originalText.indexOf(":");
			String textAfterPoints = originalText.substring(positionTwoPoints + 1);
			String cleanText = textAfterPoints.trim();
			Usuario sender = userRepository.findByFirstName(cleanText).orElseThrow();
			if (!receptor.getAmigos().contains(sender)) { 
				receptor.getAmigos().add(sender); 
				sender.getAmigos().add(receptor);
				userRepository.save(sender);
			}
		}
		List<Notificacion> notificationsUser = receptor.getNotificaciones();
		notificationsUser.remove(notificacion);
		userRepository.save(receptor);

	}

	@GetMapping("/loadFriends")
	public @ResponseBody List<String> loadFriends(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<String> lFriends = userRepository.findFirstNameOfAmigosByUsuario(user);

		return lFriends;
	}

	@GetMapping("/loadRutines")
	public @ResponseBody List<Rutina> getOwnRutines(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutina> rutines = user.getRutinas();
		return rutines;
	}

	@GetMapping("/showRutine")
	public String showRutine(Model model, @RequestParam Long id, HttpServletRequest request) {
		Usuario user = userRepository.findByRutinaId(id).orElseThrow();
		Rutina rutine = rutineRepository.findById(id).orElseThrow();
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("nameUser", user.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(rutine.getDate());
		model.addAttribute("date", formatedDate);
		model.addAttribute("rutName", rutine.getName());
		model.addAttribute("ejercicios", rutine.getEjercicios());
		model.addAttribute("mensajes", rutine.getMensajes());
		model.addAttribute("id", id);
		if (user.getImagen() != null) {

			model.addAttribute("rutaImagen", user.getImagen().getName());
		} 
		else
			model.addAttribute("rutaImagen", "logo.jpg");

		return "rutine";
	}

	@PostMapping("/sendComment")
	public @ResponseBody Mensaje postMethodName(@RequestParam String comentario, @RequestParam Long id,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		String firstNameUser = user.getFirstName();
		Mensaje message = new Mensaje(firstNameUser, comentario);
		messageRepository.save(message);
		Rutina rutine = rutineRepository.findById(id).orElseThrow();
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
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutina> lrutines = user.getRutinas();
		for (Rutina rutine : lrutines) {
			List<EjerRutina> lExcer = rutine.getEjercicios();
			for (EjerRutina exercise : lExcer) {
				int index = Arrays.asList(gruposMusculares).indexOf(exercise.getGrupo());
				int value = map.get(gruposMusculares[index]);
				value += 1;
				map.put(gruposMusculares[index], value);

			}
		}
		return map;
	}

}
