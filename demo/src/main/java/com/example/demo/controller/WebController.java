package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.ExRutine;
import com.example.demo.model.News;
import com.example.demo.model.Notification;
import com.example.demo.model.Person;
import com.example.demo.model.Rutine;

import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PersonRepository;


import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController implements CommandLineRunner {
	@Autowired
	private PersonRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

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
		Person user = userRepository.findByalias(alias).orElseThrow();
		Boolean bAdmin = request.isUserInRole("ADMIN");
		List<String[]> lNameId = userRepository.getIdandAlias(nombre, user.getId());
		Map<String, Object> response = new HashMap<>();
		response.put("lNameId", lNameId);
		response.put("bAdmin", bAdmin);

		return response;
	}

	@PostMapping("/sendRequest")
	public @ResponseBody Boolean sendRequest(@RequestParam String id, HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person sender = userRepository.findByalias(alias).orElseThrow();
		Person receiver = userRepository.findById(Long.parseLong(id)).orElseThrow();
		Notification notification = new Notification(sender.getAlias());
		notificationRepository.save(notification);
		receiver.getLNotifications().add(notification);
		userRepository.save(receiver);
		return true;
	}

	@GetMapping("/notifications")
	public @ResponseBody List<Notification> getNotifications(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();

		List<Notification> lNotifications = userRepository.findByalias(alias).orElseThrow().getLNotifications();

		return lNotifications;
	}

	@PostMapping("/processRequest")
	public @ResponseBody void processRequest(@RequestParam Notification notification, @RequestParam boolean aceptar,
			HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person receptor = userRepository.findByalias(alias).orElseThrow();

		if (aceptar) {
			String originalText = notification.getContent();
			int positionTwoPoints = originalText.indexOf(":");
			String textAfterPoints = originalText.substring(positionTwoPoints + 1);
			String cleanText = textAfterPoints.trim();
			Person sender = userRepository.findByalias(cleanText).orElseThrow();
			if (!receptor.getFriends().contains(sender)) {
				receptor.getFriends().add(sender);
				sender.getFriends().add(receptor);
				userRepository.save(sender);
			}
		}
		List<Notification> notificationsUser = receptor.getLNotifications();
		notificationsUser.remove(notification);
		userRepository.save(receptor);

	}

	@GetMapping("/starterNews")
	public @ResponseBody List<Object> getNews(@RequestParam int iteracion, HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = userRepository.findByalias(alias).orElseThrow();
		Page<News> pNews = userRepository.findByNews(user.getNews(), PageRequest.of(iteracion, 10));
		List<News> page = pNews.getContent();
		Boolean top = pNews.hasNext();
		List<Object> data = new ArrayList<>(Arrays.asList(page, top));

		return data; // list<news>
	}

	@GetMapping("/loadFriends")
	public @ResponseBody List<String> loadFriends(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = userRepository.findByalias(alias).orElseThrow();
		List<String> lFriends = userRepository.findaliasOfFriendsByPerson(user);

		return lFriends;
	}

	@GetMapping("/loadRutines")
	public @ResponseBody List<Rutine> getOwnRutines(HttpServletRequest request) {
		String alias = request.getUserPrincipal().getName();
		Person user = userRepository.findByalias(alias).orElseThrow();
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
		Person user = userRepository.findByalias(alias).orElseThrow();
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
