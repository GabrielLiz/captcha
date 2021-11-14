package com.captcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
public class BackendBiocaptchaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendBiocaptchaApplication.class, args);
	}

}
