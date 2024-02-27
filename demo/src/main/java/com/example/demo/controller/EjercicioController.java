package com.example.demo.controller;

import com.example.demo.repository.EjercicioRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.service.ImagenService;
import com.example.demo.model.Ejercicio;
import com.example.demo.model.Imagen;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {
    @Autowired
	private ImagenService imagenService;
    @Autowired
    private EjercicioRepository repository;

    @Autowired
    private EjercicioService service;

    @Override
    public void run(String... args) throws Exception {
    }

    /*
     * @PostMapping("/add")
     * public String register(@RequestParam("date") String date,
     * 
     * @RequestParam("name") String name,
     * 
     * @RequestParam("time") Integer time) {
     * 
     * Optional<Ejercicio> existingEjercicioOptional = repository.findByName(name);
     * if (date.isEmpty() || name.isEmpty() || time == 0) return "error";
     * if (existingEjercicioOptional.isPresent()) return "error";
     * 
     * service.save(date, name, time);
     * 
     * return "index";
     * }
     */

    @PostMapping("/newEx")
    public String newEx(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("grp") String grp, @RequestParam("video") String video, HttpSession session, Model model,
            HttpServletRequest request) {
        Optional<Ejercicio> existingExOptional = repository.findByName(name);
        if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
            model.addAttribute("erroMg", "Rellene todos los campos");
            return "error";
        }

        if (existingExOptional.isPresent()) {
            model.addAttribute("erroMg", "El ejercicio ya existe");
            return "error";
        }
        
        if (video.isEmpty()) {
            model.addAttribute("ExVideo", false);
            service.save(name, description, grp, "0");

        } else {
            model.addAttribute("ExVideo", true);
            service.save(name, description, grp, video);
            model.addAttribute("video", video);
        }
      
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        return "details";

    }

    /*@GetMapping("/group/{grupo}")
    public String group(@PathVariable String grupo, Model model, HttpServletRequest request) {

        List<Ejercicio> exs = repository.findByGrp(grupo);
        model.addAttribute("exs", exs);

        model.addAttribute("grupo", grupo);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "lists";
    }*/
    @GetMapping("/group/{grupo}")
    public String group( @PathVariable String grupo, Model model,HttpServletRequest request, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), 5);
        Page<Ejercicio> exs= repository.findByGrp(grupo,pageable);
        model.addAttribute("exs", exs); 
        model.addAttribute("grupo", grupo);
        model.addAttribute("hasPrev", exs.hasPrevious());
        model.addAttribute("hasNext", exs.hasNext());
        model.addAttribute("nextPage", exs.getNumber()+1);
        model.addAttribute("prevPage", exs.getNumber()-1);   
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "lists";
    } 
    @GetMapping("/group/{grupo}/")
    public String group2( @PathVariable String grupo, Model model,HttpServletRequest request, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), 5);
        Page<Ejercicio> exs= repository.findByGrp(grupo,pageable);
        model.addAttribute("exs", exs); 
        model.addAttribute("grupo", grupo);
        model.addAttribute("hasPrev", exs.hasPrevious());
        model.addAttribute("hasNext", exs.hasNext());
        model.addAttribute("nextPage", exs.getNumber()+1);
        model.addAttribute("prevPage", exs.getNumber()-1);   
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "lists";
    } 

    @GetMapping("/group/{grupo}/{id}")
    public String exDetails(@PathVariable String grupo, @PathVariable long id, Model model,
            HttpServletRequest request) {
        Ejercicio exercise = repository.findById(id).orElseThrow();
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
			if(!imagenService.verificarExistenciaImagen(rutaImagen)){
				imagenService.guardarImagen(image);
			}
		}
        model.addAttribute("image", rutaImagen);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "details";
    }

}
