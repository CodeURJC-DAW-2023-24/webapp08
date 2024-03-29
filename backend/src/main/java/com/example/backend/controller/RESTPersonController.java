package com.example.backend.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.PersonDTO;
import com.example.backend.DTO.RutineDTO;
import com.example.backend.model.ExRutine;
import com.example.backend.model.Exercise;
import com.example.backend.model.News;
import com.example.backend.model.Notification;
import com.example.backend.model.Person;
import com.example.backend.model.Picture;
import com.example.backend.model.Rutine;
import com.example.backend.service.NewsService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PersonService;
import com.example.backend.service.PictureService;
import com.example.backend.service.RutineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
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



	 @Operation(summary = "Get person by logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content)
    })
	@GetMapping("/")
	public ResponseEntity<?> getPerson(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		PersonDTO personDTO = new PersonDTO(person, personService);
		return ResponseEntity.ok(personDTO);

	}


	@Operation(summary = "Create new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New person created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
            }),
		})
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		person.setEncodedPassword(passwordEncoder.encode(person.getEncodedPassword()));
		personService.save(person);
		URI location = fromCurrentRequest().path("{id}").buildAndExpand(person.getId()).toUri();

		return ResponseEntity.created(location).body(person);
	}
	@Operation(summary = "Edit person by user request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person has been edited", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
            }),
			@ApiResponse(responseCode = "401", description = "You are not logged", content = @Content)
    })
	@PatchMapping("/")
	public ResponseEntity<?> editPerson(HttpServletRequest request, // check if it goes into the body and not into the
																	// url
			@RequestParam(required = false) String alias,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) Integer weight) {

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

			personService.save(person);
			PersonDTO personDTO = new PersonDTO(person, personService);
			return ResponseEntity.ok(personDTO);

		} catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
		}

	}
	@Operation(summary = "Create new image by logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New image created", content = {
                    @Content(mediaType = "image/jpeg")}),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content)
    })
	@PostMapping("/image")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> setUserImage(HttpServletRequest request, @RequestParam MultipartFile image) {
		Person person = personService.findPersonByHttpRequest(request);
		try {
			Picture imageN = pictureService.newPicture(image);
			person.setImage(imageN);
			personService.save(person);
			return ResponseEntity.ok().build();
		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@Operation(summary = "Get image by logged user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found image", content = {
                    @Content(mediaType = "image/jpeg")}),
            @ApiResponse(responseCode = "404", description = "Image dosen´t exit", content = @Content),
     })
	@GetMapping("/image")
	public ResponseEntity<?> getImage(HttpServletRequest request) throws IOException {
		Person person = personService.findPersonByHttpRequest(request);
		if (person.getImage() != null){
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(person.getImage().getData());

		}
		return ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Delete person by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person has been deleted", content = @Content),
			@ApiResponse(responseCode = "403", description = "You are not admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "That person doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePerson(@PathVariable long id) {

		Person person = personService.findById(id);
		try {
			personService.deletePerson(person);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
		}

	}

	@Operation(summary = "Get request list by logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content)
    })
	@GetMapping("/requests")
	public ResponseEntity<List<Notification>> getRequests(HttpServletRequest request) {

		Person person = personService.findPersonByHttpRequest(request);
		return ResponseEntity.ok(person.getlNotifications());

	}
	@Operation(summary = "Send request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "404", description = "The person dosen´t exit", content = @Content),
            
    })
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
	@Operation(summary = "Response request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response request", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "404", description = "The person dosen´t exit", content = @Content),
            
    })
	@PutMapping("/friends/requests/{requestId}")
	public ResponseEntity<?> processRequest(HttpServletRequest request, @PathVariable long requestId,
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

	@Operation(summary = "Delete friend by friend id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete friend", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "404", description = "The person dosen´t exit", content = @Content),
            
    })
	@DeleteMapping("/friends/{friendId}")
	public ResponseEntity<?> deleteFriend(HttpServletRequest request, @PathVariable long friendId) {
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

	@Operation(summary = "Get news list by logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			})
	@GetMapping("/news") // Added page parameter (check it)
	public ResponseEntity<List<News>> getNews(HttpServletRequest request, @RequestParam int iteracion) {

		Person person = personService.findPersonByHttpRequest(request);
		Page<News> pNews = personService.findNews(person, iteracion);
		List<News> page = pNews.getContent();
		return ResponseEntity.ok(page);

	}
	@Operation(summary = "Get new by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			@ApiResponse(responseCode = "404", description = "The new dosen´t exit", content = @Content),
    
    })
	@GetMapping("/news/{id}")
	public ResponseEntity<News> showNotification(HttpServletRequest request, @PathVariable Long id) {
		Person person = personService.findPersonByHttpRequest(request);
		News news = newsService.findNewsById(id).orElseThrow();
		Person person2 = personService.findByAlias(news.getAlias());
		if (person.getFriends().contains(person2)) {
			return ResponseEntity.ok(news);
		} else {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

	}
	@Operation(summary = "Get chart by logged user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found chart", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            }),
            @ApiResponse(responseCode = "401", description = "You are not logged", content = @Content),
			
    
    })
	@GetMapping("/charts")
	public ResponseEntity<?> loadChart(HttpServletRequest request){
		Map<String, Integer> map = new HashMap<>();
		String[] grpMuscle = {
				"Pecho",
				"Espalda",
				"Biceps",
				"Triceps",
				"Hombro",
				"Tren Inferior",
				"Cardio"
		};
		for (int i = 0; i < grpMuscle.length; i++) {
			map.put(grpMuscle[i], 0);

		}
		try {
			Person person = personService.findPersonByHttpRequest(request);
		List<Rutine> lrutines = person.getRutines();
		for (Rutine rutine : lrutines) {
			List<ExRutine> lExcer = rutine.getExercises();
			for (ExRutine exercise : lExcer) {
				int index = Arrays.asList(grpMuscle).indexOf(exercise.getGrp());
				int value = map.get(grpMuscle[index]);
				value += 1;
				map.put(grpMuscle[index], value);

			}
		}
		return ResponseEntity.ok(map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
		
	}
}
