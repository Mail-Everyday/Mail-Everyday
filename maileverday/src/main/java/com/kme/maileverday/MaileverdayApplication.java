package com.kme.maileverday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class MaileverdayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaileverdayApplication.class, args);
	}

	@Bean  // 수정, 삭제를 위한 Bean 등록
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}

}
