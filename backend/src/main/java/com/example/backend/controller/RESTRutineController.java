package com.example.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.backend.DTO.RutineDTO;
import com.example.backend.model.Comment;
import com.example.backend.model.ExRutine;
import com.example.backend.model.News;
import com.example.backend.model.Person;
import com.example.backend.model.Rutine;
import com.example.backend.service.CommentService;
import com.example.backend.service.ExRutineService;
import com.example.backend.service.NewsService;
import com.example.backend.service.PersonService;
import com.example.backend.service.RutineService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/rutines")
public class RESTRutineController {
    @Autowired
    private RutineService rutineService;
    @Autowired
    private PersonService personService;

    @Autowired
    ExRutineService exRutineService;

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @GetMapping("/")
    public List<RutineDTO> getRutines(HttpServletRequest request) {
        Person person = personService.findPersonByHttpRequest(request);
        List<Rutine> rutines = person.getRutines();
        List<RutineDTO> rutineDTOs = new ArrayList<>();
        for (Rutine rutine : rutines) {
            RutineDTO rutineDTO = new RutineDTO(rutine, personService);
            rutineDTOs.add(rutineDTO);
        }

        return rutineDTOs;
    }

    @PostMapping("/")
    public ResponseEntity<RutineDTO> createRutine(HttpServletRequest request, @RequestBody Rutine rutine) {
        try {
            Person person = personService.findPersonByHttpRequest(request);
            Date newRutineDate = rutine.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newRutineDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            newRutineDate = calendar.getTime();
            rutine.setDate(newRutineDate);
            rutineService.save(rutine);
            person.getRutines().add(rutine);
            personService.save(person);
            List<Person> lFriends = person.getFriends();
            if (!lFriends.isEmpty()) {
                for (Person friend : lFriends) {
                    News news = new News(person.getAlias());
                    news.setRutine(rutine);
                    newsService.save(news);
                    friend.getNews().add(news);
                    personService.save(friend);
                }
            }
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(rutine.getId())
                    .toUri();
            RutineDTO rutineDTO = new RutineDTO(rutine, personService);
            return ResponseEntity.created(location).body(rutineDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rutine> deleteRutine(@PathVariable long id, HttpServletRequest request) {
        Person user = personService.findByRutineId(id).orElseThrow();
        try {
            if (user.getAlias().equals(request.getUserPrincipal().getName())) {
                Rutine rutine = rutineService.findById(id).orElseThrow();
                List<News> listNews = (List<News>) newsService.findByRutineId(id);
                personService.deleteNewsById(listNews);
                String alias = request.getUserPrincipal().getName();
                Person userSesion = personService.findByAlias(alias);
                userSesion.getRutines().remove(rutine);
                personService.save(userSesion);
                rutineService.deleteById(id);
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }

        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editRutine(HttpServletRequest request, @RequestBody Rutine rutine, @PathVariable Long id) {

        Person person = personService.findPersonByHttpRequest(request);
        try {
            if (person.getAlias().equals(request.getUserPrincipal().getName())) {
                Rutine rutineEditable = rutineService.findById(id).orElseThrow();
                if (rutine.getTime() != null)
                    rutineEditable.setTime(rutine.getTime());
                if (rutine.getName() != null)
                    rutineEditable.setName(rutine.getName());
                if (rutine.getDate() != null)
                    rutineEditable.setDate(rutine.getDate());
                if (rutine.getExercises() != null)
                    rutineEditable.setExercises(rutine.getExercises());

                rutineService.save(rutineEditable);
                RutineDTO rutineDTO = new RutineDTO(rutineEditable, personService);
                return ResponseEntity.ok(rutineDTO);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e2.getMessage());
        }

    }

    @PostMapping("/{rutineId}/comments") // Psycho method and check it
    public ResponseEntity<?> postComment(HttpServletRequest request, @PathVariable long rutineId,
            @RequestBody JsonNode jsonNode) {
        String comment = jsonNode.get("comment").asText();

        Person person = personService.findPersonByHttpRequest(request);

        try {
            Rutine rutine = rutineService.findById(rutineId).orElseThrow();
            int positionRutine;
            positionRutine = person.getRutines().indexOf(rutine);
            if (positionRutine == -1) {
                List<News> lnews = person.getNews();
                int i = 0;
                for (News news : lnews) {
                    if (news.getRutine().getId() == rutineId) {
                        positionRutine = i;
                        break; // Check it
                    }
                    i = i + 1;
                }

                if (positionRutine == -1) {
                    return ResponseEntity.status(403).body("Its not your rutine or your friends one"); // Its not your
                                                                                                       // rutine or
                                                                                                       // your friends
                                                                                                       // one
                } else {// insert comment
                    rutine.getMessages().add(new Comment(person.getAlias(), comment));
                    rutineService.save(rutine);
                    return ResponseEntity.ok().build();
                }
            } else { // insert comment
                rutine.getMessages().add(new Comment(person.getAlias(), comment));
                rutineService.save(rutine);
                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{rutineId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable long rutineId,
            @PathVariable long commentId) {

        Person person = personService.findPersonByHttpRequest(request);
        try {
            Rutine rutine = rutineService.findById(rutineId).orElseThrow();
            Comment comment = commentService.findById(commentId);
            if (comment.getAlias().equals(person.getAlias())) {
                rutine.getMessages().remove(comment);
                rutineService.save(rutine);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(403).body("Thats not your comment");

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadPDF(@PathVariable Long id, HttpServletResponse response,
            HttpServletRequest request)
            throws IOException {
        Rutine rutine = rutineService.findById(id).orElseThrow();
        Person person = personService.findPersonByHttpRequest(request);
        List<Long> idsFriends = new ArrayList<>();
        for (Person friend : person.getFriends()) {
            List<Rutine> rutineListFriend = friend.getRutines();
            for (Rutine rutineFriend : rutineListFriend) {
                Long idRutineFriend = rutineFriend.getId();
                idsFriends.add(idRutineFriend);
            }

        }

        if (person.getAlias().equals(personService.findByRutineId(id).orElseThrow().getAlias())
                || idsFriends.contains(id)) {

            response.setContentType("application/pdf");

            String truncatedDate = rutine.getDate().toString(); // date in file
            String fileName = "rutina_" + truncatedDate + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            List<ExRutine> list = rutine.getExercises();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.TIMES_BOLD, 20);
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.showText("                                  RUTINA");
                    contentStream.newLineAtOffset(0, -30);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.showText("_________________________________________________________________");
                    contentStream.newLineAtOffset(0, -20);
                    for (ExRutine exRutine : list) {
                        contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11);
                        contentStream.showText("   Ejercicio: ");
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(exRutine.getExercise());
                        contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11);
                        contentStream.showText("    S X Rep: ");
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(exRutine.getSeries());
                        contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11);
                        contentStream.showText("    Peso (kg): ");
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(exRutine.getWeight().toString());
                        contentStream.newLineAtOffset(0, -10);
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.showText("_________________________________________________________________");
                        contentStream.newLineAtOffset(0, -20);
                    }

                    contentStream.endText();
                }

                document.save(response.getOutputStream());
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleRutine(HttpServletRequest request, @PathVariable Long id) {
        Person person = personService.findPersonByHttpRequest(request);
        Rutine rutine = rutineService.findById(id).orElseThrow();
        if(person.getRutines().contains(rutine)){
            RutineDTO rutineDTO = new RutineDTO(rutine, personService);
            return ResponseEntity.ok(rutineDTO);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

	@GetMapping("/friends/{id}")
    public ResponseEntity<?> getFriendRutine(HttpServletRequest request, @PathVariable Long id) {
        Person person = personService.findPersonByHttpRequest(request);
        Rutine rutine = rutineService.findById(id).orElseThrow();
		Person owner= personService.findByRutineId(id).orElseThrow();
        if(person.getFriends().contains(owner)){
            RutineDTO rutineDTO = new RutineDTO(rutine, personService);
            return ResponseEntity.ok(rutineDTO);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
