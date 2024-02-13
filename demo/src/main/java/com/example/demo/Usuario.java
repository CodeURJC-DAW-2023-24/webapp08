package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;
	private String password;

	protected Usuario() {
		// Used by JPA
	}

	public Usuario(String firstName, String password) {
		super();
		this.firstName = firstName;
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return password;
	}


	@Override
	public String toString() {
		return String.format("Usuario[id=%d, firstName='%s', password='%s']",
				id, firstName, password);
	}

}