package com.example.demo.controller;

import com.example.demo.repository.EjercicioRepository;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.model.Ejercicio;
import com.example.demo.model.Rutina;
import com.example.demo.model.Usuario;

import com.example.demo.model.Imagen;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private EjercicioService service;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
    }

   @PostMapping("/add")
    public String register(@RequestParam("date") Date date,
                           @RequestParam("name") String name,
                           @RequestParam("time") Integer time,  HttpServletRequest request) {
        
        if (date == null|| name.isEmpty() || time == 0) return "error";

        String nameUser = request.getUserPrincipal().getName();
		Usuario usuario = userRepository.findByFirstName(nameUser).orElseThrow();
       
        Rutina rutina = new Rutina(name,date,time);
        rutinaRepository.save(rutina);

        usuario.getRutinas().add(rutina);
        userRepository.save(usuario);
       
       
                
        
        return "redirect:/main";
    }

    
    

    @PostMapping("/newEx")
	public String newEx(@RequestParam("name") String name,
	@RequestParam("description") String description,
    @RequestParam MultipartFile image,
    @RequestParam("video") String video, 
    @RequestParam("grp") String grp,
    Model model, HttpServletRequest request) {
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
        if (video.isEmpty()){
            model.addAttribute("ExVideo", false);
           
            	
        }else{
           model.addAttribute("ExVideo", true);
           ejercicio.setVideo(video);
           model.addAttribute("video", video);
        }
        String rutaImagen = "logo.jpg";
        if (!image.isEmpty()){
            try{
                byte[] datosImagen = image.getBytes();

            // Crear objeto Imagen
            Imagen imagen = new Imagen();
            imagen.setContenido(image.getContentType());
            imagen.setName(image.getOriginalFilename());
            imagen.setDatos(datosImagen);
            ejercicio.setImagen(imagen);
            rutaImagen = imagen.getName();
            }
            catch (IOException e) {}
        }
        ejercicioRepository.save(ejercicio);
      
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);	
		model.addAttribute("description",description);
        model.addAttribute("image", rutaImagen);
		return "details";
       
	}
    @GetMapping ("/addEx")
	public String addEX(Model model){
        List<Ejercicio> pecho = ejercicioRepository.findByGrp("Pecho");
        model.addAttribute("pecho", pecho);
        List<Ejercicio> espalda = ejercicioRepository.findByGrp("Espalda");
        model.addAttribute("espalda", espalda);
        List<Ejercicio> hombro = ejercicioRepository.findByGrp("Hombro");
        model.addAttribute("hombro", hombro);
        List<Ejercicio> biceps = ejercicioRepository.findByGrp("Biceps");
        model.addAttribute("biceps", biceps);
        List<Ejercicio> triceps = ejercicioRepository.findByGrp("Triceps");
        model.addAttribute("triceps", triceps);
        List<Ejercicio>  inferior = ejercicioRepository.findByGrp("Tren Inferior");
        model.addAttribute("inferior", inferior);
        List<Ejercicio> cardio = ejercicioRepository.findByGrp("Cardio");
        model.addAttribute("cardio", cardio);
		return "adEjerRutina";
	}
   
    
}
