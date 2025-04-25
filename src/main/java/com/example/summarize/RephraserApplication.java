package com.example.summarize;

import com.example.summarize.api.GoogleAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RephraserApplication {

	public static void main(String[] args) {
		SpringApplication.run(RephraserApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(GoogleAPI googleAPI) {
		return args -> {
			 String key = googleAPI.getAPI_KEY();
			 System.out.println(key);
		};
	}

}
