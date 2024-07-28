package org.example.task_manager.service;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.models.Task;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDTO> allBooks();
    void createBook(BookDTO newBook);
    Optional<BookDTO> getBookById(Integer id);
    void deleteBook(Integer id);
    void addTask(Integer bookID, Task aTask);
}
