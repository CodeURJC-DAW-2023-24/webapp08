package com.example.demo.model;

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
    public String getContenido() {
      return contenido;
    }
    public Long getId() {
      return id;
    }
    public void setId(Long id) {
      this.id = id;
    }
    public void setContenido(String contenido) {
      this.contenido = contenido;
    }
    public boolean isLeido() {
      return leido;
    }
    public void setLeido(boolean leido) {
      this.leido = leido;
    }

    @Override
	public String toString() {
		return String.format("Notificacion[id=%d, contenido='%s', leido='%s']",
				id, contenido, leido);
	}
  
}