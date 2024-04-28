package com.example.backend.DTO;

import java.util.Arrays;
import java.util.List;


import com.example.backend.model.News;
import com.example.backend.model.Notification;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;
import com.example.backend.service.PersonService;


public class PersonDTO {




    private long id;
    private String alias;
	private String name;
    private String date;
	private Integer weight;
    private List<String> friends;
    private List<String> roles;
    private List<Notification> lNotifications;
    private List<Rutine> rutines;
    private List<News> news;
	

   public PersonDTO(){
    
   }

    public PersonDTO(Person person, PersonService personService){
        this.id = person.getId();
        this.alias = person.getAlias();
        this.name = person.getName();
        this.date= person.getDate();
        this.weight = person.getWeight();
        this.friends = personService.findAliasofFriendsByPerson(person); 
        this.lNotifications = person.getlNotifications();
        this.rutines = person.getRutines();
        this.news = person.getNews();
        this.roles=person.getRoles();
    }

 

    public long getId() {
        return id;
    }



    public void setId(long id) {
        this.id = id;
    }



    public String getAlias() {
        return alias;
    }



    public void setAlias(String alias) {
        this.alias = alias;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }



    public Integer getWeight() {
        return weight;
    }



    public void setWeight(Integer weight) {
        this.weight = weight;
    }



    public List<String> getFriends() {
        return friends;
    }



    public void setFriends(List<String> friends) {
        this.friends = friends;
    }



    public List<String> getRoles() {
        return roles;
    }



    public void setRoles(List<String> roles) {
        this.roles = roles;
    }



    public List<Notification> getlNotifications() {
        return lNotifications;
    }



    public void setlNotifications(List<Notification> lNotifications) {
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



   

    
}
