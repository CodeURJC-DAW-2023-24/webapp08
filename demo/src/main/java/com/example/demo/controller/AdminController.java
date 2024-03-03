package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Exercise;
import com.example.demo.model.News;
import com.example.demo.model.Person;
import com.example.demo.model.Picture;
import com.example.demo.model.Rutine;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PictureService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController implements CommandLineRunner {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private PictureService imageService;

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/mainPage/exercForm")
    public String exForm() {
        return "exercFormAdmin";
    }

    @PostMapping("/deleteUser")
    public @ResponseBody Boolean deleteUser(@RequestParam Long id) {
        Person user = userRepository.findById(id).orElseThrow();
        List<Rutine> lrutines = user.getRutines();
        for (Rutine rutine : lrutines) {
            List<Person> lUsers = user.getFriends();
            Optional<News> news = newsRepository.findByRutine(rutine);
            for (Person friend : lUsers) {
                if (news.isPresent()) {
                    friend.getNews().remove(news.get());
                    friend.getFriends().remove(user);
                    userRepository.save(friend);
                }
            }
            if (news.isPresent()) {
                newsRepository.delete(news.get());
            }
        }

        user.getFriends().clear();
        userRepository.save(user);
        userRepository.deleteById(id);
        return true;
    }

    @PostMapping("/mainPage/exercise/newExercise")
    public String newEx(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam MultipartFile image,
            @RequestParam("video") String video,
            @RequestParam("grp") String grp,
            Model model, HttpServletRequest request) throws InterruptedException {
        Optional<Exercise> existingExOptional = exerciseRepository.findByName(name);
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
                Picture imageN = new Picture(null);
                imageN.setContent(image.getContentType());
                imageN.setName(image.getOriginalFilename());
                imageN.setDatos(datosImage);
                imageService.guardarImagen(imageN);
                exercise.setImage(imageN);
                exercise.setbImage(true);
            } catch (IOException e) {
            }
        }
        exerciseRepository.save(exercise);
        Picture imageN = exercise.getImage();
        String imagePath = "logo.jpg";
        if (!(imageN == null)) {
            imagePath = imageN.getName();

        }
        Exercise ex = exerciseRepository.findByName(name).orElseThrow();
        Thread.sleep(500);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("image", imagePath);
        model.addAttribute("id", ex.getId());
        return "details";

    }

    @GetMapping("/deleteEx/{id}")
    public String deleteEx(@PathVariable Long id) {
        exerciseRepository.deleteById(id);
        return "redirect:/mainPage/exerciseSearch";
    }
}
