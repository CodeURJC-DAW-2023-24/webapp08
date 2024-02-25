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

    public Ejercicio() {}

    public Ejercicio( String name, String description, String grp, String video) {
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
    public void setName(String name){
        this.name=name;
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
    public void setGrp(String grp){
        this.grp= grp;
    }
    public void setVideo(String video){
        this.video=video;
    }

    public void setDescription(String description){
        this.description= description;
    }

    @Override
    public String toString() {
        return String.format("Ejercicio[id=%d, name='%s', grp='%s',video='%s',description='%s']",
                id, name,grp,video,description);
    }

}