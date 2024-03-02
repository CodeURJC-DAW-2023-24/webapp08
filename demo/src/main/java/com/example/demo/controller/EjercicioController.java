package com.example.demo.controller;

import com.example.demo.repository.ExRutineRepository;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.RutineRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.EjercicioService;
import com.example.demo.service.ImagenService;
import com.example.demo.service.UserService;
import com.example.demo.model.Exercise;
import com.example.demo.model.Rutine;
import com.example.demo.model.Person;
import com.example.demo.model.ExRutine;
import com.example.demo.model.Picture;
import com.example.demo.model.News;

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
    private ExerciseRepository exRepository;

    @Autowired
    private EjercicioService exService;

    @Autowired
    private UserService userService;

    @Autowired
    private RutineRepository rutineRepository;

    @Autowired
    private ExRutineRepository exRutineRepository;

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

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
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();

        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.setDate(date);
        rutine.setName(name);
        rutine.setTime(time);
        rutineRepository.save(rutine);

        user.getRutinas().add(rutine);
        userRepository.save(user);

        List<Person> lFriends = user.getAmigos();
        if (!lFriends.isEmpty()) {
            News news = new News(nameUser);
            news.setRutina(rutine);
            newsRepository.save(news);
            for (Person friend : lFriends) {
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
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
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
        ExRutine exercise = new ExRutine(grupo, name, series, peso);
        String nameUser = request.getUserPrincipal().getName();
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();
        userService.increaseFreq(user, grupo, name);
        exRutineRepository.save(exercise);
        rutine.addEjerRutina(exercise);
        rutineRepository.save(rutine);
        List<ExRutine> exercises = rutine.getEjercicios();
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
        Optional<Exercise> existingExOptional = exRepository.findByName(name);
        if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
            model.addAttribute("erroMg", "Rellene todos los campos");
            return "error";
        }

        if (existingExOptional.isPresent()) {
            model.addAttribute("erroMg", "El ejercicio ya existe");
            return "error";
        }
        Exercise exercise = new Exercise(name, description, grp, "0");
        if (video.isEmpty()) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            exercise.setVideo(video);
            model.addAttribute("video", video);
        }
        if (!image.isEmpty()) {
            try {
                byte[] datosImage = image.getBytes();

                // Object Image
                Picture imageN = new Picture();
                imageN.setContenido(image.getContentType());
                imageN.setName(image.getOriginalFilename());
                imageN.setDatos(datosImage);
                imageService.guardarImagen(imageN);
                exercise.setImage(imageN);
                exercise.setbImage(true);
            } catch (IOException e) {
            }
        }
        exRepository.save(exercise);
        Picture imageN = exercise.getImage();
        String imagePath = "logo.jpg";
        if (!(imageN == null)) {
            imagePath = imageN.getName();

        }
        Exercise ex = exRepository.findByName(name).orElseThrow();
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
        Page<Exercise> exs = exRepository.findByGrp(grupo, pageable);
        List<Exercise> exerciseList = exs.getContent();
        for (Exercise exercise : exerciseList) {
            Picture image = exercise.getImage();
            String imagePath = "logo.jpg";
            if (!(image == null)) {
                exercise.setbImage(true);
                imagePath = image.getName();
                if (!imageService.verificarExistenciaImagen(imagePath)) {
                    imageService.guardarImagen(image);
                }
                exercise.setPath(imagePath);
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
        Exercise exercise = exRepository.findById(id).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Picture image = exercise.getImage();
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
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();
        List<Exercise> chest = exRepository.findByGrp("Pecho");
        chest = userService.order(user, "Pecho", chest);
        model.addAttribute("pecho", chest);
        List<Exercise> back = exRepository.findByGrp("Espalda");
        back = userService.order(user, "Espalda", back);
        model.addAttribute("espalda", back);
        List<Exercise> shoulder = exRepository.findByGrp("Hombro");
        shoulder = userService.order(user, "Hombro", shoulder);
        model.addAttribute("hombro", shoulder);
        List<Exercise> biceps = exRepository.findByGrp("Biceps");
        biceps = userService.order(user, "Biceps", biceps);
        model.addAttribute("biceps", biceps);
        List<Exercise> triceps = exRepository.findByGrp("Triceps");
        triceps = userService.order(user, "Triceps", triceps);
        model.addAttribute("triceps", triceps);
        List<Exercise> lower = exRepository.findByGrp("Inferior");
        lower = userService.order(user, "Inferior", lower);
        model.addAttribute("inferior", lower);
        List<Exercise> cardio = exRepository.findByGrp("Cardio");
        cardio = userService.order(user, "Cardio", cardio);
        model.addAttribute("cardio", cardio);
        model.addAttribute("id", id);
        return "adEjerRutina";
    }

    @SuppressWarnings("null")
    @GetMapping("/cancel/{id}")
    public String adRutine(@PathVariable Long id, Model model) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.delete(rutine);
        return "redirect:/main";
    }

    @GetMapping("/adRutine")
    public String cancelRutine(Model model) {
        Rutine rutine = new Rutine();
        rutineRepository.save(rutine);
        model.addAttribute("id", rutine.getId());
        return "adRutine";
    }

    @GetMapping("/rutine/{id}")
    public String rutine(@PathVariable Long id, Model model) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
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
        Exercise exercise = exRepository.findByName(nombre).orElseThrow();
        model.addAttribute("name", exercise.getName());
        model.addAttribute("description", exercise.getDescription());
        if (exercise.getVideo().equals("0")) {
            model.addAttribute("ExVideo", false);

        } else {
            model.addAttribute("ExVideo", true);
            model.addAttribute("video", exercise.getVideo());
        }
        Picture image = exercise.getImage();
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
