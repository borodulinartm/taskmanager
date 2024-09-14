package org.example.task_manager.models.body;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authorization {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String role;
}
