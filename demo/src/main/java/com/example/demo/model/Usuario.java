package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;
	private String name;
	private String encodedPassword;
	private String date;
	private Integer weight;
	@OneToOne(cascade = CascadeType.ALL)
	private Imagen imagen;

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	public Usuario() {
		// Used by JPA
	}

	public Usuario(String firstName, String encodedPassword,String name,String date, Integer weight, String...roles) {
		super();
		this.firstName = firstName;
		this.encodedPassword = encodedPassword;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.roles = List.of(roles);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getName() {
		return name;
	}
	public String getDate() {
		return date;
	}
	public Integer getWeight() {
		return weight;
	}


	public String getEncodedPassword() {
		return encodedPassword;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles){
		this.roles = roles;
	}

	public void setName(String name){
		this.name=name;
	}

	public void setFirstName(String firstName){
		this.firstName=firstName;
	}
	public void setDate(String date){
		this.date= date;
	}

	public void setWeight(Integer weight){
		this.weight=weight;
	}

	@Override
	public String toString() {
		return String.format("Usuario[id=%d, firstName='%s', password='%s']",
				id, firstName, encodedPassword);
	}

}