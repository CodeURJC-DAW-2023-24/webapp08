package com.example.demo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstName;
	private String name;
	private String encodedPassword;
	private String date;
	private Integer weight;

	@ManyToMany 
	 @JoinTable( name = "friends", 
	 joinColumns = @JoinColumn(name = "person_id"), 
	 inverseJoinColumns = @JoinColumn(name = "personFriend_id") ) 
	 private List<Person> friends;
	 
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	@OneToMany (cascade = CascadeType.ALL)
    private List<Notification> lNotifications;

	@OneToMany (cascade = CascadeType.ALL)
	private List<Rutine> rutines;

	@OneToMany (cascade = CascadeType.ALL)
    private List<News> news;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Picture imagen;

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

	protected Person() {
		// Used by JPA
	}
	
	public Person(String firstName, String encodedPassword,String name,String date, Integer weight, String...roles) {
		super();
		this.firstName = firstName;
		this.encodedPassword = encodedPassword;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.roles = List.of(roles);
	}

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

	
	public Picture getImagen() {
		return imagen;
	}

	public void setImagen(Picture imagen) {
		this.imagen = imagen;
	
	}

	 public void setId(long id) {
		this.id = id;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}

	public void setLNotifications(List<Notification> lNotifications) {
		this.lNotifications = lNotifications;
	}

	public List<Rutine> getRutines() {
		return rutines;
	}

	public void setRutines(List<Rutine> rutines) {
		this.rutines = rutines;
	}
	
	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}
	
	public long getId() {
		return id;
	}
	
	public List<Person> getFriends() {
		return friends;
	}

	public List<Notification> getLNotifications() {
		return lNotifications;
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

}