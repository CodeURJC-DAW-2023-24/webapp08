package com.example.backend.controller;

import com.example.backend.repository.ExerciseRepository;
import com.example.backend.service.PictureService;
import com.example.backend.model.Exercise;
import com.example.backend.model.Picture;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExerciseController implements CommandLineRunner {
    @Autowired
    private PictureService imageService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public void run(String... args) throws Exception {
    }

    @GetMapping("/mainPage/exerciseSearch")
    public String muscGr(Model model, HttpServletRequest request) {
        model.addAttribute("search", true);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "muscleGroup";
    }

    @GetMapping("/mainPage/group/{grp}/")
    public String group2(@PathVariable String grp, Model model, HttpServletRequest request, Pageable page) {
        Pageable pageable = PageRequest.of(page.getPageNumber(), 5);
        Page<Exercise> exs = exerciseRepository.findByGrp(grp, pageable);
        List<Exercise> exerciseList = exs.getContent();
        for (Exercise exercise : exerciseList) {
            Picture image = exercise.getImage();
            String imagePath = "logo.jpg";
            if (!(image == null)) {
                exercise.setbImage(true);
                imagePath = image.getName();
                if (!imageService.verifyPictureExistance(imagePath)) {
                    imageService.savePicture(image);
                }
                exercise.setPath(imagePath);
            }
        }

        model.addAttribute("exs", exerciseList);
        model.addAttribute("grp", grp);
        model.addAttribute("hasPrev", exs.hasPrevious());
        model.addAttribute("hasNext", exs.hasNext());
        model.addAttribute("nextPage", exs.getNumber() + 1);
        model.addAttribute("prevPage", exs.getNumber() - 1);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));

        return "lists";

    }

    @GetMapping("/mainPage/group/{grp}/{id}")
    public String exDetails(@PathVariable String grp, @PathVariable long id, Model model,
            HttpServletRequest request) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow();
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

    @GetMapping("/searchEx")
    public @ResponseBody List<String[]> searchNames(@RequestParam String nombre, HttpServletRequest request) {

        List<String[]> names = exerciseRepository.getNames(nombre);
        return names;
    }

    @GetMapping("/mainPage/exerciseSearch/exercise/{nombre}")
    public String exercseM(@PathVariable String nombre, Model model,
            HttpServletRequest request) {
        Exercise exercise = exerciseRepository.findByName(nombre).orElseThrow();
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
            if (!imageService.verifyPictureExistance(imagePath)) {
                imageService.savePicture(image);
            }
        }
        model.addAttribute("id", exercise.getId());
        model.addAttribute("image", imagePath);
        model.addAttribute("adEx", request.isUserInRole("ADMIN"));
        return "details";
    }

}
