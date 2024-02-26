package com.example.demo.controller;

import com.example.demo.repository.EjercicioRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.model.Ejercicio;
import com.example.demo.model.Imagen;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {

    @Autowired
    private EjercicioRepository EjercicioRepository;

    @Autowired
    private EjercicioService service;

    @Override
    public void run(String... args) throws Exception {
    }

   /* @PostMapping("/add")
    public String register(@RequestParam("date") String date,
                           @RequestParam("name") String name,
                           @RequestParam("time") Integer time) {

        Optional<Ejercicio> existingEjercicioOptional = repository.findByName(name);
        if (date.isEmpty() || name.isEmpty() || time == 0) return "error";
        if (existingEjercicioOptional.isPresent()) return "error";

        service.save(date, name, time);

        return "index";
    }*/

    @PostMapping("/newEx")
	public String newEx(@RequestParam("name") String name,
	@RequestParam("description") String description,
    @RequestParam MultipartFile image,
    @RequestParam("video") String video, 
    @RequestParam("grp") String grp,
    Model model, HttpServletRequest request) {
		Optional<Ejercicio> existingExOptional = EjercicioRepository.findByName(name);
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
        EjercicioRepository.save(ejercicio);
      
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);	
		model.addAttribute("description",description);
        model.addAttribute("image", rutaImagen);
		return "details";
       
	}
    @GetMapping ("/addEx")
	public String addEX(Model model){
        List<Ejercicio> pecho = EjercicioRepository.findByGrp("Pecho");
        model.addAttribute("pecho", pecho);
        List<Ejercicio> espalda = EjercicioRepository.findByGrp("Espalda");
        model.addAttribute("espalda", espalda);
        List<Ejercicio> hombro = EjercicioRepository.findByGrp("Hombro");
        model.addAttribute("hombro", hombro);
        List<Ejercicio> biceps = EjercicioRepository.findByGrp("Biceps");
        model.addAttribute("biceps", biceps);
        List<Ejercicio> triceps = EjercicioRepository.findByGrp("Triceps");
        model.addAttribute("triceps", triceps);
        List<Ejercicio>  inferior = EjercicioRepository.findByGrp("Tren Inferior");
        model.addAttribute("inferior", inferior);
        List<Ejercicio> cardio = EjercicioRepository.findByGrp("Cardio");
        model.addAttribute("cardio", cardio);
		return "adEjerRutina";
	}
   
    
}
