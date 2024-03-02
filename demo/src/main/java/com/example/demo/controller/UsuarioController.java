package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.service.ImagenService;
import com.example.demo.service.UserService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImagenService imagenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NovedadRepository novedadRepository;

	@Autowired
	private NotificacionRepository notificacionRepository;

	@Autowired
	private RutinaRepository rutinaRepository;

	@Autowired
	private MensajeRepository mensajeRepository;

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
			model.addAttribute("erroMg", "El usuario ya existe");
			return "error";
		}
		String pass = passwordEncoder.encode(password);
		Usuario usuario = new Usuario(name, pass, name, date, weight, "USER");
		if (!imagenFile.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = imagenFile.getBytes();

				// object Image
				Imagen imagen = new Imagen();
				imagen.setContenido(imagenFile.getContentType());
				imagen.setName(imagenFile.getOriginalFilename());
				imagen.setDatos(datosImagen);
				usuario.setImagen(imagen);
				imagenService.guardarImagen(imagen);
			} catch (IOException e) {
			}
		}
		userRepository.save(usuario);
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
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
		usuario.setName(name);
		usuario.setFirstName(firstName);
		usuario.setDate(date);
		usuario.setWeight(weight);
		if (!image.isEmpty()) {
			try {
				// byte[] convert
				byte[] datosImagen = image.getBytes();

				// object Image
				Imagen imagen = new Imagen();
				imagen.setContenido(image.getContentType());
				imagen.setName(image.getOriginalFilename());
				imagen.setDatos(datosImagen);
				imagenService.guardarImagen(imagen);
				usuario.setImagen(imagen);
			} catch (IOException e) {
			}
		}
		userRepository.save(usuario);
		Imagen imageN = usuario.getImagen();
		String rutaImagen = "logo.jpg";
		if (!(imageN == null)) {
			rutaImagen = imageN.getName();

		}
		model.addAttribute("firstName", usuario.getFirstName());
		model.addAttribute("name", usuario.getName());
		model.addAttribute("date", usuario.getDate());
		model.addAttribute("weight", usuario.getWeight());
		model.addAttribute("rutaImagen", rutaImagen);
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));

		return "user";
	}

	@GetMapping("/novedades-iniciales")
	public @ResponseBody List<Object> getNovedades(@RequestParam int iteracion, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Novedad> pagina = userRepository.novedades(usuario, PageRequest.of(iteracion, 10));
		long numPaginas = usuario.getNovedades().size();
		List<Object> data = new ArrayList<>(Arrays.asList(pagina, numPaginas));

		return data; // list<news>
	}

	@GetMapping("/muscGr")
	public String muscGr(Model model, HttpServletRequest request) {
		model.addAttribute("search", true);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		return "muscleGroup";
	}

	@GetMapping("/busqueda")
	public @ResponseBody Map<String, Object> getNombres(@RequestParam String nombre, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		Boolean bAdmin = request.isUserInRole("ADMIN");
		List<String[]> lNameId = userRepository.getIdandFirstName(nombre, user.getId());
		Map<String, Object> response = new HashMap<>();
		response.put("lNameId", lNameId);
		response.put("bAdmin", bAdmin);

		return response;
	}

	@PostMapping("/sendSolicitud")
	public @ResponseBody Boolean enviarSolicitud(@RequestParam String id, HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario sender = userRepository.findByFirstName(nameUser).orElseThrow();
		Usuario reciber = userRepository.findById(Long.parseLong(id)).orElseThrow();
		Notificacion notificacion = new Notificacion(sender.getFirstName());
		notificacionRepository.save(notificacion);
		reciber.getNotificaciones().add(notificacion);
		userRepository.save(reciber);
		return true;
	}

	@PostMapping("/deleteUser")
	public @ResponseBody Boolean deleteUser(@RequestParam Long id) {
		Usuario usuario = userRepository.findById(id).orElseThrow();
		List<Rutina> lrutinas = usuario.getRutinas();
		for (Rutina rutina : lrutinas) {
			List<Usuario> lUsuarios = usuario.getAmigos();
			Optional<Novedad> novedad = novedadRepository.findByrutina(rutina);
			for (Usuario amigo : lUsuarios) {
				if (novedad.isPresent()) {
					amigo.getNovedades().remove(novedad.get());
					amigo.getAmigos().remove(usuario);
					userRepository.save(amigo);
				}
			}

			if (novedad.isPresent()) {
				novedadRepository.delete(novedad.get());
			}
		}

		usuario.getAmigos().clear(); 
		userRepository.save(usuario);

		userRepository.deleteById(id);
		return true;
	}

	@GetMapping("/notificaciones")
	public @ResponseBody List<Notificacion> getNotificaciones(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();

		List<Notificacion> lNotificaciones = userRepository.findByFirstName(nameUser).orElseThrow().getNotificaciones();

		return lNotificaciones;
	}

	@PostMapping("/procesarSolicitud")
	public @ResponseBody void procesarSolicitud(@RequestParam Notificacion notificacion, @RequestParam boolean aceptar,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario receptor = userRepository.findByFirstName(nameUser).orElseThrow();

		if (aceptar) {
			String textoOriginal = notificacion.getContenido();
			int indiceDosPuntos = textoOriginal.indexOf(":");
			String textoDespuesDosPuntos = textoOriginal.substring(indiceDosPuntos + 1);
			String textoLimpio = textoDespuesDosPuntos.trim();
			Usuario sender = userRepository.findByFirstName(textoLimpio).orElseThrow();
			if (!receptor.getAmigos().contains(sender)) { 
				receptor.getAmigos().add(sender); 
				sender.getAmigos().add(receptor);
				userRepository.save(sender);
			}
		}
		List<Notificacion> notificacionesUsuario = receptor.getNotificaciones();
		notificacionesUsuario.remove(notificacion);
		userRepository.save(receptor);

	}

	@GetMapping("/cargarAmigos")
	public @ResponseBody List<String> cargarAmigos(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<String> lAmigos = userRepository.findFirstNameOfAmigosByUsuario(user);

		return lAmigos;
	}

	@GetMapping("/cargarRutinas")
	public @ResponseBody List<Rutina> getRutinasPropias(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutina> rutinas = user.getRutinas();
		return rutinas;
	}

	@GetMapping("/verRutina")
	public String verRutina(Model model, @RequestParam Long id, HttpServletRequest request) {
		Usuario usuario = userRepository.findByRutinaId(id).orElseThrow();
		Rutina rutina = rutinaRepository.findById(id).orElseThrow();
		model.addAttribute("firstName", usuario.getFirstName());
		model.addAttribute("nameUser", usuario.getName());
		model.addAttribute("date", rutina.getDate());
		model.addAttribute("rutName", rutina.getName());
		model.addAttribute("ejercicios", rutina.getEjercicios());
		model.addAttribute("mensajes", rutina.getMensajes());
		model.addAttribute("id", id);
		if (usuario.getImagen() != null) {

			model.addAttribute("rutaImagen", usuario.getImagen().getName());
		} 
		else
			model.addAttribute("rutaImagen", "logo.jpg");

		return "rutine";
	}

	@PostMapping("/enviarComentario")
	public @ResponseBody Mensaje postMethodName(@RequestParam String comentario, @RequestParam Long id,
			HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
		String nombreUser = usuario.getFirstName();
		Mensaje mensaje = new Mensaje(nombreUser, comentario);
		mensajeRepository.save(mensaje);
		Rutina rutina = rutinaRepository.findById(id).orElseThrow();
		rutina.getMensajes().add(mensaje);
		rutinaRepository.save(rutina);

		return mensaje;
	}

	@GetMapping("/estadisticas")
	public String estadisticas() {
		return "progress";
	}

	@GetMapping("/cargarGraficas")
	public @ResponseBody Map<String, Integer> cargarGraficas(HttpServletRequest request) {
		Map<String, Integer> mapa = new HashMap<>();
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
			mapa.put(gruposMusculares[i], 0);

		}
		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutina> lRutinas = usuario.getRutinas();
		for (Rutina rutina : lRutinas) {
			List<EjerRutina> lEjercicios = rutina.getEjercicios();
			for (EjerRutina ejercicio : lEjercicios) {
				int indice = Arrays.asList(gruposMusculares).indexOf(ejercicio.getGrupo());
				int valor = mapa.get(gruposMusculares[indice]);
				valor += 1;
				mapa.put(gruposMusculares[indice], valor);

			}
		}
		return mapa;
	}
}
