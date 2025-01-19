package com.yoprogramoenjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YoprogramoenjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoprogramoenjavaApplication.class, args);
	}

}
