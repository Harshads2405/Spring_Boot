package com.example.bookcrud.repository;

import com.example.bookcrud.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);

    @Query(value = "CALL GetBooksByAuthor(:authorId)", nativeQuery = true)
    List<Book> findBooksByAuthorUsingProcedure(@Param("authorId") Long authorId);
}