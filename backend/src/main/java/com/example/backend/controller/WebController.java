package com.example.backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.backend.model.ExRutine;
import com.example.backend.model.News;
import com.example.backend.model.Notification;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;


import com.example.backend.service.NotificationService;
import com.example.backend.service.PersonService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController implements CommandLineRunner {
	@Autowired
	private PersonService personService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void run(String... args) throws Exception {
	}

	@GetMapping("/errorPage")
	public String errorPage(Model model) {
		return "errorPage";
	}

	@GetMapping("/mainPage/community")
	public String community(Model model, HttpServletRequest request) {
		model.addAttribute("search", false);
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		return "community";
	}

	@GetMapping("/searchUsers")
	public @ResponseBody Map<String, Object> searchbyName(@RequestParam String nombre, HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		Boolean bAdmin = request.isUserInRole("ADMIN");
		List<String[]> lNameId = personService.getIdandAlias(nombre, user.getId());
		Map<String, Object> response = new HashMap<>();
		response.put("lNameId", lNameId);
		response.put("bAdmin", bAdmin);

		return response;
	}

	@PostMapping("/sendRequest")
	public @ResponseBody Boolean sendRequest(@RequestParam String id, HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person sender = personService.findByAlias(alias);
		Person receiver = personService.findById(Long.parseLong(id));
		Notification notification = new Notification(sender.getAlias());
		notificationService.save(notification);
		receiver.getLNotifications().add(notification);
		personService.save(receiver);
		return true;
	}

	@GetMapping("/notifications")
	public @ResponseBody List<Notification> getNotifications(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();

		List<Notification> lNotifications = personService.findByAlias(alias).getLNotifications();

		return lNotifications;
	}

	@PostMapping("/processRequest")
	public @ResponseBody void processRequest(@RequestParam Notification notification, @RequestParam boolean aceptar,
			HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person receptor = personService.findByAlias(alias);

		if (aceptar) {
			String originalText = notification.getContent();
			int positionTwoPoints = originalText.indexOf(":");
			String textAfterPoints = originalText.substring(positionTwoPoints + 1);
			String cleanText = textAfterPoints.trim();
			Person sender = personService.findByAlias(cleanText);
			if (!receptor.getFriends().contains(sender)) {
				receptor.getFriends().add(sender);
				sender.getFriends().add(receptor);
				personService.save(sender);
			}
		}
		List<Notification> notificationsUser = receptor.getLNotifications();
		notificationsUser.remove(notification);
		personService.save(receptor);
		notificationService.deleteNotification(notification);
	}

	@GetMapping("/starterNews")
	public @ResponseBody List<Object> getNews(@RequestParam int iteracion, HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		Page<News> pNews = personService.findNews(user, iteracion);
		List<News> page = pNews.getContent();
		Boolean top = pNews.hasNext();
		List<Object> data = new ArrayList<>(Arrays.asList(page, top));

		return data; // list<news>
	}

	@GetMapping("/loadFriends")
	public @ResponseBody List<String> loadFriends(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		List<String> lFriends = personService.findAliasofFriendsByPerson(user);

		return lFriends;
	}

	@GetMapping("/loadRutines")
	public @ResponseBody List<Rutine> getOwnRutines(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		List<Rutine> rutines = user.getRutines();
		return rutines;
	}

	@GetMapping("/mainPage/statistics")
	public String statistics(Model model, HttpServletRequest request) {
		model.addAttribute("adEx", request.isUserInRole("ADMIN"));
		return "progress";
	}

	@GetMapping("/loadCharts")
	public @ResponseBody Map<String, Integer> loadCharts(HttpServletRequest request) {
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
		String alias = request.getUserPrincipal().getName();
		Person user = personService.findByAlias(alias);
		List<Rutine> lrutines = user.getRutines();
		for (Rutine rutine : lrutines) {
			List<ExRutine> lExcer = rutine.getExercises();
			for (ExRutine exercise : lExcer) {
				int index = Arrays.asList(grpMuscle).indexOf(exercise.getGrp());
				int value = map.get(grpMuscle[index]);
				value += 1;
				map.put(grpMuscle[index], value);

			}
		}
		return map;
	}

}
