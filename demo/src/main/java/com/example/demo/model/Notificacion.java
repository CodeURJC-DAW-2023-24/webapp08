package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
//@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   /* @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;*/

    //private String tipo;
     //private Date fechaCreacion;
     
    private String contenido;
    private boolean leido;


    protected Notificacion() {
		// Used by JPA
	}
    public Notificacion(String sender) {
		super();
		this.contenido = "Has recibido una solicitud de: " + sender;
        this.leido = false;
		
	}
}