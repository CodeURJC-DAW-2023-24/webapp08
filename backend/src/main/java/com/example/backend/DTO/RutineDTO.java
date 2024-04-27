package com.example.backend.DTO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.backend.model.Comment;
import com.example.backend.model.ExRutine;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;
import com.example.backend.service.PersonService;

public class RutineDTO {

    private Long id;
    private String name;
    private Date date;
    private Integer time;
    private List<Comment> lComments;
    private List<ExRutine> exercises;
    private String person;
    
    public RutineDTO(Rutine rutine, PersonService personService){
        this.id = rutine.getId();
        this.name= rutine.getName();
        Date newRutineDate = rutine.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newRutineDate);
        //calendar.add(Calendar.DAY_OF_YEAR, +1);
        newRutineDate = calendar.getTime();
        this.date= newRutineDate;
        this.time= rutine.getTime();
        this.lComments= rutine.getMessages();
        this.exercises= rutine.getExercises();
        Person user = personService.findByRutineId(id).orElseThrow();
        this.person = user.getAlias();
    }
    public RutineDTO(){
        
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

    public List<Comment> getlComments() {
        return lComments;
    }

    public void setlComments(List<Comment> lComments) {
        this.lComments = lComments;
    }

    public List<ExRutine> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExRutine> exercises) {
        this.exercises = exercises;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

}
