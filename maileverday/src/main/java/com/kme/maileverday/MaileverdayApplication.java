package com.kme.maileverday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MaileverdayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaileverdayApplication.class, args);
	}

}
