package com.example.backend.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.PersonDTO;
import com.example.backend.model.Comment;
import com.example.backend.model.News;
import com.example.backend.model.Notification;
import com.example.backend.model.Person;
import com.example.backend.model.Picture;
import com.example.backend.model.Rutine;
import com.example.backend.repository.PersonRepository;
import com.example.backend.service.CommentService;
import com.example.backend.service.NewsService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;
import com.example.backend.service.RutineService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/persons")
public class RESTPersonController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PersonService personService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	NewsService newsService;

	@Autowired
	private RutineService rutineService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private NewsService newsService;

	@GetMapping("/")
	public ResponseEntity<?> getPerson(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		PersonDTO personDTO = new PersonDTO(person, personService);
		return ResponseEntity.ok(personDTO);

	}

	@PostMapping("/")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		person.setEncodedPassword(passwordEncoder.encode(person.getEncodedPassword()));
		personService.save(person);
		URI location = fromCurrentRequest().path("{id}").buildAndExpand(person.getId()).toUri();

		return ResponseEntity.created(location).body(person);
	}

	@PatchMapping("/")
	public ResponseEntity<?> editPerson(HttpServletRequest request, // check if it goes into the body and not into the
																	// url
			@RequestParam(required = false) String alias,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) Integer weight,
			@RequestParam(required = false) MultipartFile image) {

		Person person = personService.findPersonByHttpRequest(request);
		try {
			if (alias != null)
				person.setAlias(alias);
			if (name != null)
				person.setName(name);
			if (date != null)
				person.setDate(date);

			if (weight != null)
				person.setWeight(weight);

			if (image != null) {
				byte[] imageData = image.getBytes();

				// object Image
				Picture imageF = new Picture(null);
				imageF.setContent(image.getContentType());
				imageF.setName(image.getOriginalFilename());
				imageF.setData(imageData);

				pictureService.savePicture(imageF);
				Thread.sleep(1000);

				person.setImage(imageF);
			}
			personService.save(person);
			PersonDTO personDTO = new PersonDTO(person, personService);
			return ResponseEntity.ok(personDTO);

		} catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
		}

	}

	@DeleteMapping("/")
	public ResponseEntity<?> deletePerson(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		try {
			personService.deletePerson(person);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
		}

	}

	@DeleteMapping("/friends")
	public ResponseEntity<?> deleteFriend(HttpServletRequest request, @RequestParam("friendId") long friendId) {
		try {
			Person friend = personService.findById(friendId);
			Person person = personService.findPersonByHttpRequest(request);
			try {
				person.getFriends().remove(friend);
				friend.getFriends().remove(person);

				Iterator<News> iterator = person.getNews().iterator();
				while (iterator.hasNext()) {
					News news = iterator.next();
					if (news.getAlias().equals(friend.getAlias())) {
						iterator.remove(); 
						newsService.delete(news);
					}
				}

				iterator = person.getNews().iterator();
				while (iterator.hasNext()) {
					News news = iterator.next();
					if (news.getAlias().equals(person.getAlias())) {
						iterator.remove(); 
						newsService.delete(news);
					}
				}

				personService.save(person);
				personService.save(friend);
				return ResponseEntity.ok().build();

			} catch (Exception e2) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/friends/requests") /// SEND REQUEST///
	public ResponseEntity<?> sendFriendRequest(HttpServletRequest request, @RequestParam("friendId") long friendId) {
		try {
			Person person = personService.findPersonByHttpRequest(request);
			Person person2 = personService.findById(friendId);
			try {
				Notification notification = new Notification(person.getAlias());
				notificationService.save(notification);
				person2.getlNotifications().add(notification);
				personService.save(person2);
				return ResponseEntity.ok().body(null);
			} catch (Exception e2) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/friends/requests")
	public ResponseEntity<?> processRequest(HttpServletRequest request, @RequestParam("requestId") long requestId,
			@RequestParam("accepted") boolean accepted) {

		try {
			Person receptor = personService.findPersonByHttpRequest(request);
			List<Notification> notificationsUser = receptor.getLNotifications();
			Notification notification = notificationService.findNotificationById(requestId);
			String originalText = notification.getContent();
			notificationsUser.remove(notification);
			personService.save(receptor);
			notificationService.deleteNotification(notification);
			if (accepted) {
				try {
					int positionTwoPoints = originalText.indexOf(":");
					String textAfterPoints = originalText.substring(positionTwoPoints + 1);
					String cleanText = textAfterPoints.trim();
					Person sender = personService.findByAlias(cleanText);
					if (!receptor.getFriends().contains(sender)) {
						receptor.getFriends().add(sender);
						sender.getFriends().add(receptor);
						personService.save(sender);
						personService.save(receptor);
					}
					return ResponseEntity.ok().body(null);
				} catch (Exception e2) {

					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
				}
			} else {
				return ResponseEntity.ok().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build(); // Did not found receptor
		}
	}

	@GetMapping("/requests")
	public ResponseEntity<List<Notification>> getRequests(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		return ResponseEntity.ok(person.getlNotifications());

	}

	@GetMapping("/news") // Added page parameter (check it)
	public ResponseEntity<List<News>> getNews(HttpServletRequest request, @RequestParam int iteracion) {

		Person person = personService.findPersonByHttpRequest(request);
		Page<News> pNews = personService.findNews(person, iteracion);
		List<News> page = pNews.getContent();
		return ResponseEntity.ok(page);

	}

	@GetMapping("/notifications")
	public ResponseEntity<List<Notification>> getNotifications(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		return ResponseEntity.ok(person.getlNotifications());

	}

	@GetMapping("/news/{id}")
	public ResponseEntity<News> showNotification(HttpServletRequest request, @PathVariable Long id) {
		Person person = personService.findPersonByHttpRequest(request);
		News news = newsService.findNewsById(id).orElseThrow();
		Person person2=personService.findByAlias(news.getName());
		if (person.getFriends().contains(person2)) {
			return ResponseEntity.ok(news);
		} else {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

	}

}
