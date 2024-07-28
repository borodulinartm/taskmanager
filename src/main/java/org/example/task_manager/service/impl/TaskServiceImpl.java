package org.example.task_manager.service.impl;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.mapping.BookMapper;
import org.example.task_manager.mapping.TaskMapper;
import org.example.task_manager.models.Task;
import org.example.task_manager.repositry.TaskRepository;
import org.example.task_manager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final BookMapper bookMapper;
    private final TaskMapper taskMapper;

    // @Autowired
    public TaskServiceImpl(TaskRepository repository, TaskMapper mapper, BookMapper bookMapper) {
        this.bookMapper = bookMapper;
        taskMapper = mapper;
        taskRepository = repository;
    }

    @Override
    public void createTask(TaskDTO aTask, BookDTO aBook) {
        aTask.setBook(bookMapper.bookDTOToBook(aBook));
        taskRepository.save(taskMapper.taskDTOToTask(aTask));
    }

    @Override
    public Optional<TaskDTO> getTaskById(Integer id) {
        Optional<Task> aOptionalTask = taskRepository.findById(id);
        if (aOptionalTask.isPresent()) {
            TaskDTO aTaskDTO = taskMapper.taskToTaskDTO(aOptionalTask.get());
            return Optional.of(aTaskDTO);
        }

        return Optional.ofNullable(null);
    }

    @Override
    public void deleteTask(Integer taskID) {
        taskRepository.deleteById(taskID);
    }

    @Override
    public void markCompleted(TaskDTO task) {
        task.setCompleted(true);
        taskRepository.save(taskMapper.taskDTOToTask(task));
    }
}
