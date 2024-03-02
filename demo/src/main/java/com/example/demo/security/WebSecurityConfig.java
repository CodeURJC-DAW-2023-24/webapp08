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
					.requestMatchers("/index").permitAll()
					.requestMatchers("/register").permitAll()
					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/statistics").hasAnyRole("USER")
					.requestMatchers("/images/**").permitAll()
                    .requestMatchers("/newUser").permitAll()
					.requestMatchers("/muscGr").permitAll()
					.requestMatchers("/main").permitAll()
					.requestMatchers("/js/**").permitAll()
					.requestMatchers("/starterNews").permitAll() 
					.requestMatchers("/comunity").hasAnyRole("USER")
					.requestMatchers("/searchUsers").permitAll()
					.requestMatchers("/sendRequest").hasAnyRole("USER")
					.requestMatchers("/notifications").permitAll()
					.requestMatchers("/processRequest").permitAll()
					.requestMatchers("/loadFriends").permitAll()
					.requestMatchers("/addEjercicioRutina").permitAll()
					.requestMatchers("/loadRutines").permitAll()
					.requestMatchers("/loadCharts").permitAll()
					.requestMatchers("/deleteUser").permitAll()
					.requestMatchers("/showRutine").permitAll()
					.requestMatchers("/sendComment").permitAll()
					.requestMatchers("/user").hasAnyRole("USER")
					.requestMatchers("/editUser").hasAnyRole("USER")
					.requestMatchers("/exForm").hasAnyRole("ADMIN")
					.requestMatchers("/newEx**").hasAnyRole("ADMIN")
					.requestMatchers("/add/**").hasAnyRole("USER")
					.requestMatchers("/adRutine").hasAnyRole("USER")
					.requestMatchers("/group/**").permitAll()					
					.requestMatchers("/addEx/**").hasAnyRole("USER")
					.requestMatchers("/rutine/**").hasAnyRole("USER")
					.requestMatchers("/addExRutine/**").hasAnyRole("USER")
					.requestMatchers("/searchEx/**").permitAll()
					.requestMatchers("/searchEx").permitAll()
					.requestMatchers("/exercise/**").permitAll()
					.requestMatchers("/cancel/**").hasAnyRole("USER")
					.requestMatchers("/group").permitAll()
					.requestMatchers("/group/**").permitAll()
					/* .requestMatchers("/group/pecho").permitAll()
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
					.requestMatchers("/group/hombro/**").permitAll()*/
					.requestMatchers("/pdf/download/**").permitAll()
					.requestMatchers("/pdf**").permitAll()
					.requestMatchers("/deleteEx/**").permitAll()
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/index")
					.failureUrl("/error")
					.defaultSuccessUrl("/main")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/sendRequest"));  
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/processRequest"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/add"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/sendComment"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/deleteUser"));

		
		return http.build();
		
	}
	
}
