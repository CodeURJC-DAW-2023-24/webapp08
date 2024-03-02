package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String grp;
    private String video;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private Imagen imagen;
    private boolean tieneImagen;
    private String ruta;

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;

    }

    public boolean getTieneImagen() {
        return tieneImagen;
    }

    public void setTieneImagen(boolean tieneImagen) {
        this.tieneImagen = tieneImagen;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Ejercicio() {
    }

    public Ejercicio(String name, String description, String grp, String video) {
        super();

        this.name = name;
        this.grp = grp;
        this.video = video;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrp() {
        return grp;
    }

    public String getDescription() {
        return description;
    }

    public String getVideo() {
        return video;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Ejercicio[id=%d, name='%s', grp='%s',video='%s',description='%s']",
                id, name, grp, video, description);
    }

}