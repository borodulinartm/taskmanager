package org.example.task_manager.mapping;

import java.util.List;

import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task taskDTOToTask(TaskDTO taskDTO);
    TaskDTO taskToTaskDTO(Task task);

    List<TaskDTO> tasksToTaskDTOs(List<Task> tasks);
    List<Task> tasksDTOToTasks(List<TaskDTO> taskDTOs);
}