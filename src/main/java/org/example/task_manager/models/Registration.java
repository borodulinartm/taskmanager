package org.example.task_manager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private String nickname;
    private String email;

    // #TODO Add password check
    private String password;
}
