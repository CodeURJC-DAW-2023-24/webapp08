package com.example.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.DTO.RutineDTO;
import com.example.backend.model.Rutine;
import com.example.backend.repository.PersonRepository;
import com.example.backend.repository.RutineRepository;
import com.example.backend.service.PersonService;
import com.example.backend.service.RutineService;

@RestController
@RequestMapping("api/rutines")
public class RESTRutineController {
    @Autowired
    private RutineService rutineService;
    @Autowired
    private PersonService personService;

    @GetMapping("/")
	public List<RutineDTO> getRutines() {
		List<Rutine> rutines=  rutineService.findAll();
        List<RutineDTO> rutineDTOs= new ArrayList<>();;
        for (Rutine rutine: rutines){
            RutineDTO rutineDTO= new RutineDTO(rutine,personService);
            rutineDTOs.add(rutineDTO);
        }

        return rutineDTOs;
	}

    
}
