package org.example.task_manager.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

import org.example.task_manager.etc.PriorityTasks;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Task extends BaseCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "The name cannot be null")
    private String name;
    
    @NotEmpty(message = "The description cannot be null")
    private String descriptionTask;
    
    @NotNull(message = "The date cannot be null")
    @FutureOrPresent(message = "The date cannot be less than current date")
    private LocalDate dateCompleting;

    @Enumerated(EnumType.STRING)
    private PriorityTasks priorityTasks;

    @JsonProperty("isCompleted")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private Book book;

    @Override
    public final boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;

        Class<?> oOtherClass = otherObject instanceof HibernateProxy ?
                ((HibernateProxy) otherObject).getHibernateLazyInitializer().getImplementationClass() :
                otherObject.getClass();

        Class<?> oThisClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass() :
                this.getClass();

        if (oOtherClass != oThisClass) return true;

        Task otherTask = (Task) otherObject;
        return getId() != null && Objects.equals(getId(), otherTask.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass().hashCode() :
                this.getClass().hashCode();
    }
}
