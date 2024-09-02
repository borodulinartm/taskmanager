package org.example.task_manager.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty(message = "The name cannot be null")
    private String name;
    @NotEmpty(message = "The description cannot be null")
    private String descriptionTask;
    @NotNull(message = "The date cannot be null")
    @FutureOrPresent(message = "The date cannot be less than current date")
    private LocalDate dateCompleting;
    @Enumerated(EnumType.STRING)
    private PriorityTasks priorityTasks;
    private boolean completed;
    private BookDTO book;
}
