package com.example.bookcrud.batch;

import com.example.bookcrud.model.Book;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaPagingItemReader<Book> reader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT b FROM Book b")
                .pageSize(100)
                .build();
    }

    @Bean
    public JpaItemWriter<Book> writer() {
        JpaItemWriter<Book> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Book, Book>chunk(100, transactionManager)
                .reader(reader())
                .processor(item -> {
                    item.setPrice(item.getPrice().multiply(new java.math.BigDecimal("1.1")));
                    return item;
                })
                .writer(writer())
                .build();
    }

    @Bean
    public Job updateBookPricesJob() {
        return new JobBuilder("updateBookPricesJob", jobRepository)
                .start(step1())
                .build();
    }
}