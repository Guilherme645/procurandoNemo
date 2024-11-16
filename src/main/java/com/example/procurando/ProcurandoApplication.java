package com.example.procurando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.procurando")
public class ProcurandoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProcurandoApplication.class, args);
	}
}
