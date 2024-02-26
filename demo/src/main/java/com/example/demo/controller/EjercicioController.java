package com.example.demo.controller;

import com.example.demo.repository.EjercicioRepository;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.model.Ejercicio;
import com.example.demo.model.Rutina;
import com.example.demo.model.Usuario;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
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

    @GetMapping("/addEjercicioRutina")
    public String addEjercicicoRutina() {
        return "adEjerRutina";
    }
    

    @PostMapping("/newEx")
	public String newEx(@RequestParam("name") String name,
	@RequestParam("description") String description,
	@RequestParam("grp") String grp,@RequestParam("video") String video, HttpSession session, Model model, HttpServletRequest request) {
		Optional<Ejercicio> existingExOptional = ejercicioRepository.findByName(name);
		if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
			model.addAttribute("erroMg", "Rellene todos los campos");
			return "error";
		}

		if (existingExOptional.isPresent()) {
			model.addAttribute("erroMg", "El ejercicio ya existe");
			return "error";
		}		
			
        if (video.isEmpty()){
            model.addAttribute("ExVideo", false);
            service.save(name, description, grp, "0");
            	
        }else{
            model.addAttribute("ExVideo", true);
            service.save(name, description, grp, video);
            model.addAttribute("video", video);
        }
      
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);	
		model.addAttribute("description",description);
		return "details";
       
	}
   
}
