package org.example.task_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Book extends BaseCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "The name cannot be empty")
    @Column(nullable = false)
    private String bookName;

    private String bookDescription;

    @OneToMany(mappedBy = "book")
    private List<Task> listTasks;
}
