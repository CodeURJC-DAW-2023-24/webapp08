package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import com.example.demo.model.Notificacion;
import com.example.demo.model.Ejercicio;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	private ImagenService imagenService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService service;
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
	public String main(Model model,  HttpServletRequest request){
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
	@RequestParam("image") MultipartFile imagenFile,HttpSession session, Model model) {
		Optional<Usuario> existingUserOptional = userRepository.findByFirstName(firstName);
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
			userRepository.save(usuario);
			//service.save(firstName, pass, name, date, weight);
			//userRepository.save(new Usuario(name, passwordEncoder.encode(password),name, date,weight, "USER"));
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
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		String name = request.getUserPrincipal().getName();
		System.out.println(name);
		Usuario user = userRepository.findByFirstName(name).orElseThrow();
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

	@GetMapping ("/comunity")
	public String comunity(){
		return "comunity";
	}

	@GetMapping ("/exForm")
	public String exForm(){
		return "exFormAd";
	}
	
	
	
	@PostMapping("/editUser")
	public String editUser(Model model, @RequestParam String name, @RequestParam String firstName,@RequestParam String
	            date, @RequestParam Integer
	             weight ,
				 @RequestParam MultipartFile image,HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
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
		userRepository.save(usuario);
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
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		
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
	public String muscGr(Model model,HttpServletRequest request) {
		model.addAttribute("search", true);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		return "muscleGroup";
	}
	

	@GetMapping("/busqueda")
	public @ResponseBody List<String[]> getNombres(@RequestParam  String nombre,HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
    Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();

    
    List<String[]> lNameId = userRepository.getIdandFirstName(nombre, user.getId());

    return lNameId;
	}

	@PostMapping("/sendSolicitud") //Ns si la solicitud fetch es un post o un get
	public @ResponseBody Boolean  enviarSolicitud(@RequestParam String id,HttpServletRequest request){
	String nameUser = request.getUserPrincipal().getName();
	Usuario sender = userRepository.findByFirstName(nameUser).orElseThrow();
	Usuario reciber = userRepository.findById(Long.parseLong(id)).orElseThrow();
	Notificacion notificacion = new Notificacion(sender.getFirstName());
	notificacionRepository.save(notificacion);
	reciber.getNotificaciones().add(notificacion);
	userRepository.save(reciber);
	return 	true;
	}

	@GetMapping("/notificaciones")
	public @ResponseBody List<Notificacion> getNotificaciones(HttpServletRequest request) {
		String nameUser = request.getUserPrincipal().getName();
		
		List<Notificacion> lNotificaciones = userRepository.findByFirstName(nameUser).orElseThrow().getNotificaciones();

		
		
		return lNotificaciones;
	}
	
	@PostMapping("/procesarSolicitud")
	public @ResponseBody void procesarSolicitud(@RequestParam  Notificacion notificacion,@RequestParam  boolean aceptar,HttpServletRequest request) { //Aunque le pases el id si pones Notificacion te la busca automaticamente
		String nameUser = request.getUserPrincipal().getName();
		Usuario receptor = userRepository.findByFirstName(nameUser).orElseThrow();
			 
		if (aceptar) {
			 String textoOriginal = notificacion.getContenido();
			 int indiceDosPuntos = textoOriginal.indexOf(":");
			String textoDespuesDosPuntos = textoOriginal.substring(indiceDosPuntos + 1);
			String textoLimpio = textoDespuesDosPuntos.trim();
			Usuario sender = userRepository.findByFirstName(textoLimpio).orElseThrow();
			if (!receptor.getAmigos().contains(sender)){ //Con comprobarlo para uno es suficiente
			receptor.getAmigos().add(sender); //REVISAR SI HACE FALTA AÑADIR LA OPUESTA ##################################################################
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
		Rutina rutina = new Rutina("hoy", new Date(124, 1, 26), 70);
		rutinaRepository.save(rutina);
		userRepository.findByFirstName("1").orElseThrow().getRutinas().add(rutina);
		userRepository.save(userRepository.findByFirstName("1").orElseThrow());

		String nameUser = request.getUserPrincipal().getName();
		Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
		List<Rutina> rutinas = user.getRutinas();
		return rutinas;
	}


	@GetMapping("/verRutina")
	public String verRutina(Model model,@RequestParam Long id,HttpServletRequest request) {
		rutinaRepository.findById(id);

		String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();

		model.addAttribute("firstName", usuario.getFirstName());	
		model.addAttribute("name", usuario.getName());
		model.addAttribute("date", usuario.getDate());
		model.addAttribute("id", id);
		if (usuario.getImagen() !=null) {
			
			model.addAttribute("rutaImagen", usuario.getImagen().getName());} //Creo que no devuelve una url y por eso peta
		else 
			model.addAttribute("rutaImagen","logo.jpg");

		return "rutine";
	}
	
	@PostMapping("/enviarComentario")
    public @ResponseBody List<Mensaje> postMethodName(@RequestParam String comentario, @RequestParam Long id,HttpServletRequest request ) {
        String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
        String nombreUser = usuario.getFirstName();
		Mensaje mensaje = new Mensaje(nombreUser, comentario);
		mensajeRepository.save(mensaje);

		 Rutina rutina = rutinaRepository.findById(id).orElseThrow();
		 rutina.getMensajes().add(mensaje);
		 rutinaRepository.save(rutina);

        
   
       return rutina.getMensajes();
        
    }
	
}
