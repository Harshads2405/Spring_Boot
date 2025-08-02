package com.example.bookcrud.controller;

import com.example.bookcrud.model.Book;
import com.example.bookcrud.service.BookService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://127.0.0.1:8080/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job updateBookPricesJob;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{authorId}")
    public List<Book> getBooksByAuthor(@PathVariable Long authorId) {
        return bookService.getBooksByAuthor(authorId);
    }

    @PostMapping("/batch/update-prices")
    public String runBatchJob() throws Exception {
        JobParametersBuilder params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis());
        jobLauncher.run(updateBookPricesJob, params.toJobParameters());
        return "Batch job started";
    }
}