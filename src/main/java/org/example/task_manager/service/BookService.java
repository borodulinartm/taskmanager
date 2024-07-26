package org.example.task_manager.service;

import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;

import java.util.Optional;

public interface BookService {
    Iterable<Book> allBooks();
    void createBook(Book newBook);
    Optional<Book> getBookById(Integer id);
    void deleteBook(Integer id);
    void addTask(Integer bookID, Task aTask);
}
