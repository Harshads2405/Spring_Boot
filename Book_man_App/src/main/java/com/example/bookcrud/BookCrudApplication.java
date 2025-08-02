package com.example.bookcrud;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableBatchProcessing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class BookCrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookCrudApplication.class, args);
    }
}