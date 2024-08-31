package org.example.task_manager.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer code;
    private String message;
    private String timeStamp;
}
