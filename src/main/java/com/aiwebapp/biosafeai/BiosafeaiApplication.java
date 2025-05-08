package com.aiwebapp.biosafeai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class BiosafeaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiosafeaiApplication.class, args);
	}

}
