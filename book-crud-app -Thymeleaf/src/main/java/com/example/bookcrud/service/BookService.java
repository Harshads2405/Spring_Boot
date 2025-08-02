//9011-765bd0aa49df" artifact_version_id="1b44981c-35e9-419f-9ffd-2cd7c83aed7a" title="BookService.java" contentType="text/x-java">
package com.example.bookcrud.service;

import com.example.bookcrud.entity.Book;
import com.example.bookcrud.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

        @Service
        @RequiredArgsConstructor
        public class BookService {
            private final BookRepository bookRepository;

            @Transactional
            public Book saveBook(Book book) {
                return bookRepository.save(book);
            }

            @Cacheable("books")
            public List<Book> getAllBooks() {
                return bookRepository.findAll();
            }

            @Cacheable(value = "books", key = "#id")
            public Optional<Book> getBookById(Long id) {
                return bookRepository.findById(id);
            }

            @Transactional
            @CacheEvict(value = "books", allEntries = true)
            public Book updateBook(Long id, Book bookDetails) {
                Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Book not found"));
                book.setTitle(bookDetails.getTitle());
                book.setIsbn(bookDetails.getIsbn());
                book.setPublicationDate(bookDetails.getPublicationDate());
                book.setPrice(bookDetails.getPrice());
                book.setAuthor(bookDetails.getAuthor());
                return bookRepository.save(book);
            }

            @Transactional
            @CacheEvict(value = "books", allEntries = true)
            public void deleteBook(Long id) {
                bookRepository.deleteById(id);
            }

            public int getTotalBooksByAuthor(Long authorId) {
                return bookRepository.getTotalBooksByAuthor(authorId);
            }
        }