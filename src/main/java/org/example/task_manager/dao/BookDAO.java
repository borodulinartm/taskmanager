package org.example.task_manager.dao;

import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookDAO {
    private List<Book> booksList;

    public BookDAO() {
        booksList = new ArrayList<>();

        booksList.add(new Book("Book 1", "This is the book number 1"));
    }

    public List<Book> getBooks() {
        return booksList;
    }

    // Находим по заданному ID-шнику наш журнал
    public Book getBookByID(int id) {
        return booksList.stream().filter(book -> book.getBookID() == id).findAny().get();
    }

    public void addTask(int id, Task task) {
        if (isBookExists(id)) {
            Book findedBook = getBookByID(id);
            findedBook.addTask(task);
        }
    }

    public boolean deleteTask(int bookID, int taskID) {
        if (isTaskExists(bookID, taskID)) {
            List<Task> allTasks = getBookByID(bookID).getListTasks();
            return allTasks.removeIf(curTask -> curTask.getId() == taskID);
        } else {
            return false;
        }
    }

    // Private section
    private boolean isBookExists(int id) {
        return ! booksList.stream().filter(book -> book.getBookID() == id).toList().isEmpty();
    }

    private boolean isTaskExists(int bookID, int taskID) {
        List<Task> allTasks = getBookByID(bookID).getListTasks();
        return ! allTasks.stream().filter(curTask -> curTask.getId() == taskID).toList().isEmpty();
    }
}
