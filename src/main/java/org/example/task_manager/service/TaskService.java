package org.example.task_manager.service;

import org.example.task_manager.models.Task;

import java.util.Optional;

public interface TaskService {
    void createTask(Task aTask);
    Optional<Task> getTaskById(Integer id);
    void deleteTask(Integer taskID);
}
