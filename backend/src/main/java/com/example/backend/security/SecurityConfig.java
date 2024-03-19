package com.example.backend.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.backend.security.jwt.UnauthorizedHandlerJwt;
import com.example.backend.security.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
    public RepositoryUserDetailsService userDetailService;

	@Autowired
  	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
					.requestMatchers(HttpMethod.POST,"/api/exercises/").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST,"/api/exercises/image/").hasRole("ADMIN")
					.requestMatchers(HttpMethod.DELETE,"/api/exercises/").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT,"/api/exercises/").hasRole("ADMIN")
					.requestMatchers(HttpMethod.DELETE,"/api/exercises/image/").hasRole("ADMIN")
					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
    @Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		http
          .csrf()
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); //In order to permit token in js

		
		http
			.authorizeHttpRequests(authorize -> authorize	

					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/js/**").permitAll()
					.requestMatchers("/images/**").permitAll()


					.requestMatchers("/index").permitAll()
					.requestMatchers("/register").permitAll()
					.requestMatchers("/newUser").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/mainPage").permitAll()
					.requestMatchers("/mainPage/exerciseSearch").permitAll()
					.requestMatchers("/mainPage/group/**").permitAll()					
					.requestMatchers("/searchEx").permitAll()
					.requestMatchers("/searchEx/**").permitAll()
					.requestMatchers("/mainPage/exerciseSearch/exercise/**").permitAll()
					.requestMatchers("/group").permitAll()
					.requestMatchers("/mainPage/statistics").hasAnyRole("USER")
					.requestMatchers("/starterNews").hasAnyRole("USER")
					.requestMatchers("/mainPage/community").hasAnyRole("USER")
					.requestMatchers("/searchUsers").hasAnyRole("USER")
					.requestMatchers("/sendRequest").hasAnyRole("USER")
					.requestMatchers("/notifications").hasAnyRole("USER")
					.requestMatchers("/processRequest").hasAnyRole("USER")
					.requestMatchers("/loadFriends").hasAnyRole("USER")
					.requestMatchers("/loadRutines").hasAnyRole("USER")
					.requestMatchers("/loadCharts").hasAnyRole("USER")
					.requestMatchers("/deleteUser").hasAnyRole("USER")
					.requestMatchers("/mainPage/showRutine").hasAnyRole("USER")
					.requestMatchers("/sendComment").hasAnyRole("USER")
					.requestMatchers("/deleteComment").hasAnyRole("USER")
					.requestMatchers("/mainPage/person").hasAnyRole("USER")
					.requestMatchers("/mainPage/person/config").hasAnyRole("USER")
					.requestMatchers("/add/**").hasAnyRole("USER")
					.requestMatchers("/mainPage/rutine/addRutine").hasAnyRole("USER")
					.requestMatchers("/mainPage/rutine/addEx/false/**").hasAnyRole("USER")
					.requestMatchers("/mainPage/rutine/addEx/true/**").hasAnyRole("USER")
					.requestMatchers("/mainPage/rutine/**").hasAnyRole("USER")
					.requestMatchers("/mainPage/editRutine/**").hasAnyRole("USER")
					.requestMatchers("/mainPage/rutine/newExercise/**").hasAnyRole("USER")
					.requestMatchers("/cancel/**").hasAnyRole("USER")
					.requestMatchers("/deleteRutine/**").hasAnyRole("USER")
					.requestMatchers("/editRutine/**").hasAnyRole("USER")
					.requestMatchers("/deleteExRutine/**").hasAnyRole("USER")
					.requestMatchers("/pdf/download/**").hasAnyRole("USER")
					.requestMatchers("/pdf/").hasAnyRole("USER")
					.requestMatchers("/mainPage/newRoutine/**").hasAnyRole("USER")
					.requestMatchers("/**").hasAnyRole("ADMIN")	
						

			)
			.formLogin(formLogin -> formLogin
					.loginPage("/index")
					.failureUrl("/errorPage")
					.defaultSuccessUrl("/mainPage")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

	
		return http.build();
		
	}
	

}
