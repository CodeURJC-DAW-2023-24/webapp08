package com.example.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String alias;

	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
	private Rutine rutine;

	protected News() {
	}

	public News(String alias) {
		super();
		this.alias = alias;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Rutine getRutine() {
		return rutine;
	}

	public void setRutine(Rutine rutine) {
		this.rutine = rutine;
	}


	public String getAlias() {
		return alias;
	}
}
