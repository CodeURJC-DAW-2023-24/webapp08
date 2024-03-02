package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	@OneToOne
	private Rutine rutina;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Rutine getRutina() {
		return rutina;
	}

	public void setRutina(Rutine rutina) {
		this.rutina = rutina;
	}

	protected News() {
	}

	public News(String name) {
		super();
		this.name = name;

	}

	public String getName() {
		return name;
	}
}
