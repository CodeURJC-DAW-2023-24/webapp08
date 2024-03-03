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
    private List<Comment> lComments;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExRutine> exercises;

    protected Rutine() {

    }

    public Rutine(String name, Date date, Integer time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public void addExRutine(ExRutine exRutine) {

        exercises.add(exRutine);
    }

    public List<ExRutine> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExRutine> exercises) {
        this.exercises = exercises;
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

    public List<Comment> getMessages() {
        return lComments;
    }

    public void setMessages(List<Comment> lComments) {
        this.lComments = lComments;
    }

    

    
}
