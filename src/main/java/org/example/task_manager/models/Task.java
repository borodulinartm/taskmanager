package org.example.task_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Task extends BaseCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty(message = "The name cannot be null")
    private String name;
    
    @NotEmpty(message = "The description cannot be null")
    private String descriptionTask;
    
    @NotNull(message = "The date cannot be null")
    @FutureOrPresent(message = "The date cannot be less than current date")
    private LocalDate dateCompleting;

    private PriorityTasks priorityTasks;

    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public enum PriorityTasks {
        LOW, MEDIUM, HIGH
    }
}
