package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  protected Notification() {

  }

  public Notification(String sender) {
    super();
    this.content = "Has recibido una solicitud de: " + sender;
  }

  public String getContent() {
    return content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setContent(String content) {
    this.content = content;
  }
}