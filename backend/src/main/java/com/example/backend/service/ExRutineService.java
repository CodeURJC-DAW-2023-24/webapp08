package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.ExRutine;
import com.example.backend.repository.ExRutineRepository;



@Service
public class ExRutineService {
    @Autowired
	private ExRutineRepository exRutineRepository;

    public void save(ExRutine exRutine) {
		exRutineRepository.save(exRutine);

	}

    public ExRutine findById(long id) {
		return exRutineRepository.findById(id).orElseThrow();
	}

    public void delete(ExRutine exRutine){
        exRutineRepository.delete(exRutine);
    }
}
