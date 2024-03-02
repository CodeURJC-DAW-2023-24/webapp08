package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EjerRutina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String grupo;
    private String ejercicio;
    private String series;
    private Integer peso;

    public EjerRutina() {
    }

    public Integer getPeso() {
        return peso;
    }

    public EjerRutina(String grupo, String ejercicio, String series, Integer peso) {
        this.ejercicio = ejercicio;
        this.series = series;
        this.grupo = grupo;
        this.peso = peso;

    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

}