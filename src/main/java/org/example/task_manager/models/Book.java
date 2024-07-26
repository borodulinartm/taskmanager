package org.example.task_manager.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Book {
    private static int curID = 1;

    private int bookID;

    @NotEmpty(message = "The name cannot be empty")
    private String bookName;

    private String bookDescription;

    private List<Task> listTasks;
    
    {
        listTasks = new ArrayList<>();
    }

    public Book(String bookName, String bookDescription) {
        this.bookID = curID;
        this.bookName = bookName;
        this.bookDescription = bookDescription;

        ++curID;
    }

    public Book(String bookName) {
        this(bookName, "");
    }

    public void addTask(Task addTask) {
        listTasks.add(addTask);
    }
}
