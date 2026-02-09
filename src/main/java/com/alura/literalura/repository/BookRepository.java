package com.alura.literalura.repository;

import com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainsIgnoreCase(String title);

    List<Book> findByLanguage(String language);

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findBooksByLanguage(String language);

    // Top 10 most downloaded books
    List<Book> findTop10ByOrderByDownloadsDesc();
}
