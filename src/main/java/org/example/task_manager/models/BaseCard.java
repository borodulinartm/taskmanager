package org.example.task_manager.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseCard {
    @Column(updatable = false)
    private LocalDateTime creationDate;

    @Column(insertable = false)
    private LocalDateTime updatedTime;

    @Column(updatable = false)
    private String createdUser;

    @Column(insertable = false)
    private String updatedUser;
}
