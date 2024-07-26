package org.example.task_manager.service.impl;

import org.example.task_manager.models.Task;
import org.example.task_manager.repositry.TaskRepository;
import org.example.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        taskRepository = repository;
    }

    @Override
    public void createTask(Task aTask) {
        taskRepository.save(aTask);
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }
}
