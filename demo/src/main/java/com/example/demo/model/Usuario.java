package com.example.demo.model;

import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;
	private String encodedPassword;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	protected Usuario() {
		// Used by JPA
	}

	public Usuario(String firstName, String encodedPassword, String... roles) {
		super();
		this.firstName = firstName;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public List<String> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return String.format("Usuario[id=%d, firstName='%s', password='%s']",
				id, firstName, encodedPassword);
	}

}