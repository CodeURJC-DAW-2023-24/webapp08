package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Notificacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String contenido;
  private boolean leido;

  protected Notificacion() {

  }

  public Notificacion(String sender) {
    super();
    this.contenido = "Has recibido una solicitud de: " + sender;
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