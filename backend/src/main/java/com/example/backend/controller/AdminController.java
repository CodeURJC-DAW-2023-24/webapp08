package com.example.backend.controller;

import java.io.IOException;

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

import com.example.backend.model.Exercise;
import com.example.backend.model.Person;
import com.example.backend.model.Picture;
import com.example.backend.service.ExerciseService;
import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController implements CommandLineRunner {

    @Autowired
    private PersonService personService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private PictureService imageService;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/mainPage/exercForm")
    public String exForm() {
        return "exercFormAdmin";
    }

    @PostMapping("/deleteUser")
    public @ResponseBody Boolean deleteUser(@RequestParam Long id) {
        Person user = personService.findById(id);
        personService.deletePerson(user);
        return true;
    }

    @PostMapping("/mainPage/exercise/newExercise")
    public String newEx(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam MultipartFile image,
            @RequestParam("video") String video,
            @RequestParam("grp") String grp,
            Model model, HttpServletRequest request) throws InterruptedException {
        Optional<Exercise> existingExOptional = exerciseService.findByName(name);
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
            
                // Object Image
                Picture imageN = imageService.newPicture(image);
                Thread.sleep(1000);
                exerciseService.setImage(exercise, imageN);
            } catch (IOException e) {
            }
        }
        else{
            exerciseService.save(exercise);
        }
        Picture imageN = exercise.getImage();
        String imagePath = "logo.jpg";
        if (!(imageN == null)) {
            imagePath = imageN.getName();

        }
        Exercise ex = exerciseService.findByName(name).orElseThrow();
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
        exerciseService.deleteById(id);
        return "redirect:/mainPage/exerciseSearch";
    }

    @PostMapping("/editExerciseAdmin/{id}")
    public String editExercise(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam MultipartFile image,
            @RequestParam("video") String video,
            @RequestParam("grp") String grp,
            Model model, HttpServletRequest request,@PathVariable Long id) throws InterruptedException {
        Exercise editedEx = exerciseService.findById(id).orElseThrow();
        if (name.isEmpty() || description.isEmpty() || grp.isEmpty()) {
            model.addAttribute("erroMg", "Rellene todos los campos");
            return "error";
        }
        Optional<Exercise> newEx= exerciseService.findByName(name);
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
            
                // Object Image
                Picture imageN = imageService.newPicture(image);
                Thread.sleep(1000);
                exerciseService.setImage(editedEx, imageN);
            } catch (IOException e) {
            }
        }
        else{
            exerciseService.save(editedEx);
        }
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
