package org.example.task_manager.controller;

import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exceptions.TaskNotFoundException;
import org.example.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/task", produces = {
        "application/json", "text/xml"
})
public class TaskAPI {
    private final TaskService taskService;

    @Autowired
    public TaskAPI(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDTO> getTaskByiD(@PathVariable(name = "id") Integer taskID) {
        Optional<TaskDTO> curTask = taskService.getTaskById(taskID);
        if (curTask.isPresent()) {
            return ResponseEntity.ok(curTask.get());
        }

        throw new TaskNotFoundException("Task with ID = " + taskID + " not found");
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable(name = "id") Integer taskID,
                                              @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(taskID, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable(name = "id") Integer taskID) {
        taskService.deleteTask(taskID);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> completeTask(@PathVariable(name = "id") Integer taskID) {
        TaskDTO completedTask = taskService.markCompleted(taskID);
        return ResponseEntity.ok(completedTask);
    }
}
