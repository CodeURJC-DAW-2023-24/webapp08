package com.example.demo.model;



import java.sql.Date;
import java.util.List;


import jakarta.persistence.*;

@Entity
public class Rutina {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


    private String name;
    private Date date;
    private Integer time;
   

    @OneToMany (cascade = CascadeType.ALL)
    private List<Mensaje> mensajes;
    
    @OneToMany
    private List<EjerRutina> ejercicios;
    
    public void addEjerRutina(EjerRutina ejerRutina){
       
        ejercicios.add(ejerRutina);
    }
    public List<EjerRutina> getEjercicios() {
        return ejercicios;
    }



    public void setEjercicios(List<EjerRutina> ejercicios) {
        this.ejercicios = ejercicios;
    }



    public Rutina() {
    
    }



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



    public List<Mensaje> getMensajes() {
        return mensajes;
    }



    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    
}


