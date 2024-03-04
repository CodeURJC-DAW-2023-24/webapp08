package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String grp;
    private String video;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private Picture image;
    private boolean bImage;
    private String path;

    protected Exercise() {
    }

    public Exercise(String name, String description, String grp, String video) {
        super();

        this.name = name;
        this.grp = grp;
        this.video = video;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Picture getImage() {
        return image;
    }

    public void setImage(Picture image) {
        this.image = image;

    }

    public boolean getbImage() {
        return bImage;
    }

    public void setbImage(boolean bImage) {
        this.bImage = bImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}