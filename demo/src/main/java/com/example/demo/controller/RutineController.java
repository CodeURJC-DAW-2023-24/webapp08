package com.example.demo.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Comment;
import com.example.demo.model.ExRutine;
import com.example.demo.model.Exercise;
import com.example.demo.model.News;
import com.example.demo.model.Person;
import com.example.demo.model.Rutine;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ExRutineRepository;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RutineRepository;
import com.example.demo.service.PersonService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RutineController implements CommandLineRunner {

    @Autowired
    private ExRutineRepository exRutineRepository;

    @Autowired
    private PersonService userService;

    @Autowired
    private ExerciseRepository exRepository;

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RutineRepository rutineRepository;

    @Autowired
    private CommentRepository messageRepository;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/mainPage/showRutine")
    public String showRutine(Model model, @RequestParam Long id, HttpServletRequest request) {
        Person user = userRepository.findByRutineId(id).orElseThrow();
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("nameUser", user.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(rutine.getDate());
        model.addAttribute("date", formatedDate);
        model.addAttribute("pathName", rutine.getName());
        model.addAttribute("exercises", rutine.getExercises());
        model.addAttribute("messages", rutine.getMessages());
        model.addAttribute("id", id);
        if (user.getImagen() != null) {

            model.addAttribute("imagePath", user.getImagen().getName());
        } else
            model.addAttribute("imagePath", "logo.jpg");

        return "rutine";
    }

    @PostMapping("/sendComment")
    public @ResponseBody Comment postMethodName(@RequestParam String comentario, @RequestParam Long id,
            HttpServletRequest request) {
        String nameUser = request.getUserPrincipal().getName();
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();
        String firstNameUser = user.getFirstName();
        Comment message = new Comment(firstNameUser, comentario);
        messageRepository.save(message);
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.getMessages().add(message);
        rutineRepository.save(rutine);

        return message;
    }

    @PostMapping("/mainPage/newRoutine/{id}")
    public String newRoutine(@PathVariable Long id,
            @RequestParam("date") Date date,
            @RequestParam("name") String name,
            @RequestParam("time") Integer time, HttpServletRequest request, Model model) {

        if (date == null || name.isEmpty() || time == 0){
            model.addAttribute("message", true);
            model.addAttribute("erroMg", "Rutina vacía");
            return "error";
        }
        String nameUser = request.getUserPrincipal().getName();
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();

        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.setDate(date);
        rutine.setName(name);
        rutine.setTime(time);
        rutineRepository.save(rutine);

        user.getRutines().add(rutine);
        userRepository.save(user);

        List<Person> lFriends = user.getFriends();
        if (!lFriends.isEmpty()) {
            News news = new News(nameUser);
            news.setRutine(rutine);
            newsRepository.save(news);
            for (Person friend : lFriends) {
                friend.getNews().add(news);
                userRepository.save(friend);
            }
        }

        return "redirect:/mainPage";
    }

    @PostMapping("/mainPage/rutine/newExercise/{id}")
    public String addExRutine(@PathVariable Long id,
            @RequestParam("grp") String grp,
            @RequestParam("chest") String chest,
            @RequestParam("back") String back,
            @RequestParam("shoulder") String shoulder,
            @RequestParam("biceps") String biceps,
            @RequestParam("triceps") String triceps,
            @RequestParam("lower") String lower,
            @RequestParam("cardio") String cardio,
            @RequestParam("series") String series,
            @RequestParam("weight") Integer weight, HttpServletRequest request, Model model) {

        if (series == null)
            return "error";
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        String name = " ";
        switch (grp) {
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
        ExRutine exercise = new ExRutine(grp, name, series, weight);
        String nameUser = request.getUserPrincipal().getName();
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();
        userService.increaseFreq(user, grp, name);
        exRutineRepository.save(exercise);
        rutine.addExRutine(exercise);
        rutineRepository.save(rutine);
        List<ExRutine> exercises = rutine.getExercises();
        model.addAttribute("id", id);
        model.addAttribute("exerciseList", exercises);

        return "addRutine";
    }

    @GetMapping("/mainPage/rutine/addEx/{id}")
    public String addEX(@PathVariable Long id, Model model, HttpServletRequest request) {
        String nameUser = request.getUserPrincipal().getName();
        Person user = userRepository.findByFirstName(nameUser).orElseThrow();
        List<Exercise> chest = exRepository.findByGrp("Pecho");
        chest = userService.order(user, "Pecho", chest);
        model.addAttribute("chest", chest);
        List<Exercise> back = exRepository.findByGrp("Espalda");
        back = userService.order(user, "Espalda", back);
        model.addAttribute("back", back);
        List<Exercise> shoulder = exRepository.findByGrp("Hombro");
        shoulder = userService.order(user, "Hombro", shoulder);
        model.addAttribute("shoulder", shoulder);
        List<Exercise> biceps = exRepository.findByGrp("Biceps");
        biceps = userService.order(user, "Biceps", biceps);
        model.addAttribute("biceps", biceps);
        List<Exercise> triceps = exRepository.findByGrp("Triceps");
        triceps = userService.order(user, "Triceps", triceps);
        model.addAttribute("triceps", triceps);
        List<Exercise> lower = exRepository.findByGrp("Inferior");
        lower = userService.order(user, "Inferior", lower);
        model.addAttribute("lower", lower);
        List<Exercise> cardio = exRepository.findByGrp("Cardio");
        cardio = userService.order(user, "Cardio", cardio);
        model.addAttribute("cardio", cardio);
        model.addAttribute("id", id);
        return "addExRutine";
    }

    @SuppressWarnings("null")
    @GetMapping("/cancel/{id}")
    public String addRutine(@PathVariable Long id, Model model) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.delete(rutine);
        return "redirect:/mainPage";
    }

    @GetMapping("/mainPage/rutine/addRutine")
    public String cancelRutine(Model model) {
        Rutine rutine = new Rutine(null, null, null);
        rutineRepository.save(rutine);
        model.addAttribute("id", rutine.getId());
        return "addRutine";
    }

    @GetMapping("/mainPage/rutine/{id}")
    public String rutine(@PathVariable Long id, Model model) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.save(rutine);
        model.addAttribute("id", id);
        return "addRutine";
    }
}
