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
	private String alias;
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
    private Map<String, Integer> chestFrec = new HashMap<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> shoulderFrec = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> backFrec = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> bicepsFrec = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> tricepsFrec = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> lowerFrec = new HashMap<>();

	
	@ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> cardioFrec = new HashMap<>();

	protected Person() {
		// Used by JPA
	}
	
	public Person(String alias, String encodedPassword,String name,String date, Integer weight, String...roles) {
		super();
		this.alias = alias;
		this.encodedPassword = encodedPassword;
		this.name = name;
		this.date = date;
		this.weight = weight;
		this.roles = List.of(roles);
	}

	public Map<String, Integer> getFrecuencia(String grp){
		switch (grp) {
			case "Pecho":
                return chestFrec;
               
            case "Espalda":
			return backFrec;

            case "Hombro":
			return shoulderFrec;
              
            case "Biceps":
			return bicepsFrec;
             
            case "Triceps":
			return tricepsFrec;
          
            case "Inferior":
			return lowerFrec;
               
            default:
			return cardioFrec;
               
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
	
	public String getalias() {
		return alias;
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

	public void setalias(String alias){
		this.alias=alias;
	}
	public void setDate(String date){
		this.date= date;
	}

	public void setWeight(Integer weight){
		this.weight=weight;
	}

}