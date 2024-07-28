package org.example.task_manager.dto;

import java.time.LocalDate;

import org.example.task_manager.etc.PriorityTasks;
import org.example.task_manager.models.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskDTO extends BaseDTO {
    private int id;
    private String name;
    private String descriptionTask;
    private LocalDate dateCompleting;
    private PriorityTasks priorityTasks;
    private boolean isCompleted;
    private Book book;
}
