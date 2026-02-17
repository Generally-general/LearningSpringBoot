package com.myProject.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MySpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringProjectApplication.class, args);
	}

}
