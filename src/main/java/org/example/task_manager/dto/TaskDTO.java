package org.example.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.task_manager.etc.PriorityTasks;

import java.time.LocalDate;

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
    private boolean completed;
    private BookDTO book;
}
