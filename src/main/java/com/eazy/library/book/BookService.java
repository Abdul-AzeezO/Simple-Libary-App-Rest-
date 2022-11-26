package com.eazy.library.book;

import com.eazy.library.exceptions.ResourceExistsException;
import com.eazy.library.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository repository;

    public void addBook(BookRequestBody body) {
        log.info("adding book");
        if (repository.existsByIsbn(body.getIsbn())) {
            throw new ResourceExistsException("book already exists");
        }
        final Book book = new Book(
                body.getName(),
                body.getIsbn(),
                body.getDescription(),
                body.getImageUrl()
        );
        repository.save(book);
    }

    public void updateBook(Long bookId, BookRequestBody body) {
        log.info("Updating book");
        final Book book = repository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found")
                );
        if (book.getName().equalsIgnoreCase(body.getName()) &&
                book.getIsbn().equalsIgnoreCase(body.getIsbn()) &&
                book.getDescription().equalsIgnoreCase(body.getDescription()) &&
                book.getImageUrl().equalsIgnoreCase(body.getImageUrl())) {
            log.error("no changes in book {}", bookId);
            throw new ResourceExistsException("book already exists");
        }
        book.setName(body.getName());
        book.setIsbn(body.getIsbn());
        book.setDescription(body.getDescription());
        book.setImageUrl(body.getImageUrl());
        repository.save(book);
    }

    public void deleteBook(Long bookId) {
        log.info("Deleting book {}", bookId);
        final Book book = repository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        repository.delete(book);
    }

    public List<Book> getAllBooks() {
        log.info("fetching all books");
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("error in fetching all books", e);
            throw new ResourceNotFoundException("No books found");
        }
    }
}
