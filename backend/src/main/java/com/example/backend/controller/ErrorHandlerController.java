package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ErrorHandlerController {     
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleMissingParams( Exception ex, Model model) { 
        model.addAttribute("message", true);
        model.addAttribute("erroMg", "Rellene todos los campos");   
         return "error";
    } 
}