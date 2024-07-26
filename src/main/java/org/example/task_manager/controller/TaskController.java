package org.example.task_manager.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.example.task_manager.models.Task.PriorityTasks;
import org.example.task_manager.service.BookService;
import org.example.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@SessionAttributes("curBook")
public class TaskController {
    private final BookService bookService;
    private final TaskService taskService;

    @Autowired
    public TaskController(BookService bookService, TaskService taskService) {
        this.taskService = taskService;
        this.bookService = bookService;
    }

    @GetMapping("/tasks/{id}")
    public String getTaskByID(@PathVariable(name = "id") Integer taskID, Model model) {
        Optional<Task> curTask = taskService.getTaskById(taskID);
        model.addAttribute("task", curTask.get());

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
            @Valid @ModelAttribute(name = "newTask") Task newTask, Errors errors, RedirectAttributes attributes,
            @ModelAttribute(name = "curBook") Book currentBook) {
        if (errors.hasErrors()) {
            return "task/new_task";
        }

        if (newTask.getPriorityTasks() == null) {
            newTask.setPriorityTasks(PriorityTasks.LOW);
        }

        if (currentBook != null) {
            newTask.setBook(currentBook);
            taskService.createTask(newTask);

            attributes.addFlashAttribute("success_text", "A task has added successfully");

            return "redirect:/books/" + currentBook.getId();
        } else {
            return "redirect:/tasks";
        }
    }

    // Редактирование текущей задачи
    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable(name = "id") Integer taskID, Model model) {
//        Task task = tasksDAO.getTaskById(taskID);
//        if (task.isTaskNew()) {
//            log.error("The task is not exist");
//        } else {
//            model.addAttribute("editedTask", task);
//        }

        return "task/edit_task";
    }

    @PostMapping("/tasks/{id}/edit")
    public ModelAndView postEditTask(
            @PathVariable(name = "id") Integer id,
            @Valid @ModelAttribute(name = "editedTask") Task editedTask, Errors error,
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

//        tasksDAO.updateTask(id, editedTask.getName(),
//            editedTask.getDescriptionTask(), editedTask.getDateCompleting(), editedTask.getPriorityTasks());

        // Формирование ModelAndView
        mView.setViewName("redirect:/tasks/{id}");

        redirectAttributes.addFlashAttribute("ok", true);
        redirectAttributes.addFlashAttribute("message", "The task with ID = " + id + " updated successfully");

        return mView;
    }

    @GetMapping("/tasks/{id}/delete")
    public ModelAndView deleteTask(
            @PathVariable(name = "id") Integer taskID, final RedirectAttributes attributes,
            @ModelAttribute("curBook") Book currentBook) {
//        if (tasksDAO.isTaskExists(taskID)) {
//            tasksDAO.deleteTask(taskID);
//        }

        // Redirect Attributes для отображения доп.информации
        ModelAndView mov = new ModelAndView();
//        String redirectUrl = "redirect:/books/" + currentBook.getBookID();

        attributes.addFlashAttribute("ok", true);
        attributes.addFlashAttribute("message", "The task with ID " + taskID + " has removed");

//        mov.setViewName("");
        return mov;
    }
}