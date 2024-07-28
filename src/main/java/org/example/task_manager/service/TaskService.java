package org.example.task_manager.service;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;

import java.util.Optional;

public interface TaskService {
    void createTask(TaskDTO aTask, BookDTO aBook);
    Optional<TaskDTO> getTaskById(Integer id);
    void deleteTask(Integer taskID);
    void markCompleted(TaskDTO task);
}
