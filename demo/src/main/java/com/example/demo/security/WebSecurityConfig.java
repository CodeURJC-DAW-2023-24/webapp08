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

					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/js/**").permitAll()
					.requestMatchers("/images/**").permitAll()

					.requestMatchers("/index").permitAll()
					.requestMatchers("/register").permitAll()
					.requestMatchers("/newUser").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/main").permitAll()
					.requestMatchers("/muscGr").permitAll()
					.requestMatchers("/group/**").permitAll()					
					.requestMatchers("/searchEx").permitAll()
					.requestMatchers("/searchEx/**").permitAll()
					.requestMatchers("/exercise/**").permitAll()
					.requestMatchers("/group").permitAll()
					.requestMatchers("/group/**").permitAll()
					

					.requestMatchers("/statistics").hasAnyRole("USER")
					.requestMatchers("/starterNews").hasAnyRole("USER")
					.requestMatchers("/comunity").hasAnyRole("USER")
					.requestMatchers("/searchUsers").hasAnyRole("USER")
					.requestMatchers("/sendRequest").hasAnyRole("USER")
					.requestMatchers("/notifications").hasAnyRole("USER")
					.requestMatchers("/processRequest").hasAnyRole("USER")
					.requestMatchers("/loadFriends").hasAnyRole("USER")
					.requestMatchers("/loadRutines").hasAnyRole("USER")
					.requestMatchers("/loadCharts").hasAnyRole("USER")
					.requestMatchers("/deleteUser").hasAnyRole("USER")
					.requestMatchers("/showRutine").hasAnyRole("USER")
					.requestMatchers("/sendComment").hasAnyRole("USER")
					.requestMatchers("/user").hasAnyRole("USER")
					.requestMatchers("/editUser").hasAnyRole("USER")
					.requestMatchers("/add/**").hasAnyRole("USER")
					.requestMatchers("/adRutine").hasAnyRole("USER")
					.requestMatchers("/addEx/**").hasAnyRole("USER")
					.requestMatchers("/rutine/**").hasAnyRole("USER")
					.requestMatchers("/addExRutine/**").hasAnyRole("USER")
					.requestMatchers("/cancel/**").hasAnyRole("USER")
					.requestMatchers("/pdf/download/**").hasAnyRole("USER")
					.requestMatchers("/pdf**").hasAnyRole("USER")

					.requestMatchers("/**").hasAnyRole("ADMIN")		

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
