package com.example.demo.controller;

import com.example.demo.repository.EjerRutinaRepository;
import com.example.demo.repository.EjercicioRepository;
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
import com.example.demo.model.Novedad;

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
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {
    @Autowired
    private ImagenService imageService;

    @Autowired
    private EjercicioRepository exRepository;

    @Autowired
    private EjercicioService exService;

    @Autowired
    private UserService userService;

    @Autowired
    private RutinaRepository rutineRepository;

    @Autowired
    private EjerRutinaRepository exRutineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NovedadRepository newsRepository;

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
        Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();

        Rutina rutine = rutineRepository.findById(id).orElseThrow();
        rutine.setDate(date);
        rutine.setName(name);
        rutine.setTime(time);
        rutineRepository.save(rutine);

        user.getRutinas().add(rutine);
        userRepository.save(user);

        List<Usuario> lFriends = user.getAmigos();
        if (!lFriends.isEmpty()) {
            Novedad news = new Novedad(nameUser);
            news.setRutina(rutine);
            newsRepository.save(news);
            for (Usuario friend : lFriends) {
                friend.getNovedades().add(news);
                userRepository.save(friend);
            }
        }

        return "redirect:/main";
    }

    @PostMapping("/addExRutine/{id}")
    public String addExRutine(@PathVariable Long id,
            @RequestParam("grupo") String grupo,
            @RequestParam("pecho") String chest,
            @RequestParam("espalda") String back,
            @RequestParam("hombro") String shoulder,
            @RequestParam("biceps") String biceps,
            @RequestParam("triceps") String triceps,
            @RequestParam("inferior") String lower,
            @RequestParam("cardio") String cardio,
            @RequestParam("series") String series,
            @RequestParam("peso") Integer peso, HttpServletRequest request, Model model) {

        if (series == null)
            return "error";
        Rutina rutine = rutineRepository.findById(id).orElseThrow();
        String name = " ";
        switch (grupo) {
            case "Pecho":
                name = chest;
                break;
            case "Espalda":
                name = back;
                break;
            case "Hombro":
                name = shoulder;
                break;
            case "Biceps":
                name = biceps;
                break;
            case "Triceps":
                name = triceps;
                break;
            case "Inferior":
                name = lower;
                break;
            default:
                name = cardio;
                break;
        }
        EjerRutina exercise = new EjerRutina(grupo, name, series, peso);
        String nameUser = request.getUserPrincipal().getName();
        Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
        userService.aumentarFrecuencia(user, grupo, name);
        exRutineRepository.save(exercise);
        rutine.addEjerRutina(exercise);
        rutineRepository.save(rutine);
        List<EjerRutina> exercises = rutine.getEjercicios();
        model.addAttribute("id", id);
        model.addAttribute("ejersRutina", exercises);

        return "adRutine";
    }

    @PostMapping("/newEx")
    public String newEx(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam MultipartFile image,
            @RequestParam("video") String video,
            @RequestParam("grp") String grp,
            Model model, HttpServletRequest request) throws InterruptedException {
        Optional<Ejercicio> existingExOptional = exRepository.findByName(name);
        if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
            model.addAttribute("erroMg", "Rellene todos los campos");
            return "error";
        }

        if (existingExOptional.isPresent()) {
            model.addAttribute("erroMg", "El ejercicio ya existe");
            return "error";
        }
        Ejercicio exercise = new Ejercicio(name, description, grp, "0");
        if (video.isEmpty()) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            exercise.setVideo(video);
            model.addAttribute("video", video);
        }
        if (!image.isEmpty()) {
            try {
                byte[] datosImagen = image.getBytes();

                // Object Image
                Imagen imagen = new Imagen();
                imagen.setContenido(image.getContentType());
                imagen.setName(image.getOriginalFilename());
                imagen.setDatos(datosImagen);
                imageService.guardarImagen(imagen);
                exercise.setImagen(imagen);
            } catch (IOException e) {
            }
        }
        exRepository.save(exercise);
        Imagen imageN = exercise.getImagen();
        String imagePath = "logo.jpg";
        if (!(imageN == null)) {
            imagePath = imageN.getName();

        }
        Ejercicio ex = exRepository.findByName(name).orElseThrow();
        Thread.sleep(500);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("image", imagePath);
        model.addAttribute("id", ex.getId());
        return "details";

    }

    @GetMapping("/group/{grupo}/")
    public String group2(@PathVariable String grupo, Model model, HttpServletRequest request, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), 5);
        Page<Ejercicio> exs = exRepository.findByGrp(grupo, pageable);
        List<Ejercicio> exerciseList = exs.getContent();
        for (Ejercicio exercise : exerciseList) {
            Imagen image = exercise.getImagen();
            String imagePath = "logo.jpg";
            if (!(image == null)) {
                exercise.setTieneImagen(true);
                imagePath = image.getName();
                if (!imageService.verificarExistenciaImagen(imagePath)) {
                    imageService.guardarImagen(image);
                }
                exercise.setRuta(imagePath);
            } else {
                exercise.setTieneImagen(false);
            }
        }

        model.addAttribute("exs", exerciseList);
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
        Ejercicio exercise = exRepository.findById(id).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Imagen image = exercise.getImagen();
        String imagePath = "logo.jpg";
        if (!(image == null)) {
            imagePath = image.getName();
        }
        model.addAttribute("image", imagePath);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("id", id);
        return "details";
    }

    @GetMapping("/addEx/{id}")
    public String addEX(@PathVariable Long id, Model model, HttpServletRequest request) {
        String nameUser = request.getUserPrincipal().getName();
        Usuario user = userRepository.findByFirstName(nameUser).orElseThrow();
        List<Ejercicio> chest = exRepository.findByGrp("Pecho");
        chest = userService.ordenar(user, "Pecho", chest);
        model.addAttribute("pecho", chest);
        List<Ejercicio> back = exRepository.findByGrp("Espalda");
        back = userService.ordenar(user, "Espalda", back);
        model.addAttribute("espalda", back);
        List<Ejercicio> shoulder = exRepository.findByGrp("Hombro");
        shoulder = userService.ordenar(user, "Hombro", shoulder);
        model.addAttribute("hombro", shoulder);
        List<Ejercicio> biceps = exRepository.findByGrp("Biceps");
        biceps = userService.ordenar(user, "Biceps", biceps);
        model.addAttribute("biceps", biceps);
        List<Ejercicio> triceps = exRepository.findByGrp("Triceps");
        triceps = userService.ordenar(user, "Triceps", triceps);
        model.addAttribute("triceps", triceps);
        List<Ejercicio> lower = exRepository.findByGrp("Inferior");
        lower = userService.ordenar(user, "Inferior", lower);
        model.addAttribute("inferior", lower);
        List<Ejercicio> cardio = exRepository.findByGrp("Cardio");
        cardio = userService.ordenar(user, "Cardio", cardio);
        model.addAttribute("cardio", cardio);
        model.addAttribute("id", id);
        return "adEjerRutina";
    }

    @SuppressWarnings("null")
    @GetMapping("/cancel/{id}")
    public String adRutine(@PathVariable Long id, Model model) {
        Rutina rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.delete(rutine);
        return "redirect:/main";
    }

    @GetMapping("/adRutine")
    public String cancelRutine(Model model) {
        Rutina rutine = new Rutina();
        rutineRepository.save(rutine);
        model.addAttribute("id", rutine.getId());
        return "adRutine";
    }

    @GetMapping("/rutine/{id}")
    public String rutine(@PathVariable Long id, Model model) {
        Rutina rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.save(rutine);
        model.addAttribute("id", id);
        return "adRutine";
    }

    @GetMapping("/searchEx")
    public @ResponseBody List<String[]> searchNames(@RequestParam String nombre, HttpServletRequest request) {

        List<String[]> names = exRepository.getNames(nombre);
        return names;
    }

    @GetMapping("/exercise/{nombre}")
    public String exercseM(@PathVariable String nombre, Model model,
            HttpServletRequest request) {
        Ejercicio exercise = exRepository.findByName(nombre).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Imagen image = exercise.getImagen();
        String imagePath = "logo.jpg";
        if (!(image == null)) {
            imagePath = image.getName();
            if (!imageService.verificarExistenciaImagen(imagePath)) {
                imageService.guardarImagen(image);
            }
        }
        model.addAttribute("id", exercise.getId());
        model.addAttribute("image", imagePath);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "details";
    }

    @GetMapping("/deleteEx/{id}")
    public String deleteEx(@PathVariable Long id) {
        exService.delete(id);
        return "muscleGroup";
    }

}
