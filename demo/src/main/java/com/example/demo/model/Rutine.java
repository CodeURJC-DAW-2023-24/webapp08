package com.example.demo.model;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Rutine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Date date;
    private Integer time;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> mensajes;

    @OneToMany
    private List<ExRutine> ejercicios;

    public void addEjerRutina(ExRutine ejerRutina) {

        ejercicios.add(ejerRutina);
    }

    public List<ExRutine> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<ExRutine> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public Rutine() {

    }

    public Rutine(String name, Date date, Integer time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }
}
