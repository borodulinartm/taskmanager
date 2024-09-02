package org.example.task_manager.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "personName", column = @Column(name = "createdUser_name")),
        @AttributeOverride(name = "personSurname", column = @Column(name = "createdUser_surname")),
        @AttributeOverride(name = "personEmail", column =  @Column(name = "createdUser_email"))
    })
    private Person createdUser;

    @Column(insertable = false)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "personName", column = @Column(name = "updatedUser_name")),
        @AttributeOverride(name = "personSurname", column = @Column(name = "updatedUser_surname")),
        @AttributeOverride(name = "personEmail", column =  @Column(name = "updatedUser_email"))
    })
    private Person updatedUser;
}
