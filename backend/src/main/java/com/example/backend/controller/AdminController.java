package com.example.backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Person;
import com.example.backend.model.Picture;
import com.example.backend.model.Rutine;
import com.example.backend.repository.ExerciseRepository;
import com.example.backend.repository.NewsRepository;
import com.example.backend.repository.PersonRepository;
import com.example.backend.service.PictureService;

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
        
        List<Person> lAmigos =  user.getFriends();
        for(Person amigo: lAmigos) {
            amigo.getFriends().remove(user);
            userRepository.save(amigo);
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
            model.addAttribute("video", " ");

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
                imageN.setData(datosImage);
              
                imageService.savePicture(imageN);
                Thread.sleep(1000);

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
        model.addAttribute("grp", grp);
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

    @PostMapping("/editExerciseAdmin/{id}")
    public String editExercise(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam MultipartFile image,
            @RequestParam("video") String video,
            @RequestParam("grp") String grp,
            Model model, HttpServletRequest request,@PathVariable Long id) throws InterruptedException {
        Exercise editedEx = exerciseRepository.findById(id).orElseThrow();
        if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
            model.addAttribute("erroMg", "Rellene todos los campos");
            return "error";
        }
        Optional<Exercise> newEx= exerciseRepository.findByName(name);
        if(newEx.isPresent()){
            model.addAttribute("message", true);
			// existing user
			model.addAttribute("erroMg", "El ejercicio ya existe");
			return "error";
        }
        if (!name.equals(editedEx.getName())){
            editedEx.setName(name);
        }
        if (!description.equals(editedEx.getDescription())){
            editedEx.setDescription(description);
            
        }
        if (video.isEmpty()) {
            editedEx.setVideo("0");
            model.addAttribute("ExVideo", false);
            model.addAttribute("video", " ");

        } else if  (!video.equals(editedEx.getVideo())) {
            model.addAttribute("ExVideo", true);
            editedEx.setVideo(video);
            model.addAttribute("video", video);
        }
        if (!image.isEmpty()) {
            try {
                byte[] datosImage = image.getBytes();

                // Object Image
                Picture imageN = new Picture(null);
                imageN.setContent(image.getContentType());
                imageN.setName(image.getOriginalFilename());
                imageN.setData(datosImage);
              
                imageService.savePicture(imageN);
                Thread.sleep(1000);

                editedEx.setImage(imageN);
                editedEx.setbImage(true);
            } catch (IOException e) {
            }
        }
        exerciseRepository.save(editedEx);
        Picture imageN = editedEx.getImage();
        String imagePath = "logo.jpg";
        if (!(imageN == null)) {
            imagePath = imageN.getName();

        }
        
        Thread.sleep(500);
        model.addAttribute("video", video);
        model.addAttribute("grp", grp);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("image", imagePath);
        model.addAttribute("id", id);
        return "details";

    }
}
