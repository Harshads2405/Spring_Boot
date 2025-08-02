package com.example.bookcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookCrudAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookCrudAppApplication.class, args);
	}
}