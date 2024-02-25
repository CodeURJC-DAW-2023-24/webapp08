package com.example.demo.controller;

import com.example.demo.model.Ejercicio;
import com.example.demo.repository.EjercicioRepo;
import com.example.demo.service.EjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EjercicioController implements CommandLineRunner {

    @Autowired
    private EjercicioRepo repository;

    @Autowired
    private EjercicioService service;

    @Override
    public void run(String... args) throws Exception {
    }

    @PostMapping("/add")
    public String register(@RequestParam("date") String date,
                           @RequestParam("name") String name,
                           @RequestParam("time") Integer time) {

        Optional<Ejercicio> existingEjercicioOptional = repository.findByName(name);
        if (date.isEmpty() || name.isEmpty() || time == 0) return "error";
        if (existingEjercicioOptional.isPresent()) return "error";

        service.save(date, name, time);

        return "index";
    }
   
}
