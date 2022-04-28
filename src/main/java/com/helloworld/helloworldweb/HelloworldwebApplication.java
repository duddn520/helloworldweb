package com.helloworld.helloworldweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HelloworldwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldwebApplication.class, args);
	}

}
