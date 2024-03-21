package com.example.backend.controller;

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

import com.example.backend.model.Comment;
import com.example.backend.model.ExRutine;
import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.ExRutineRepository;
import com.example.backend.repository.NewsRepository;
import com.example.backend.repository.PersonRepository;
import com.example.backend.repository.RutineRepository;
import com.example.backend.service.ExerciseService;
import com.example.backend.service.PersonService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RutineController implements CommandLineRunner {

    @Autowired
    private ExRutineRepository exRutineRepository;

    @Autowired
    private PersonService userService;

   
    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RutineRepository rutineRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/mainPage/showRutine")
    public String showRutine(Model model, @RequestParam Long id, HttpServletRequest request) {
        Person user = userRepository.findByRutineId(id).orElseThrow();
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        String alias = request.getUserPrincipal().getName();
        Person userSesion = userRepository.findByalias(alias).orElseThrow();
        if(user == userSesion){
            model.addAttribute("user", true);
        }
        else{
            model.addAttribute("user", false);
        }
        List<Comment> lComments = rutine.getMessages();
        for (Comment comment: lComments){
            comment.setOwn(comment.getName().equals(alias));
        }

        model.addAttribute("alias", user.getAlias());
        model.addAttribute("nameUser", user.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(rutine.getDate());
        model.addAttribute("date", formatedDate);
        model.addAttribute("pathName", rutine.getName());
        model.addAttribute("exercises", rutine.getExercises());
        model.addAttribute("messages", lComments);
        model.addAttribute("id", id);
        if (user.getImage() != null) {

            model.addAttribute("imagePath", user.getImage().getName());
        } else
            model.addAttribute("imagePath", "logo.jpg");

        return "rutine";
    }

    @PostMapping("/sendComment")
    public @ResponseBody Comment postMethodName(@RequestParam String comentario, @RequestParam Long id,
            HttpServletRequest request) {
        String alias = request.getUserPrincipal().getName();
        Person user = userRepository.findByalias(alias).orElseThrow();
        String aliasUser = user.getAlias();
        Comment message = new Comment(aliasUser, comentario);
        commentRepository.save(message);
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.getMessages().add(message);
        rutineRepository.save(rutine);

        return message;
    }

    @PostMapping("deleteComment")
	public @ResponseBody void deleteRutineComment(@RequestParam Long commentId, @RequestParam Long rutineId) {
        Rutine rutine = rutineRepository.findById(rutineId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        rutine.getMessages().remove(comment);
        rutineRepository.save(rutine);
        commentRepository.deleteById(commentId); 	
		
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
        String alias = request.getUserPrincipal().getName();
        Person user = userRepository.findByalias(alias).orElseThrow();

        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.setDate(date);
        rutine.setName(name);
        rutine.setTime(time);
        rutineRepository.save(rutine);

        user.getRutines().add(rutine);
        userRepository.save(user);

        List<Person> lFriends = user.getFriends();
        if (!lFriends.isEmpty()) {
            for (Person friend : lFriends) {
            News news = new News(alias);
            news.setRutine(rutine);
            newsRepository.save(news);
            friend.getNews().add(news);
            userRepository.save(friend);
            }
        }

        return "redirect:/mainPage";
    }

    @PostMapping("/mainPage/rutine/newExercise/{edit}/{id}")
    public String addExRutine(@PathVariable Long id,
    @PathVariable Boolean edit,
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
           /*  case "Hombro":
                name = shoulder;
                break;*/
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
        String alias = request.getUserPrincipal().getName();
        Person user = userRepository.findByalias(alias).orElseThrow();
        userService.increaseFreq(user, grp, name);
        exRutineRepository.save(exercise);
        rutine.addExRutine(exercise);
        rutineRepository.save(rutine);
        List<ExRutine> exercises = rutine.getExercises();
        model.addAttribute("id", id);
        model.addAttribute("exerciseList", exercises);
        if(edit){
            model.addAttribute("edit", true);
            model.addAttribute("date", rutine.getDate());
            model.addAttribute("name", rutine.getName());
            model.addAttribute("time", rutine.getTime());
        }

        return "addRutine";
    }

    @GetMapping("/mainPage/rutine/addEx/{edit}/{id}")
    public String addEX(@PathVariable Long id,@PathVariable Boolean edit, Model model, HttpServletRequest request) {
        String alias = request.getUserPrincipal().getName();
        Person user = userRepository.findByalias(alias).orElseThrow();
        List<Exercise> chest = exerciseService.findByGrp("Pecho");
        chest = userService.order(user, "Pecho", chest);
        model.addAttribute("chest", chest);
        List<Exercise> back = exerciseService.findByGrp("Espalda");
        back = userService.order(user, "Espalda", back);
        model.addAttribute("back", back);
        List<Exercise> shoulder = exerciseService.findByGrp("Hombro");
        shoulder = userService.order(user, "Hombro", shoulder);
        model.addAttribute("shoulder", shoulder);
        List<Exercise> biceps = exerciseService.findByGrp("Biceps");
        biceps = userService.order(user, "Biceps", biceps);
        model.addAttribute("biceps", biceps);
        List<Exercise> triceps = exerciseService.findByGrp("Triceps");
        triceps = userService.order(user, "Triceps", triceps);
        model.addAttribute("triceps", triceps);
        List<Exercise> lower = exerciseService.findByGrp("Inferior");
        lower = userService.order(user, "Inferior", lower);
        model.addAttribute("lower", lower);
        List<Exercise> cardio = exerciseService.findByGrp("Cardio");
        cardio = userService.order(user, "Cardio", cardio);
        model.addAttribute("cardio", cardio);
        model.addAttribute("id", id);
        model.addAttribute("edit", edit);
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
        model.addAttribute("edit", false);
        model.addAttribute("id", rutine.getId());
        return "addRutine";
    }

    @GetMapping("/mainPage/rutine/{id}")
    public String rutine(@PathVariable Long id, Model model) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutineRepository.save(rutine);
        model.addAttribute("id", id);
        model.addAttribute("exerciseList", rutine.getExercises());
        model.addAttribute("edit", false);
        return "addRutine";
    }
    @GetMapping("/deleteRutine/{id}")
    public String deleteRutine(@PathVariable Long id, Model model, HttpServletRequest request) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        List<News> listNews = (List<News>) newsRepository.findByRutineId(id);
        userService.deleteNewsById(listNews);
        for (News news :listNews){
            newsRepository.delete(news);
        }
        String alias = request.getUserPrincipal().getName();
        Person userSesion = userRepository.findByalias(alias).orElseThrow();
        userSesion.getRutines().remove(rutine);
        userRepository.save(userSesion);
        rutineRepository.delete(rutine);
        return "redirect:/mainPage";
    }
    @GetMapping("/editRutine/{id}")
    public String editRutine(@PathVariable Long id, Model model, HttpServletRequest request) {
        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        model.addAttribute("date", rutine.getDate());
        model.addAttribute("name", rutine.getName());
        model.addAttribute("time", rutine.getTime());
        model.addAttribute("exerciseList", rutine.getExercises());
        model.addAttribute("edit", true);
        return "/addRutine";
    }
    @PostMapping("/mainPage/editRutine/{id}")
    public String editRoutine(@PathVariable Long id,
            @RequestParam("date") Date date,
            @RequestParam("name") String name,
            @RequestParam("time") Integer time, Model model) {

        if (date == null || name.isEmpty() || time == 0){
            model.addAttribute("message", true);
            model.addAttribute("erroMg", "Rutina vacía");
            return "error";
        }

        Rutine rutine = rutineRepository.findById(id).orElseThrow();
        rutine.setDate(date);
        rutine.setName(name);
        rutine.setTime(time);
        rutineRepository.save(rutine);

       

        return "redirect:/mainPage";
    }
    @GetMapping("/deleteExRutine/{edit}/{id}")
    public String deleteExRutine(@PathVariable Long id, @PathVariable Boolean edit, Model model, HttpServletRequest request) {
        ExRutine exRutine = exRutineRepository.findById(id).orElseThrow();
        Rutine rutine = rutineRepository.findByExerciseId(id).orElseThrow();
        List<ExRutine> exercices = rutine.getExercises();
        exercices.remove(exRutine);
        rutineRepository.save(rutine);
        exRutineRepository.delete(exRutine);
       
        model.addAttribute("exerciseList", rutine.getExercises());
        if(edit){
            model.addAttribute("edit", true);
            model.addAttribute("date", rutine.getDate());
            model.addAttribute("name", rutine.getName());
            model.addAttribute("time", rutine.getTime());
        }
        return "/addRutine";
    }
}
