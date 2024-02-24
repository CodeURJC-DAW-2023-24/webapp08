package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String date;
    private String name;
    private Integer time;

    public Ejercicio() {

    }

    public Ejercicio(String date, String name,Integer time) {
        super();
        this.date = date;
        this.name = name;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
    public Integer getTime() {
        return time;
    }
    public void setName(String name){
        this.name=name;
    }

    public void setDate(String date){
        this.date= date;
    }

    public void setTime(Integer time){
        this.time=time;
    }

    @Override
    public String toString() {
        return String.format("Ejercicio[id=%d, date='%s', name='%s', time='%d']",
                id, date, name, time);
    }

}