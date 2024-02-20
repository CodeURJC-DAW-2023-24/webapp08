package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Novedad {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	


	protected Novedad() {
		// Used by JPA
	}

	public Novedad(String name) {
		super();
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	


	@Override
	public String toString() {
		return String.format("Novedad[id=%d, name='%s']",
				id, name);
	}

}
