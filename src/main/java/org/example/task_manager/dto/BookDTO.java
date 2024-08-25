package org.example.task_manager.dto;

import jakarta.validation.constraints.NotBlank;
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
public class BookDTO extends BaseDTO {
    private Integer id;
    @NotBlank(message = "book name cannot be blank")
    private String bookName;
    private String bookDescription;
}
