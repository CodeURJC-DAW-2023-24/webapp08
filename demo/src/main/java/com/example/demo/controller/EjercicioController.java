package com.example.demo.controller;

import com.example.demo.repository.EjercicioRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.model.Ejercicio;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {

    @Autowired
    private EjercicioRepository repository;

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
	@RequestParam("grp") String grp,@RequestParam("video") String video, HttpSession session, Model model, HttpServletRequest request) {
		Optional<Ejercicio> existingExOptional = repository.findByName(name);
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
