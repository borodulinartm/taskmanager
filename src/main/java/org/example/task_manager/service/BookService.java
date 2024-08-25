package org.example.task_manager.service;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDTO> allBooks();
    void createBook(BookDTO newBook);
    Optional<BookDTO> getBookById(Integer id);
    BookDTO updateBook(Integer bookID, BookDTO updatingBook);
    void deleteBook(Integer id);
    void addTask(Integer bookID, TaskDTO aTask);
}
