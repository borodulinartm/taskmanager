package org.example.task_manager.service;

import org.example.task_manager.models.Book;

import java.util.Optional;

public interface BookService {
    Iterable<Book> allBooks();
    void createBook(Book newBook);
    Optional<Book> getBookById(Integer id);
    void deleteBook(Integer id);
}
