package com.example.demo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	

	private String firstName;
	private String name;
	private String encodedPassword;
	private String date;
	private Integer weight;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Imagen imagen;

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	
	}

	 public void setId(long id) {
		this.id = id;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}

	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	public List<Rutina> getRutinas() {
		return rutinas;
	}

	public void setRutinas(List<Rutina> rutinas) {
		this.rutinas = rutinas;
	}

	@ManyToMany 
	 @JoinTable( name = "amigos", 
	 joinColumns = @JoinColumn(name = "usuario_id"), 
	 inverseJoinColumns = @JoinColumn(name = "amigo_id") ) 
	 private List<Usuario> amigos;
	 
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	@OneToMany (cascade = CascadeType.ALL)
    private List<Notificacion> notificaciones;

	@OneToMany (cascade = CascadeType.ALL)
	private List<Rutina> rutinas;

	@OneToMany (cascade = CascadeType.ALL)
    private List<Novedad> novedades;

	
	public List<Novedad> getNovedades() {
		return novedades;
	}

	public void setNovedades(List<Novedad> novedades) {
		this.novedades = novedades;
	}

	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaPecho = new HashMap<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaHombro = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaEspalda = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaBiceps = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaTriceps = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaInferior = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> frecuenciaCardio = new HashMap<>();

	public Map<String, Integer> getFrecuencia(String grupo){
		switch (grupo) {
			case "Pecho":
                return frecuenciaPecho;
               
            case "Espalda":
			return frecuenciaEspalda;

            case "Hombro":
			return frecuenciaHombro;
              
            case "Biceps":
			return frecuenciaBiceps;
             
            case "Triceps":
			return frecuenciaTriceps;
          
            case "Inferior":
			return frecuenciaInferior;
               
            default:
			return frecuenciaCardio;
               
        }
	}
	

	public Usuario() {
		// Used by JPA
	}

	public Usuario(String firstName, String encodedPassword,String name,String date, Integer weight, String...roles) {
		super();
		this.firstName = firstName;
		this.encodedPassword = encodedPassword;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.roles = List.of(roles);
	}

	
	public long getId() {
		return id;
	}
	
	public List<Usuario> getAmigos() {
		return amigos;
	}

	public List<Notificacion> getNotificaciones() {
		return notificaciones;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getName() {
		return name;
	}
	public String getDate() {
		return date;
	}
	public Integer getWeight() {
		return weight;
	}


	public String getEncodedPassword() {
		return encodedPassword;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles){
		this.roles = roles;
	}

	public void setName(String name){
		this.name=name;
	}

	public void setFirstName(String firstName){
		this.firstName=firstName;
	}
	public void setDate(String date){
		this.date= date;
	}

	public void setWeight(Integer weight){
		this.weight=weight;
	}

	@Override
	public String toString() {
		return String.format("Usuario[id=%d, firstName='%s', password='%s']",
				id, firstName, encodedPassword);
	}

}