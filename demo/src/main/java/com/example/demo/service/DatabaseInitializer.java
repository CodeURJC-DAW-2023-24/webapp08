package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exercise;
import com.example.demo.model.Person;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.PersonRepository;

@Service
public class DatabaseInitializer {

		
	@Autowired
		private ExerciseRepository exerciseRepository;

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${admin.password}")
    private String adminPassword;
	
	@SuppressWarnings("null")
	@PostConstruct
	public void init() throws SerialException, SQLException {
		personRepository.save(new Person("user", passwordEncoder.encode("pass"),"paco","1993-04-06",90, "USER"));
		personRepository.save(new Person("marta03", passwordEncoder.encode("qwerty"),"Marta","1993-04-06",70, "USER"));
		personRepository.save(new Person("admin", adminPassword, "paco","1993-04-06",90,"USER", "ADMIN"));
		personRepository.save(new Person("1", passwordEncoder.encode("1"),"paco","1993-04-06",90,"USER"));
		personRepository.save(new Person("paquito99", passwordEncoder.encode("qwerty"),"El Paco","1997-03-31",88,"USER"));
		personRepository.save(new Person("lolalolita05", passwordEncoder.encode("qwerty"),"Lolita la influ","2002-09-25",90,"USER"));
		
		exerciseRepository.save(new Exercise("Curl de biceps con mancuerna de pie", "Para realizar el curl de bíceps", "Biceps", "https://www.youtube.com/embed/rqy0oxx__sU?si=8JkDYGNHXgpVg3NB"));
		exerciseRepository.save(new Exercise("Press de banca con agarre cerrado", "El press de banca con agarre cerrado es una variación del clásico press de banca que se enfoca más en el trabajo de los tríceps, aunque también involucra los pectorales y los deltoides frontales. ", "Triceps", "https://www.youtube.com/embed/SF0uoT4JWNw?si=1cSxJLlUKBy9N6rD"));
		exerciseRepository.save(new Exercise("Jalon al pecho con agarre supino", "Ejercicio de entrenamiento de fuerza que se enfoca en el desarrollo de la musculatura de la espalda, especialmente los músculos del dorsal ancho, los trapecios y los romboides.", "Espalda", "https://www.youtube.com/embed/SnLxcN1x3LU?si=INQ8p5xUHCFKEuHQ"));
		exerciseRepository.save(new Exercise("Apertura en polea baja", "Este ejercicio trabaja principalmente los músculos pectorales, pero también involucra los deltoides anteriores y los músculos estabilizadores del core para mantener la postura adecuada durante el movimiento. ", "Pecho", "https://www.youtube.com/embed/6oqK95erRik?si=qFbVl8E5f-lxe1yJ"));
		exerciseRepository.save(new Exercise("Eliptica", "Hacer ejercicio en una máquina elíptica ofrece beneficios cardiovasculares, de quema de calorías y fortalecimiento muscular, con bajo impacto en las articulaciones. Además, mejora la salud ósea, equilibrio y coordinación.", "Cardio", "https://www.youtube.com/embed/4Kzx79WZ72A?si=n3fFAktfBsUAtVBB"));
		exerciseRepository.save(new Exercise("Dominadas con agarre supino", "Las dominadas en agarre cerrado implican agarrar la barra con las manos más cerca que el ancho de los hombros, elevando el cuerpo hasta que el mentón sobrepase la barra y descendiendo controladamente.", "Espalda", "0"));
		exerciseRepository.save(new Exercise("Barras paralelas", "Levanta tu cuerpo con la fuerza de tus brazos hasta que los codos estén completamente extendidos, luego baja controladamente hasta que los codos formen un ángulo de 90 grados. ", "Triceps", "0"));
		exerciseRepository.save(new Exercise("Sentadilla bulgara", "Coloca un pie en un banco o plataforma elevada detrás de ti y el otro pie en el suelo frente a ti. Desciende flexionando la rodilla del pie delantero hasta que el muslo esté paralelo al suelo, manteniendo la espalda recta. ", "Inferior", "https://www.youtube.com/embed/K-6DG1hcHzU?si=7s81ckZlLqt4oeBG"));
		exerciseRepository.save(new Exercise("Press de banca con barra", "El individuo se acuesta sobre un banco plano con los pies apoyados en el suelo. Luego, agarra una barra con ambas manos, la baja hacia el pecho y luego la empuja hacia arriba hasta que los brazos estén completamente extendidos.", "Pecho", "0"));
		exerciseRepository.save(new Exercise("Cinta de correr", "Correr en la cinta proporciona un ejercicio cardiovascular efectivo que ayuda a quemar calorías y mejorar la resistencia. Además, al ser una actividad de bajo impacto, reduce el estrés en las articulaciones.", "Cardio", "0"));
		exerciseRepository.save(new Exercise("Dominadas con agarre prono", "En las dominadas con agarre abierto, el individuo se cuelga de una barra elevada con las palmas de las manos mirando hacia afuera y separadas a una distancia mayor que el ancho de los hombros.", "Espalda", "https://www.youtube.com/embed/Corxi8VEKjI?si=1rKEMMQYb0hn1WT9"));
		exerciseRepository.save(new Exercise("Extension de triceps en polea", "De pie frente a una máquina de polea con la barra o cuerda de agarre ajustada en lo alto, luego extender brazos hacia abajo con los codos cerca del cuerpo y luego flexionar los codos para llevar la barra hacia abajo..", "Triceps", "https://www.youtube.com/embed/dRkTreltpnc?si=DZJ42vCyd-QPuc-s"));
		exerciseRepository.save(new Exercise("Curl en barra Z", "El curl de bíceps en Z implica sostener una barra con un agarre supino, flexionar los codos para levantarla hacia los hombros mientras se contraen los bíceps, y luego bajarla controladamente a la posición inicial.", "Biceps", "0"));
		exerciseRepository.save(new Exercise("Aductores", "Fortalecer los músculos aductores del muslo interior. El ejercicio se realiza típicamente sentado, con las piernas en posición de apertura y luego se contraen los músculos para llevar las piernas juntas. ", "Inferior", "0"));
		
	}

}