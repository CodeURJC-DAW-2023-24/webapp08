package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ExRutine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String grp;
    private String exercise;
    private String series;
    private Integer weight;

    public ExRutine() {
    }

    public Integer getWeight() {
        return weight;
    }

    public ExRutine(String grp, String exercise, String series, Integer weight) {
        this.exercise = exercise;
        this.series = series;
        this.grp = grp;
        this.weight = weight;

    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

}