package com.example.demo.controller;

import com.example.demo.repository.EjerRutinaRepository;
import com.example.demo.repository.EjercicioRepository;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.repository.NovedadRepository;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.service.ImagenService;
import com.example.demo.service.UserService;
import com.example.demo.model.Ejercicio;
import com.example.demo.model.Rutina;
import com.example.demo.model.Usuario;
import com.example.demo.model.EjerRutina;
import com.example.demo.model.Imagen;
import com.example.demo.model.Mensaje;
import com.example.demo.model.Novedad;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EjercicioController implements CommandLineRunner {
    @Autowired
    private ImagenService imagenService;
    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private EjercicioService service;
    @Autowired
    private UserService userService;

    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private EjerRutinaRepository ejerRutinaRepository;
    @Autowired
    private MensajeRepository mensajeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NovedadRepository novedadRepository;


    @Override
    public void run(String... args) throws Exception {
    }

    @PostMapping("/add/{id}")
    public String register(@PathVariable Long id,
            @RequestParam("date") Date date,
            @RequestParam("name") String name,
            @RequestParam("time") Integer time, HttpServletRequest request) {

        if (date == null || name.isEmpty() || time == 0)
            return "error";

        String nameUser = request.getUserPrincipal().getName();
        Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();

        Rutina rutina = rutinaRepository.findById(id).orElseThrow();
        rutina.setDate(date);
        rutina.setName(name);
        rutina.setTime(time);
        rutinaRepository.save(rutina);

        usuario.getRutinas().add(rutina);
        userRepository.save(usuario); 
        
        List<Usuario> lAmigos = usuario.getAmigos();
        Novedad novedad = new Novedad(nameUser);
        novedad.setRutina(rutina);
        novedadRepository.save(novedad);
        for (Usuario amigo: lAmigos) {
            amigo.getNovedades().add(novedad);
            userRepository.save(amigo);
        }
       

        return "redirect:/main";
    }

    @PostMapping("/addExRutine/{id}")
    public String addExRutine(@PathVariable Long id,
            @RequestParam("grupo") String grupo,
            @RequestParam("pecho") String pecho,
            @RequestParam("espalda") String espalda,
            @RequestParam("hombro") String hombro,
            @RequestParam("biceps") String biceps,
            @RequestParam("triceps") String triceps,
            @RequestParam("inferior") String inferior,
            @RequestParam("cardio") String cardio,
            @RequestParam("series") String series,
            @RequestParam("peso") Integer peso, HttpServletRequest request, Model model) {

        if (series == null)
            return "error";
        Rutina rutina = rutinaRepository.findById(id).orElseThrow();
        String name = " ";
        switch (grupo) {
            case "Pecho":
                name = pecho;
                break;
            case "Espalda":
                name = espalda;
                break;
            case "Hombro":
                name = hombro;
                break;
            case "Biceps":
                name = biceps;
                break;
            case "Triceps":
                name = triceps;
                break;
            case "Inferior":
                name = inferior;
                break;
            default:
                name = cardio;
                break;
        }
        EjerRutina ejercicio = new EjerRutina(grupo,name,series, peso); 
        String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
        userService.aumentarFrecuencia(usuario,grupo,name);
        ejerRutinaRepository.save(ejercicio);
        rutina.addEjerRutina(ejercicio);
        rutinaRepository.save(rutina);
        List<EjerRutina> ejercicios = rutina.getEjercicios();
        model.addAttribute("id", id);
        model.addAttribute("ejersRutina", ejercicios);

        return "adRutine";
    }

    
    

   @PostMapping("/newEx")
	public String newEx(@RequestParam("name") String name,
	@RequestParam("description") String description,
    @RequestParam MultipartFile image,
    @RequestParam("video") String video, 
    @RequestParam("grp") String grp,
    Model model, HttpServletRequest request) throws InterruptedException {
		Optional<Ejercicio> existingExOptional = ejercicioRepository.findByName(name);
		if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
			model.addAttribute("erroMg", "Rellene todos los campos");
			return "error";
		}

        if (existingExOptional.isPresent()) {
            model.addAttribute("erroMg", "El ejercicio ya existe");
            return "error";
        }
        Ejercicio ejercicio = new Ejercicio(name, description, grp, "0");
        if (video.isEmpty()) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            ejercicio.setVideo(video);
            model.addAttribute("video", video);
        }
        if (!image.isEmpty()){
            try{
                byte[] datosImagen = image.getBytes();

            // Crear objeto Imagen
            Imagen imagen = new Imagen();
            imagen.setContenido(image.getContentType());
            imagen.setName(image.getOriginalFilename());
            imagen.setDatos(datosImagen);
            imagenService.guardarImagen(imagen);
            ejercicio.setImagen(imagen);
            }
            catch (IOException e) {}
        }
        ejercicioRepository.save(ejercicio);
        Imagen imageN = ejercicio.getImagen();
		String rutaImagen = "logo.jpg";
		if(!(imageN == null)){
			 rutaImagen = imageN.getName();
			
		}
        Thread.sleep(500);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("image", rutaImagen);
        return "details";

    }

    @GetMapping("/group/{grupo}/")
    public String group2(@PathVariable String grupo, Model model, HttpServletRequest request, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), 5);
        Page<Ejercicio> exs = ejercicioRepository.findByGrp(grupo, pageable);

        List<Ejercicio> ejercicioList = exs.getContent(); // Obtener la lista de ejercicios de la página

        // Iterar sobre cada ejercicio para agregar el atributo booleano
        for (Ejercicio ejercicio : ejercicioList) {
            Imagen image = ejercicio.getImagen();
            String rutaImagen = "logo.jpg";
            if (!(image == null)) {
                rutaImagen = image.getName();
                if (!imagenService.verificarExistenciaImagen(rutaImagen)) {
                    imagenService.guardarImagen(image);
                }
                ejercicio.setRuta(rutaImagen);
            }else{
                ejercicio.setTieneImagen(false);
            }
        }

        model.addAttribute("exs", ejercicioList);
        model.addAttribute("grupo", grupo);
        model.addAttribute("hasPrev", exs.hasPrevious());
        model.addAttribute("hasNext", exs.hasNext());
        model.addAttribute("nextPage", exs.getNumber() + 1);
        model.addAttribute("prevPage", exs.getNumber() - 1);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));

        return "lists";

    }

    @GetMapping("/group/{grupo}/{id}")
    public String exDetails(@PathVariable String grupo, @PathVariable long id, Model model,
            HttpServletRequest request) {
        Ejercicio exercise = ejercicioRepository.findById(id).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Imagen image = exercise.getImagen();
		String rutaImagen = "logo.jpg";
		if(!(image == null)){
			 rutaImagen = image.getName();
			/**if(!imagenService.verificarExistenciaImagen(rutaImagen)){
				imagenService.guardarImagen(image);
			}**/
		}
        model.addAttribute("image", rutaImagen);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "details";
    }



    
    @GetMapping ("/addEx/{id}")
	public String addEX(@PathVariable Long id,Model model, HttpServletRequest request){
        String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
        List<Ejercicio> pecho = ejercicioRepository.findByGrp("Pecho");
        pecho = userService.ordenar(usuario,"Pecho",pecho);
        model.addAttribute("pecho", pecho);
        List<Ejercicio> espalda = ejercicioRepository.findByGrp("Espalda");
        espalda = userService.ordenar(usuario,"Espalda",espalda);
        model.addAttribute("espalda", espalda);
        List<Ejercicio> hombro = ejercicioRepository.findByGrp("Hombro");
        hombro = userService.ordenar(usuario,"Hombro",hombro);
        model.addAttribute("hombro", hombro);
        List<Ejercicio> biceps = ejercicioRepository.findByGrp("Biceps");
        biceps = userService.ordenar(usuario,"Biceps",biceps);
        model.addAttribute("biceps", biceps);
        List<Ejercicio> triceps = ejercicioRepository.findByGrp("Triceps");
        triceps = userService.ordenar(usuario,"Triceps",triceps);
        model.addAttribute("triceps", triceps);
        List<Ejercicio>  inferior = ejercicioRepository.findByGrp("Inferior");
        inferior = userService.ordenar(usuario,"Inferior",inferior);
        model.addAttribute("inferior", inferior);
        List<Ejercicio> cardio = ejercicioRepository.findByGrp("Cardio");
        cardio = userService.ordenar(usuario,"Cardio",cardio);
        model.addAttribute("cardio", cardio);
        model.addAttribute("id", id);
		return "adEjerRutina";
	}

    @GetMapping ("/adRutine")
	public String adRutine(Model model){
        Rutina rutina = new Rutina();
        rutinaRepository.save(rutina);
        model.addAttribute("id", rutina.getId());
        return "adRutine";
    }

    @GetMapping("/busquedaEx")
	public @ResponseBody List<String[]> getNombres(@RequestParam  String nombre,HttpServletRequest request) {
       
    List<String[]> names = ejercicioRepository.getNames(nombre);
     return names;
	}
    @GetMapping("/exercise/{nombre}")
    public String exercseM(@PathVariable String nombre,Model model,
            HttpServletRequest request) {
        Ejercicio exercise = ejercicioRepository.findByName(nombre).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Imagen image = exercise.getImagen();
        String rutaImagen = "logo.jpg";
        if (!(image == null)) {
            rutaImagen = image.getName();
            if (!imagenService.verificarExistenciaImagen(rutaImagen)) {
                imagenService.guardarImagen(image);
            }
        }
        model.addAttribute("image", rutaImagen);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "details";
    }
    

}
