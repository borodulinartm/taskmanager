package org.example.task_manager.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.Task;
import org.slf4j.Marker;
import org.springframework.stereotype.Component;
import org.example.task_manager.models.Task.PriorityTasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@Slf4j
public class TasksDAO {
    private final List<Task> tasksList;

    // Можно использовать как статический блок инициализации, так и конструктор. Всё будет работать
    public TasksDAO() {
        log.debug(Marker.ANY_MARKER, "Инициализация блока кода");

        tasksList = new ArrayList<>();

        tasksList.add(Task.createNewTask("Task #1", "Do something",
                LocalDate.of(2001, 10, 6), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #2", "Do something 2",
                LocalDate.of(2021, 1, 16), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #3", "Do something 3",
                LocalDate.of(2014, 5, 3), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #4", "Do something 4",
                LocalDate.of(2019, 7, 7), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #5", "Do something 5",
                LocalDate.of(2017, 3, 5), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #6", "Do something 6",
                LocalDate.of(2024, 4, 9), PriorityTasks.LOW));
        tasksList.add(Task.createNewTask("Task #7", "Do something 7",
                LocalDate.of(2009, 8, 7),PriorityTasks.LOW));
    }

    public boolean isTaskExists(int id) {
        return ! tasksList.stream().filter(task -> task.getId() == id).toList().isEmpty();
    }

    public Task getTaskById(int id) {
        return tasksList.stream().filter(task -> task.getId() == id)
                .findAny()
                .orElseGet(() -> Task.createNewTask(-1, "Temp task", "temp description", LocalDate.now(), PriorityTasks.LOW));
    }

    public void addNewTask(Task newTask) {
        tasksList.add(newTask);
    }

    // А штука-то рабочая)
    // Получаешь список задач и редачишь непосредственно то, что нужно без сохранения в список
    public void updateTask(int id, String name, String description, LocalDate dateCompleting, PriorityTasks priorityTasks) {
        Task updatingTask = getTaskById(id);
        if (!updatingTask.isTaskNew()) {
            updatingTask.setName(name);
            updatingTask.setDescriptionTask(description);
            updatingTask.setDateCompleting(dateCompleting);
            updatingTask.setPriorityTasks(priorityTasks);
        }
    }

    public void deleteTask(int id) {
        tasksList.removeIf(task -> task.getId() == id);
    }
}
