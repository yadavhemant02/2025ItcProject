package com.itcbusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = "com.itcbusiness")
public class ItcBusinessTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItcBusinessTrackApplication.class, args);
	}

}

//@Cacheable("customers")
