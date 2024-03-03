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
	private Rutine rutine;

	protected News() {
	}

	public News(String name) {
		super();
		this.name = name;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Rutine getRutine() {
		return rutine;
	}

	public void setRutine(Rutine rutine) {
		this.rutine = rutine;
	}


	public String getName() {
		return name;
	}
}