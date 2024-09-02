package org.example.task_manager.service.impl;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exceptions.TaskNotFoundException;
import org.example.task_manager.mapping.TaskMapper;
import org.example.task_manager.models.Task;
import org.example.task_manager.repositry.TaskRepository;
import org.example.task_manager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // @Autowired
    public TaskServiceImpl(TaskRepository repository, TaskMapper mapper) {
        taskMapper = mapper;
        taskRepository = repository;
    }

    @Override
    public void createTask(TaskDTO aTask, BookDTO aBook) {
        taskRepository.save(taskMapper.taskDTOToTask(aTask));
    }

    @Override
    public Optional<TaskDTO> getTaskById(Integer id) {
        Optional<Task> aOptionalTask = taskRepository.findById(id);
        if (aOptionalTask.isPresent()) {
            TaskDTO aTaskDTO = taskMapper.taskToTaskDTO(aOptionalTask.get());
            return Optional.of(aTaskDTO);
        }

        throw new TaskNotFoundException("Task with id " + id + " not found");
    }

    @Override
    public void deleteTask(Integer taskID) {
        taskRepository.deleteById(taskID);
    }

    @Override
    public TaskDTO markCompleted(Integer taskID) {
        Optional<Task> aOptionalTask = taskRepository.findById(taskID);
        if (aOptionalTask.isPresent()) {
            Task aTask = aOptionalTask.get();

            aTask.setCompleted(true);
            taskRepository.save(aTask);

            return taskMapper.taskToTaskDTO(aTask);
        }

        throw new TaskNotFoundException("Task with id = " + taskID + " not found");
    }

    @Override
    public TaskDTO updateTask(Integer taskID, TaskDTO newTask) {
        Optional<Task> curOptionalTask = taskRepository.findById(taskID);
        if (curOptionalTask.isPresent()) {
            Task currentTask = curOptionalTask.get();

            Task updatedTask = taskMapper.taskDTOToTask(newTask);
            if (updatedTask.getName() != null) {
                currentTask.setName(updatedTask.getName());
            }
            if (updatedTask.getDescriptionTask() != null) {
                currentTask.setDescriptionTask(updatedTask.getDescriptionTask());
            }
            if (updatedTask.getPriorityTasks() != null) {
                currentTask.setPriorityTasks(updatedTask.getPriorityTasks());
            }
            if (updatedTask.getDateCompleting() != null) {
                currentTask.setDateCompleting(updatedTask.getDateCompleting());
            }

            taskRepository.save(currentTask);

            return taskMapper.taskToTaskDTO(currentTask);
        }

        throw new TaskNotFoundException("Task with id = " + taskID + " not found");
    }
}
