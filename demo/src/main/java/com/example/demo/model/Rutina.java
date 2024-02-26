package com.example.demo.model;



import java.sql.Date;

import jakarta.persistence.*;

@Entity
public class Rutina {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


    private String name;
    private Date date;
    private Integer time;

    public Rutina() {}



    public Rutina ( String name, Date date, Integer time){
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



    public void setDate(Date date) {
        this.date = date;
    }



    public Integer getTime() {
        return time;
    }



    public void setTime(Integer time) {
        this.time = time;
    }
    
}
