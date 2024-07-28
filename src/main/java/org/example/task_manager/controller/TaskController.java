package org.example.task_manager.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.etc.PriorityTasks;
import org.example.task_manager.models.Task;
import org.example.task_manager.service.BookService;
import org.example.task_manager.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@Slf4j
@SessionAttributes("curBook")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(BookService bookService, TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{id}")
    public String getTaskByID(@PathVariable(name = "id") Integer taskID, Model model, 
            @ModelAttribute(name = "curBook") BookDTO curBook) {
        Optional<TaskDTO> curTask = taskService.getTaskById(taskID);
        TaskDTO aTask = curTask.orElse(null);
        
        model.addAttribute("task", aTask);
        model.addAttribute("curBook", curBook);

        return "task/selected_task";
    }

    @GetMapping("/tasks/new")
    public String createNewTaskForm(Model model) {
        model.addAttribute("newTask", Task.builder().build());
        return "task/new_task";
    }

    @ModelAttribute(name = "taskPriorities")
    public PriorityTasks[] addPrioritiesToTask() {
        return PriorityTasks.values();
    }

    @PostMapping("/tasks/new")
    public String postNewTask(
            @Valid @ModelAttribute(name = "newTask") TaskDTO newTask, Errors errors, RedirectAttributes attributes,
            @ModelAttribute(name = "curBook") BookDTO currentBook) {
        if (errors.hasErrors()) {
            return "task/new_task";
        }

        if (newTask.getPriorityTasks() == null) {
            newTask.setPriorityTasks(PriorityTasks.LOW);
        }

        if (currentBook != null) {
            taskService.createTask(newTask, currentBook);

            attributes.addFlashAttribute("success_text", "A task has added successfully");

            return "redirect:/books/" + currentBook.getId();
        } else {
            return "redirect:/tasks";
        }
    }

    // Редактирование текущей задачи
    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable(name = "id") Integer taskID, Model model) {
        Optional<TaskDTO> editedTask = taskService.getTaskById(taskID);
        editedTask.ifPresentOrElse(arg0 -> model.addAttribute("editedTask", arg0),
            () -> log.error("No task selected", new NoSuchFieldError())
        );

        return "task/edit_task";
    }

    @PostMapping("/tasks/{id}/edit")
    public ModelAndView postEditTask(
            @PathVariable(name = "id") Integer id, @Valid @ModelAttribute(name = "editedTask") TaskDTO editedTask, Errors error,
            @ModelAttribute(name = "curBook") BookDTO currentBook,
            RedirectAttributes redirectAttributes) { 
        ModelAndView mView = new ModelAndView();
        if (error.hasErrors()) {
            mView.setViewName("edit_task");
            return mView;
        }

        // На случай если приоритет задачи - пустой, добавляем null
        if (editedTask.getPriorityTasks() == null) {
            editedTask.setPriorityTasks(PriorityTasks.LOW);
        }

        taskService.createTask(editedTask, currentBook);

        // Формирование ModelAndView
        mView.setViewName("redirect:/tasks/{id}");

        redirectAttributes.addFlashAttribute("ok", true);
        redirectAttributes.addFlashAttribute("message", "The task with ID = " + id + " updated successfully");

        return mView;
    }

    @GetMapping("/tasks/{id}/delete")
    public ModelAndView deleteTask(
            @PathVariable(name = "id") Integer taskID, final RedirectAttributes attributes,
            @ModelAttribute("curBook") BookDTO currentBook) {
        if (taskID != null) {
            taskService.deleteTask(taskID);
        } else {
            log.info("Cannot delete task because the ID is null");
        }

        // Redirect Attributes для отображения доп.информации
        ModelAndView mov = new ModelAndView("redirect:/books/" + currentBook.getId());
        attributes.addFlashAttribute("success_text", "The task with ID " + taskID + " has removed");
        
        return mov;
    }

    @PostMapping("/tasks/{id}/complete")
    public ModelAndView completeTask(@PathVariable Integer id, Model model, @ModelAttribute(name = "task") TaskDTO task,
            RedirectAttributes attributes) {
        ModelAndView mView = new ModelAndView("task/selected_task");

        Optional<TaskDTO> aOptionalTask = taskService.getTaskById(id);
        TaskDTO aTask = aOptionalTask.orElse(null);

        if (aTask != null) {
            taskService.markCompleted(aTask);
        } else {
            log.info("No aTask provided");
        }

        model.addAttribute("task", aTask);
        return mView;
    }
}
