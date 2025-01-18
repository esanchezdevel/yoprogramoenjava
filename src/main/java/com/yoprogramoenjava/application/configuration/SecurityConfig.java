package com.yoprogramoenjava.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${admin.password}")
    private String adminPassword;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/admin/**") // protect admin endpoints
				.authenticated()
				.anyRequest()
				.permitAll() // allow all other endpoints
				)
				.formLogin((login) -> login.permitAll())
				.logout((logout) -> logout.permitAll());
		
		return http.build();
	}
	
	@Bean
	UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails adminUser = User.builder()
				.username("admin")
				.password(passwordEncoder.encode(adminPassword))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(adminUser);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
