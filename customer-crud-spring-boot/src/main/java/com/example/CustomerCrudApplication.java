package com.example;
import com.example.controller.CustomerController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CustomerCrudApplication implements CommandLineRunner {
    private final CustomerController controller;

    public CustomerCrudApplication(CustomerController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerCrudApplication.class, args);
    }

    @Override
    public void run(String... args) {
        controller.start();
    }
}