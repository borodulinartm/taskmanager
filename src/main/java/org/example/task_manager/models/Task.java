package org.example.task_manager.models;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Task {
    private static int CURRENT_ID = 1;

    private int id;
    @NotEmpty(message = "The name cannot be null")
    private String name;
    
    @NotEmpty(message = "The description cannot be null")
    private String descriptionTask;
    
    @NotNull(message = "The date cannot be null")
    @FutureOrPresent(message = "The date cannot be less than current date")
    private LocalDate dateCompleting;

    private PriorityTasks priorityTasks;

    public static Task createNewTask(String name, String descriptionTask, LocalDate date, PriorityTasks tasks) {
        return new Task(CURRENT_ID++, name, descriptionTask, date, tasks);
    }

    public static Task createNewTask(int id, String name, String descriptionTask, LocalDate date, PriorityTasks tasks) {
        return new Task(id, name, descriptionTask, date, tasks);
    }

    private Task(int id, String name, String descriptionTask, LocalDate date, PriorityTasks tasks) {
        this.id = id;
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.dateCompleting = date;
        this.priorityTasks = tasks;
    }

    public boolean isTaskNew() {
        return id < 0;
    }

    public enum PriorityTasks {
        LOW, MEDIUM, HIGH
    }
}
