package com.example.bookcrud.controller;

import com.example.bookcrud.entity.Book;
import com.example.bookcrud.repository.AuthorRepository;
import com.example.bookcrud.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ThymeleafController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        if (session.getAttribute("token") != null) {
            return "redirect:/books";
        }
        model.addAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/auth/login",
                    Map.of("username", username, "password", password),
                    String.class
            );
            session.setAttribute("token", response.getBody());
            return "redirect:/books";
        } catch (Exception e) {
            session.setAttribute("error", "Invalid credentials");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("token");
        return "redirect:/login";
    }

    @GetMapping("/books")
    public String listBooks(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<List> booksResponse = restTemplate.exchange(
                    "http://localhost:8080/api/books",
                    HttpMethod.GET,
                    request,
                    List.class
            );
            model.addAttribute("books", booksResponse.getBody());

            Map<Long, Integer> authorCounts = new HashMap<>();
            for (Object bookObj : booksResponse.getBody()) {
                Map<String, Object> book = (Map<String, Object>) bookObj;
                Map<String, Object> author = (Map<String, Object>) book.get("author");
                Long authorId = ((Number) author.get("id")).longValue();
                ResponseEntity<Integer> countResponse = restTemplate.exchange(
                        "http://localhost:8080/api/books/author/" + authorId + "/count",
                        HttpMethod.GET,
                        request,
                        Integer.class
                );
                authorCounts.put(authorId, countResponse.getBody());
            }
            model.addAttribute("authorCounts", authorCounts);

            return "books";
        } catch (Exception e) {
            session.setAttribute("error", "Error fetching books: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/books/new")
    public String showBookForm(Model model, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepository.findAll());
        return "book-form";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + session.getAttribute("token"));
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Book> response = restTemplate.exchange(
                    "http://localhost:8080/api/books/" + id,
                    HttpMethod.GET,
                    request,
                    Book.class
            );
            model.addAttribute("book", response.getBody());
            model.addAttribute("authors", authorRepository.findAll());
            return "book-form";
        } catch (Exception e) {
            session.setAttribute("error", "Error fetching book: " + e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/create")
    public String createBook(@ModelAttribute Book book, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Book> request = new HttpEntity<>(book, headers);

            restTemplate.postForEntity("http://localhost:8080/api/books", request, Void.class);
            return "redirect:/books";
        } catch (Exception e) {
            session.setAttribute("error", "Error creating book: " + e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Book> request = new HttpEntity<>(book, headers);

            restTemplate.put("http://localhost:8080/api/books/" + id, request);
            return "redirect:/books";
        } catch (Exception e) {
            session.setAttribute("error", "Error updating book: " + e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            restTemplate.exchange("http://localhost:8080/api/books/" + id, HttpMethod.DELETE, request, Void.class);
            return "redirect:/books";
        } catch (Exception e) {
            session.setAttribute("error", "Error deleting book: " + e.getMessage());
            return "redirect:/books";
        }
    }
}