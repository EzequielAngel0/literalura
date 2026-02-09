package com.alura.literalura.service;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.dto.BookDTO;
import com.alura.literalura.dto.GutendexResponseDTO;
import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GutendexService gutendexService;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
            GutendexService gutendexService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.gutendexService = gutendexService;
    }

    /**
     * Searches for a book by title in the Gutendex API and saves it to the
     * database.
     * If the book already exists, returns the existing book.
     * If the author doesn't exist, creates a new author record.
     * 
     * @param title The title of the book to search for
     * @return The saved Book entity
     * @throws RuntimeException if the book is not found in the API or cannot be
     *                          saved
     */
    public Book searchAndSaveBook(String title) {
        // Fetch from API
        GutendexResponseDTO response = gutendexService.fetchBooks(title);
        if (response.results().isEmpty()) {
            throw new RuntimeException("Libro no encontrado en la API de Gutendex");
        }

        BookDTO bookDTO = response.results().get(0);

        // Check if book already exists
        Optional<Book> existingBook = bookRepository.findByTitleContainsIgnoreCase(bookDTO.title());
        if (existingBook.isPresent()) {
            return existingBook.get();
        }

        // Handle author (find or create)
        Author author = null;
        if (!bookDTO.authors().isEmpty()) {
            AuthorDTO authorDTO = bookDTO.authors().get(0);

            Optional<Author> existingAuthor = authorRepository.findByName(authorDTO.name());
            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                author = new Author(authorDTO.name(), authorDTO.birthYear(), authorDTO.deathYear());
                author = authorRepository.saveAndFlush(author);
            }
        }

        // Create and save book
        String language = bookDTO.languages().isEmpty() ? "unknown" : bookDTO.languages().get(0);
        Book book = new Book(bookDTO.title(), author, language, bookDTO.downloadCount());

        try {
            book = bookRepository.saveAndFlush(book);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el libro: " + e.getMessage(), e);
        }

        return book;
    }

    /**
     * Retrieves all books from the database.
     * 
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves all authors from the database.
     * 
     * @return List of all authors
     */
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Finds authors who were alive in a specific year.
     * 
     * @param year The year to check
     * @return List of authors alive in the specified year
     */
    public List<Author> getAuthorsAliveInYear(int year) {
        return authorRepository.findAuthorsAliveInYear(year);
    }

    /**
     * Retrieves books filtered by language.
     * 
     * @param language The language code (e.g., "es", "en")
     * @return List of books in the specified language
     */
    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    // ========== ADVANCED FEATURES ==========

    /**
     * Generates statistical summary of book downloads.
     * 
     * @return DoubleSummaryStatistics containing count, sum, min, max, and average
     */
    public java.util.DoubleSummaryStatistics getBookStatistics() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .mapToDouble(Book::getDownloads)
                .summaryStatistics();
    }

    /**
     * Retrieves the top 10 most downloaded books.
     * 
     * @return List of up to 10 books ordered by download count (descending)
     */
    public List<Book> getTop10MostDownloaded() {
        return bookRepository.findTop10ByOrderByDownloadsDesc();
    }

    /**
     * Searches for authors by name (case-insensitive, partial match).
     * 
     * @param name The name or partial name to search for
     * @return List of authors matching the search criteria
     */
    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainsIgnoreCase(name);
    }

    /**
     * Finds authors born within a specific year range.
     * 
     * @param startYear The start year of the range (inclusive)
     * @param endYear   The end year of the range (inclusive)
     * @return List of authors born between the specified years
     */
    public List<Author> getAuthorsByBirthYearRange(Integer startYear, Integer endYear) {
        return authorRepository.findByBirthYearBetween(startYear, endYear);
    }
}
