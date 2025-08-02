package com.example.bookcrud.repository;

import com.example.bookcrud.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT get_total_books_by_author(?1)", nativeQuery = true)
    int getTotalBooksByAuthor(Long authorId);
}