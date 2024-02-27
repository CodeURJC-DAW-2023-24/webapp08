package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
    public RepositoryUserDetailsService userDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					
					.requestMatchers("/index").permitAll()
					.requestMatchers("/register").permitAll()
					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/images/**").permitAll()
                    .requestMatchers("/newUser").permitAll()
					.requestMatchers("/muscGr").permitAll()
					.requestMatchers("/main").permitAll()
					.requestMatchers("/js/**").permitAll()
					.requestMatchers("/novedades-iniciales").permitAll() //revisar por que si no lo pongo no va
					.requestMatchers("/comunity").permitAll()
					.requestMatchers("/busqueda").permitAll()
					.requestMatchers("/sendSolicitud").permitAll()
					.requestMatchers("/notificaciones").permitAll()
					.requestMatchers("/procesarSolicitud").permitAll()
					.requestMatchers("/cargarAmigos").permitAll()
					.requestMatchers("/addEjercicioRutina").permitAll()
					.requestMatchers("/cargarRutinas").permitAll()
					.requestMatchers("/verRutina").permitAll()
					.requestMatchers("/enviarComentario").permitAll()
					.requestMatchers("/user").hasAnyRole("USER")
					.requestMatchers("/editUser").hasAnyRole("USER")
					.requestMatchers("/exForm").hasAnyRole("ADMIN")
					.requestMatchers("/newEx**").hasAnyRole("ADMIN")
					.requestMatchers("/add/**").hasAnyRole("USER")
					.requestMatchers("/adRutine").hasAnyRole("USER")
					.requestMatchers("/group/**").permitAll()					
					.requestMatchers("/addEx/**").hasAnyRole("USER")
					.requestMatchers("/addExRutine/**").hasAnyRole("USER")
					.requestMatchers("/busquedaEx/**").permitAll()
					.requestMatchers("/busquedaEx").permitAll()
					.requestMatchers("/exercise/**").permitAll()
					.requestMatchers("/group").permitAll()
					.requestMatchers("/group/pecho").permitAll()
					.requestMatchers("/group/triceps").permitAll()
					.requestMatchers("/group/biceps").permitAll()
					.requestMatchers("/group/espalda").permitAll()
					.requestMatchers("/group/cardio").permitAll()
					.requestMatchers("/group/inferior").permitAll()
					.requestMatchers("/group/hombro").permitAll()					
					.requestMatchers("/group/pecho/**").permitAll()
					.requestMatchers("/group/triceps/**").permitAll()
					.requestMatchers("/group/biceps/**").permitAll()
					.requestMatchers("/group/espalda/**").permitAll()
					.requestMatchers("/group/cardio/**").permitAll()
					.requestMatchers("/group/inferior/**").permitAll()
					.requestMatchers("/group/hombro/**").permitAll()
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/index")
					.failureUrl("/error")
					.defaultSuccessUrl("/main")
					.permitAll() //revisar por que no va
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/sendSolicitud")); //Es q le tengo q pasar el token pero ns como 
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/procesarSolicitud"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/add"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/enviarComentario"));
		return http.build();
		
	}
	
}
